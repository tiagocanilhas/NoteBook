package tiago.canilhas.notebook.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tiago.canilhas.notebook.data.db.entity.Notebook
import tiago.canilhas.notebook.data.repository.NotebookRepository

sealed interface State {
    object Loading : State
    data class Idle(
        val notebooks: List<Notebook>,
        val isPopupShowing: Boolean = false,
        val popupTextFieldValue: String = ""
    ) : State
    data class Error(val exception: Throwable) : State
}

class ViewModel(
    private val repository: NotebookRepository
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllNotebooks()
                .catch { e -> _stateFlow.value = State.Error(e) }
                .collect { notebooks ->
                    delay(2000L)
                    _stateFlow.update { currentState ->
                        val isPopupShowing = (currentState as? State.Idle)
                            ?.isPopupShowing ?: false
                        State.Idle(notebooks, isPopupShowing)
                    }
                }
        }
    }

    fun onAddBookClicked() {
        _stateFlow.update {
            (it as? State.Idle)?.copy(isPopupShowing = true) ?: it
        }
    }

    fun onPopupNameChange(newName: String) {
        _stateFlow.update {
            (it as? State.Idle)?.copy(popupTextFieldValue = newName) ?: it
        }
    }

    fun onDismissAddBookPopup() {
        _stateFlow.update {
            (it as? State.Idle)?.copy(isPopupShowing = false, popupTextFieldValue = "") ?: it
        }
    }

    fun createNewNotebook() {
        val currentState = _stateFlow.value
        if (currentState is State.Idle) {
            val name = currentState.popupTextFieldValue
            val colorHex = "#FFC107"

            _stateFlow.update {
                currentState.copy(
                    isPopupShowing = false,
                    popupTextFieldValue = ""
                )
            }

            viewModelScope.launch {
                try {
                    val newNotebook = Notebook(name = name, colorHex = colorHex)
                    repository.insertNotebook(newNotebook)
                } catch (e: Throwable) {
                    _stateFlow.value = State.Error(e)
                }
            }
        }
    }
}