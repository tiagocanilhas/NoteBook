package tiago.canilhas.notebook.ui.screens.notebookScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import tiago.canilhas.notebook.R
import tiago.canilhas.notebook.ui.components.Popup
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
                onSectionLongClicked = viewModel::onSectionLongClicked,
                onAddSection = viewModel::onAddSection,
                onPageSelected = viewModel::onPageSelected,
                onAddPage = viewModel::createNewPage,
                onBackClicked = onBackClicked,
                isTabOpen = state.isTabOpen,
                drawingState = state.drawingState,
                onNewStroke = viewModel::onNewStroke,
                toggleTab = viewModel::toggleTab
            )

            if (state.activePopup != null) {
                when (state.activePopup) {
                    is ActivePopup.AddSection -> Popup(
                        placeholder = stringResource(id = R.string.enter_section_name),
                        value = state.activePopup.title,
                        onValueChange = viewModel::onPopupNameChange,
                        onAccept = viewModel::createNewSection,
                        onAcceptText = stringResource(id = R.string.create),
                        onDismiss = viewModel::onDismissPopup
                    )

                    is ActivePopup.EditSection -> Popup(
                        placeholder = stringResource(id = R.string.enter_section_name),
                        value = state.activePopup.title,
                        onValueChange = viewModel::onPopupNameChange,
                        onAccept = viewModel::updateSection,
                        onAcceptText = stringResource(id = R.string.save),
                        onDismiss = viewModel::onDismissPopup
                    )
                }

            }
        }

        is State.Error -> {}
    }
}
