package tiago.canilhas.notebook.ui.screens.notebookScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tiago.canilhas.notebook.data.db.entity.Notebook
import tiago.canilhas.notebook.data.db.entity.Page
import tiago.canilhas.notebook.data.db.entity.Section
import tiago.canilhas.notebook.data.repository.NotebookRepository

sealed interface State {
    object Loading : State
    data class Idle(
        val notebook: Notebook,
        val sections: List<Section>,
        val pages: List<Page>,

        val currentSelectedSectionId: Long?,
        val currentSelectedPageId: Long?,

        val activePopup: ActivePopup? = null,

        val drawingState: DrawingState = DrawingState(),

        val isTabOpen: Boolean = true
    ) : State
    data class Error(val exception: Throwable) : State
}

sealed interface ActivePopup {
    data class AddSection(val title: String = "") : ActivePopup
    data class EditSection(val id: Long, val title: String) : ActivePopup
}

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModel(
    private val repository: NotebookRepository
) : ViewModel() {
    private val _stateFlow = MutableStateFlow<State>(State.Loading)
    val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()

    private val _notebookId = MutableStateFlow<Long?>(null)
    private val _selectedSectionId = MutableStateFlow<Long?>(null)
    private val _selectedPageId = MutableStateFlow<Long?>(null)

    private val _dataFlow: Flow<Triple<Notebook, List<Section>, List<Page>>?> =
        _notebookId
            .flatMapLatest { notebookId ->
                if (notebookId == null) flowOf(null)
                else {
                    val notebookFlow = repository.getNotebookById(notebookId)
                    val sectionsFlow = repository.getSectionsForNotebook(notebookId)

                    combine(notebookFlow, sectionsFlow) { notebook, sections ->
                        if (_selectedSectionId.value == null)
                            _selectedSectionId.value = sections.firstOrNull()?.id

                        Triple(notebook, sections, _selectedSectionId)
                    }
                }
            }
            .flatMapLatest { triple ->
                if (triple !is Triple<*, *, *>) return@flatMapLatest flowOf(null)

                val (notebook, sections, selectedSectionFlow) =
                    triple as Triple<Notebook, List<Section>, StateFlow<Long?>>

                selectedSectionFlow.flatMapLatest { selectedId ->
                    val pagesFlow =
                        if (selectedId != null) repository.getPagesForSection(selectedId)
                        else flowOf(emptyList())

                    pagesFlow.map { pages ->
                        Triple(notebook, sections, pages)
                    }
                }
            }
            .catch { e -> _stateFlow.value = State.Error(e) }

    init {
        viewModelScope.launch {
            _dataFlow.collect { data ->
                if (data == null) _stateFlow.value = State.Loading
                else {
                    val (notebook, sections, pages) = data

                    _stateFlow.update {
                        val idleState = it as? State.Idle

                        val previousPageId = idleState?.currentSelectedPageId

                        val pageIdToUse =
                            if (pages.any { it.id == previousPageId }) previousPageId
                            else null


                        val drawingToUse =
                            if (pageIdToUse != null) idleState?.drawingState ?: DrawingState()
                            else DrawingState()

                        State.Idle(
                            notebook = notebook,
                            sections = sections,
                            pages = pages,

                            currentSelectedSectionId = _selectedSectionId.value,
                            currentSelectedPageId = pageIdToUse,

                            activePopup = idleState?.activePopup,

                            drawingState = drawingToUse,

                            isTabOpen = idleState?.isTabOpen ?: true,
                        )
                    }
                }
            }
        }
    }

    private val drawingHelper = DrawingHelper(viewModelScope, repository)

    fun loadNotebookData(notebookId: Long) {
        _notebookId.value = notebookId
    }


    fun toggleTab() {
        _stateFlow.update {
            (it as? State.Idle)
                ?.let { idleState -> idleState.copy(isTabOpen = !idleState.isTabOpen) }
                ?: it
        }
    }

    /**
     * Section
     */

    fun onSectionSelected(sectionId: Long) {
        _selectedSectionId.value = sectionId

        _stateFlow.update {
            (it as? State.Idle)
                ?.copy(
                    currentSelectedSectionId = sectionId,
                    currentSelectedPageId = null,
                    drawingState = DrawingState()
                )
                ?: it
        }
    }

    fun onSectionLongClicked(sectionId: Long) {
        val currentState = _stateFlow.value
        if (currentState is State.Idle) {
            val section = currentState.sections.find { it.id == sectionId }
            if (section != null) {
                _stateFlow.update {
                    currentState.copy(
                        activePopup = ActivePopup.EditSection(
                            id = section.id,
                            title = section.name
                        )
                    )
                }
            }
        }
    }

    fun onAddSection() {
        _stateFlow.update {
            (it as? State.Idle)?.copy(activePopup = ActivePopup.AddSection()) ?: it
        }
    }

    fun onPopupNameChange(newName: String) {
        _stateFlow.update { it ->
            (it as? State.Idle)?.let { idleState ->
                val updatedPopup =
                    when (val popup = idleState.activePopup) {
                        is ActivePopup.AddSection -> popup.copy(title = newName)
                        is ActivePopup.EditSection -> popup.copy(title = newName)
                        else -> return@let idleState
                    }

                idleState.copy(activePopup = updatedPopup)
            } ?: it
        }
    }

    fun onDismissPopup() {
        _stateFlow.update {
            (it as? State.Idle)?.copy(activePopup = null) ?: it
        }
    }

    fun createNewSection() {
        val currentState = _stateFlow.value
        val notebookId = _notebookId.value
        if (
            currentState is State.Idle
            && notebookId != null
            && currentState.activePopup is ActivePopup.AddSection
            ) {

            val name = currentState.activePopup.title

            _stateFlow.update {
                currentState.copy(activePopup = null)
            }

            viewModelScope.launch {
                try {
                    val newSection = Section(notebookId = notebookId, name = name)
                    repository.insertSection(newSection)
                } catch (e: Throwable) {
                    _stateFlow.value = State.Error(e)
                }
            }
        }
    }

    fun updateSection() {
        val currentState = _stateFlow.value
        if (
            currentState is State.Idle
            && currentState.activePopup is ActivePopup.EditSection
        ) {
            val sectionId = currentState.activePopup.id
            val newName = currentState.activePopup.title

            _stateFlow.update {
                currentState.copy(activePopup = null)
            }

            val section = currentState.sections.find { it.id == sectionId }

            viewModelScope.launch {
                try {
                    if (section != null) {
                        val updatedSection = section.copy(name = newName)
                        repository.updateSection(updatedSection)
                    }
                } catch (e: Throwable) {
                    _stateFlow.value = State.Error(e)
                }
            }
        }
    }



    /**
     * Page
     */

    fun onPageSelected(pageId: Long) {
        _selectedPageId.value = pageId

        val currentState = _stateFlow.value
        if (currentState is State.Idle) {
            val page = currentState.pages.find { it.id == pageId }
            val paths = drawingHelper.parseJsonToPaths(page?.content ?: "")
            _stateFlow.update {
                (it as? State.Idle)
                    ?.copy(
                        drawingState = DrawingState(paths = paths),
                        currentSelectedPageId = pageId
                    )
                    ?: it
            }
        }
    }

    fun createNewPage() {
        val currentState = _stateFlow.value
        if (currentState is State.Idle && currentState.currentSelectedSectionId != null) {
            val sectionId = currentState.currentSelectedSectionId

            viewModelScope.launch {
                try {
                    val newPage = Page.create(sectionId = sectionId)
                    repository.insertPage(newPage)

                } catch (e: Throwable) {
                    _stateFlow.value = State.Error(e)
                }
            }
        }
    }

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
            val page = currentState.pages.find { it.id == _selectedPageId.value }

            if (page != null) {
                drawingHelper.saveDrawingWithDebounce(
                    page = page,
                    paths = currentState.drawingState.paths
                )
            }
        }
    }
}