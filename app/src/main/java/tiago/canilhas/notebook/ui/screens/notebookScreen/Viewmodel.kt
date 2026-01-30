package tiago.canilhas.notebook.ui.screens.notebookScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tiago.canilhas.notebook.data.db.entity.Group
import tiago.canilhas.notebook.data.db.entity.Notebook
import tiago.canilhas.notebook.data.db.entity.Page
import tiago.canilhas.notebook.data.db.entity.Section
import tiago.canilhas.notebook.data.repository.GroupRepository
import tiago.canilhas.notebook.data.repository.NotebookRepository
import tiago.canilhas.notebook.data.repository.PageRepository
import tiago.canilhas.notebook.data.repository.SectionRepository
import kotlin.collections.emptyList

sealed interface State {
    object Loading : State
    data class Idle(
        val notebook: Notebook,

        // Data
        val sections: List<Section>,
        val groups: List<Group>,
        val pages: List<Page>,

        // Selection
        val selected: Selection = Selection(),

        // UI Auxiliary
        val activePopup: ActivePopup? = null,
        val drawingState: DrawingState = DrawingState(),
        val isTabOpen: Boolean = true
    ) : State
    data class Error(val exception: Throwable) : State
}

enum class PopupTarget { SECTION, GROUP }

sealed interface ActivePopup {
    val title: String
    val target: PopupTarget

    data class Add(
        override val target: PopupTarget,
        override val title: String = ""
    ) : ActivePopup

    data class Edit(
        val id: Long,
        override val target: PopupTarget,
        override val title: String
    ) : ActivePopup
}

data class Selection(
    val sectionId: Long? = null,
    val groupId: Long? = null,
    val pageId: Long? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModel(
    savedStateHandle: SavedStateHandle,
    private val notebookRepository: NotebookRepository,
    private val sectionRepository: SectionRepository,
    private val groupRepository: GroupRepository,
    private val pageRepository: PageRepository
) : ViewModel() {

    private val notebookId: Long = checkNotNull(savedStateHandle["notebookId"])
    private val _selection = MutableStateFlow(Selection())
    private val drawingHelper = DrawingHelper(viewModelScope, pageRepository)

    private val _stateFlow = MutableStateFlow<State>(State.Loading)
    val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()

    init {
        initializeDataFlow()
    }

    private fun initializeDataFlow() {
        viewModelScope.launch {
            val notebookFlow = notebookRepository.getNotebookById(notebookId)
            val sectionsFlow = sectionRepository.getSectionsForNotebook(notebookId)

            val groupsFlow = _selection
                .map { it.sectionId }
                .distinctUntilChanged()
                .flatMapLatest { sectionId ->
                    if (sectionId != null) groupRepository.getGroupsForSection(sectionId)
                    else flowOf(emptyList())
                }

            val pagesFlow = _selection
                .map { it.groupId }
                .distinctUntilChanged()
                .flatMapLatest { groupId ->
                    if (groupId != null) pageRepository.getPagesForGroup(groupId)
                    else flowOf(emptyList())
                }

            combine(
                notebookFlow,
                sectionsFlow,
                groupsFlow,
                pagesFlow,
                _selection
            ) { notebook, sections, groups, pages, selection ->

                if (notebook == null) return@combine State.Loading

                val currentIdle = _stateFlow.value as? State.Idle

                val drawingToUse =
                    if (selection.pageId != null && selection.pageId != currentIdle?.selected?.pageId){
                        val page = pages.find { it.id == selection.pageId }
                        val paths = drawingHelper.parseJsonToPaths(page?.content ?: "")
                        DrawingState(paths = paths)
                    }
                    else currentIdle?.drawingState ?: DrawingState()

                State.Idle(
                    notebook = notebook,
                    sections = sections,
                    groups = groups,
                    pages = pages,
                    selected = selection,
                    activePopup = currentIdle?.activePopup,
                    drawingState = drawingToUse,
                    isTabOpen = currentIdle?.isTabOpen ?: true
                )
            }
                .catch { error -> _stateFlow.value = State.Error(error) }
                .collect { newState -> _stateFlow.value = newState }
        }
    }

    // ============================================================================================
    // NAVIGATION & SELECTION
    // ============================================================================================

    fun onSectionSelected(sectionId: Long) {
        _selection.update { current -> current.copy(sectionId = sectionId, groupId = null, pageId = null) }
    }

    fun onSectionLongClicked(sectionId: Long) {
        val currentState = _stateFlow.value
        if (currentState is State.Idle) {
            val section = currentState.sections.find { it.id == sectionId }
            if (section != null) {
                _stateFlow.update {
                    currentState.copy(
                        activePopup = ActivePopup.Edit(
                            id = section.id,
                            target = PopupTarget.SECTION,
                            title = section.title
                        )
                    )
                }
            }
        }
    }

    fun onAddSection() {
        _stateFlow.update {
            (it as? State.Idle)?.copy(activePopup = ActivePopup.Add(
                target = PopupTarget.SECTION
            ) ) ?: it
        }
    }

    fun onGroupSelected(groupId: Long) {
        _selection.update { current -> current.copy(groupId = groupId, pageId = null) }
    }

    fun onGroupLongClicked(groupId: Long) {
        val currentState = _stateFlow.value
        if (currentState is State.Idle) {
            val group = currentState.groups.find { it.id == groupId }
            if (group != null) {
                _stateFlow.update {
                    currentState.copy(
                        activePopup = ActivePopup.Edit(
                            id = group.id,
                            target = PopupTarget.GROUP,
                            title = group.title
                        )
                    )
                }
            }
        }
    }

    fun onAddGroup() {
        _stateFlow.update {
            (it as? State.Idle)?.copy(activePopup = ActivePopup.Add(
                target = PopupTarget.GROUP
            ) ) ?: it
        }
    }

    fun onPageSelected(pageId: Long) {
        _selection.update { current -> current.copy(pageId = pageId) }
    }

    fun toggleTab() {
        _stateFlow.update {
            (it as? State.Idle)
                ?.let { idleState -> idleState.copy(isTabOpen = !idleState.isTabOpen) }
                ?: it
        }
    }



    // ============================================================================================
    // POPUP MANAGEMENT
    // ============================================================================================

    private fun updatePopup(popup: ActivePopup?) {
        _stateFlow.update { state ->
            (state as? State.Idle)?.copy(activePopup = popup) ?: state
        }
    }

    fun onPopupNameChange(newName: String) {
        _stateFlow.update { state ->
            val idle = state as? State.Idle ?: return@update state

            val updatedPopup = when (val popup = idle.activePopup) {
                is ActivePopup.Add -> popup.copy(title = newName)
                is ActivePopup.Edit -> popup.copy(title = newName)
                else -> popup
            }

            idle.copy(activePopup = updatedPopup)
        }
    }

    fun onDismissPopup() {
        _stateFlow.update {
            (it as? State.Idle)?.copy(activePopup = null) ?: it
        }
    }



    // ============================================================================================
    // SECTION CRUD OPERATIONS
    // ============================================================================================

    fun createNewSection() {
        val currentState = _stateFlow.value
        if (currentState is State.Idle && currentState.activePopup is ActivePopup.Add) {
            val name = currentState.activePopup.title

            _stateFlow.update {
                currentState.copy(activePopup = null)
            }

            viewModelScope.launch {
                try {
                    sectionRepository.insertSection(notebookId, name)
                } catch (e: Throwable) {
                    _stateFlow.value = State.Error(e)
                }
            }
        }
    }

    fun updateSection() {
        val currentState = _stateFlow.value
        if (currentState is State.Idle && currentState.activePopup is ActivePopup.Edit) {
            val sectionId = currentState.activePopup.id
            val newName = currentState.activePopup.title

            _stateFlow.update {
                currentState.copy(activePopup = null)
            }

            val section = currentState.sections.find { it.id == sectionId }

            viewModelScope.launch {
                try {
                    if (section != null) {
                        val updatedSection = section.copy(title = newName)
                        sectionRepository.updateSection(updatedSection)
                    }
                } catch (e: Throwable) {
                    _stateFlow.value = State.Error(e)
                }
            }
        }
    }



    // ============================================================================================
    // GROUP CRUD OPERATIONS
    // ============================================================================================

    fun createNewGroup() {
        val currentState = _stateFlow.value
        if (currentState is State.Idle && currentState.activePopup is ActivePopup.Add) {
            val sectionId = currentState.selected.sectionId
            val title = currentState.activePopup.title

            if (sectionId != null) {
                _stateFlow.update {
                    currentState.copy(activePopup = null)
                }

                viewModelScope.launch {
                    try {
                        groupRepository.insertGroup(sectionId, title)
                    } catch (e: Throwable) {
                        _stateFlow.value = State.Error(e)
                    }
                }
            }
        }

    }

    fun updateGroup() {
        val currentState = _stateFlow.value
        if (currentState is State.Idle && currentState.activePopup is ActivePopup.Edit) {
            val groupId = currentState.activePopup.id
            val newTitle = currentState.activePopup.title

            _stateFlow.update {
                currentState.copy(activePopup = null)
            }

            val group = currentState.groups.find { it.id == groupId }

            viewModelScope.launch {
                try {
                    if (group != null) {
                        val updatedGroup = group.copy(title = newTitle)
                        groupRepository.updateGroup(updatedGroup)
                    }
                } catch (e: Throwable) {
                    _stateFlow.value = State.Error(e)
                }
            }
        }


    }



    // ============================================================================================
    // PAGE CRUD OPERATIONS
    // ============================================================================================

    fun createNewPage() {
        val currentState = _stateFlow.value
        if (currentState is State.Idle && currentState.selected.groupId != null) {
            val groupId = currentState.selected.groupId

            viewModelScope.launch {
                try {
                    pageRepository.insertPage(groupId)

                } catch (e: Throwable) {
                    _stateFlow.value = State.Error(e)
                }
            }
        }
    }



    // ============================================================================================
    // DRAWING & CANVAS
    // ============================================================================================

    fun onNewStroke(newPath: PathData) {
        _stateFlow.update {
            val idleState = it as? State.Idle ?: return@update it

            val oldDrawingState = idleState.drawingState
            val newDrawingState = oldDrawingState.copy(
                paths = oldDrawingState.paths + newPath
            )

            idleState.copy(drawingState = newDrawingState)
        }

        val currentState = _stateFlow.value
        if (currentState is State.Idle) {
            val page = currentState.pages.find { it.id == _selection.value.pageId }

            if (page != null) {
                drawingHelper.saveDrawingWithDebounce(
                    page = page,
                    paths = currentState.drawingState.paths
                )
            }
        }
    }
}