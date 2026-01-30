package tiago.canilhas.notebook.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import tiago.canilhas.notebook.data.repository.GroupRepository
import tiago.canilhas.notebook.data.repository.NotebookRepository
import tiago.canilhas.notebook.data.repository.PageRepository
import tiago.canilhas.notebook.data.repository.SectionRepository
import tiago.canilhas.notebook.ui.screens.mainScreen.ViewModel as MainViewModel
import tiago.canilhas.notebook.ui.screens.notebookScreen.ViewModel as NotebookViewModel

@Suppress("UNCHECKED_CAST")
class AppViewModelFactory(
    private val notebookRepository: NotebookRepository,
    private val sectionsRepository: SectionRepository,
    private val groupRepository: GroupRepository,
    private val pagesRepository: PageRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(notebookRepository) as T
            }

            modelClass.isAssignableFrom(NotebookViewModel::class.java) -> {
                NotebookViewModel(
                    extras.createSavedStateHandle(),
                    notebookRepository,
                    sectionsRepository,
                    groupRepository,
                    pagesRepository
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}