package tiago.canilhas.notebook.ui.screens.notebookScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import tiago.canilhas.notebook.R
import tiago.canilhas.notebook.ui.components.CreatePopup
import tiago.canilhas.notebook.ui.screens.notebookScreen.views.IdleView
import tiago.canilhas.notebook.ui.screens.notebookScreen.views.LoadingView

@Composable
fun Screen(
    viewModel: ViewModel,
    onBackClicked: () -> Unit
) {
    val currentState by viewModel.stateFlow.collectAsState()

    when (val state = currentState) {
        is State.Loading -> LoadingView()

        is State.Idle -> {
            IdleView(
                notebook = state.notebook,
                sections = state.sections,
                pages = state.pages,
                currentSelectedSectionId = state.currentSelectedSectionId,
                currentSelectedPageId = state.currentSelectedPageId,
                onSectionSelected = viewModel::onSectionSelected,
                onAddSection = viewModel::onAddSection,
                onPageSelected = viewModel::onPageSelected,
                onAddPage = viewModel::createNewPage,
                onBackClicked = onBackClicked,
                isTabOpen = state.isTabOpen,
                toggleTab = viewModel::toggleTab
            )

            if (state.isAddSectionPopupShowing){
                CreatePopup(
                    placeholder = stringResource(id = R.string.enter_section_name),
                    value = state.addSectionPopupTextFieldValue,
                    onValueChange = viewModel::onPopupNameChange,
                    onAccept = viewModel::createNewSection,
                    onDismiss = viewModel::onDismissAddSectionPopup
                )
            }
        }

        is State.Error -> {}
    }
}
