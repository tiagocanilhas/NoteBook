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

        val isAddSectionPopupShowing: Boolean = false,
        val addSectionPopupTextFieldValue: String = "",

        val currentStrokes: List<Any> = emptyList(),

        val isTabOpen: Boolean = true
    ) : State
    data class Error(val exception: Throwable) : State
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
                        State.Idle(
                            notebook = notebook,
                            sections = sections,
                            pages = pages,
                            currentSelectedSectionId = _selectedSectionId.value,
                            currentSelectedPageId = _selectedPageId.value,
                            isAddSectionPopupShowing = idleState?.isAddSectionPopupShowing ?: false,
                            addSectionPopupTextFieldValue = idleState?.addSectionPopupTextFieldValue ?: "",
                            isTabOpen = idleState?.isTabOpen ?: true,
                            currentStrokes = idleState?.currentStrokes ?: emptyList(),
                        )
                    }
                }
            }
        }
    }

    fun loadNotebookData(notebookId: Long) {
        _notebookId.value = notebookId
    }

    /**
     * Section
     */

    fun onSectionSelected(sectionId: Long) {
        _selectedSectionId.value = sectionId
    }

    fun onAddSection() {
        _stateFlow.update {
            (it as? State.Idle)?.copy(isAddSectionPopupShowing = true) ?: it
        }
    }

    fun onAddSectionPopupNameChange(newName: String) {
        _stateFlow.update {
            (it as? State.Idle)?.copy(addSectionPopupTextFieldValue = newName) ?: it
        }
    }

    fun onDismissAddSectionPopup() {
        _stateFlow.update {
            (it as? State.Idle)
                ?.copy(isAddSectionPopupShowing = false, addSectionPopupTextFieldValue = "")
                ?: it
        }
    }

    fun createNewSection() {
        val currentState = _stateFlow.value
        val notebookId = _notebookId.value
        if (currentState is State.Idle && notebookId != null) {
            val name = currentState.addSectionPopupTextFieldValue

            _stateFlow.update {
                currentState.copy(
                    isAddSectionPopupShowing = false,
                    addSectionPopupTextFieldValue = ""
                )
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



    /**
     * Page
     */

    fun onPageSelected(pageId: Long) {
        _selectedPageId.value = pageId
    }

    fun createNewPage() {
        val currentState = _stateFlow.value
        if (currentState is State.Idle && currentState.currentSelectedSectionId != null) {
            val sectionId = currentState.currentSelectedSectionId

            viewModelScope.launch {
                try {
                    val newPage = Page(
                        sectionId = sectionId,
                        title = "",
                        content = ""
                    )
                    repository.insertPage(newPage)

                } catch (e: Throwable) {
                    _stateFlow.value = State.Error(e)
                }
            }
        }
    }

    fun toggleTab() {
        _stateFlow.update {
            (it as? State.Idle)
                ?.let { idleState -> idleState.copy(isTabOpen = !idleState.isTabOpen) }
                ?: it
        }
    }
}