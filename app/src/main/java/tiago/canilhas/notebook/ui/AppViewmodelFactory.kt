package tiago.canilhas.notebook.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tiago.canilhas.notebook.data.repository.NotebookRepository
import tiago.canilhas.notebook.ui.screens.mainScreen.ViewModel as MainViewModel
import tiago.canilhas.notebook.ui.screens.notebookScreen.ViewModel as NotebookViewModel

@Suppress("UNCHECKED_CAST")
class AppViewModelFactory(
    private val repository: NotebookRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }

            modelClass.isAssignableFrom(NotebookViewModel::class.java) -> {
                NotebookViewModel(repository) as T
            }
            // Add other ViewModels here

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}