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
                currentSelectedSectionId = state.selected.sectionId,
                onSectionSelected = viewModel::onSectionSelected,
                onSectionLongClicked = viewModel::onSectionLongClicked,
                onAddSection = viewModel::onAddSection,

                groups = state.groups,
                currentSelectedGroupId = state.selected.groupId,
                onGroupSelected = viewModel::onGroupSelected,
                onGroupLongClicked = viewModel::onGroupLongClicked,
                onAddGroup = viewModel::onAddGroup,

                pages = state.pages,
                currentSelectedPageId = state.selected.pageId,
                onPageSelected = viewModel::onPageSelected,
                onAddPage = viewModel::createNewPage,
                onPageNameChange = viewModel::updatePageName,

                onBackClicked = onBackClicked,

                drawingState = state.drawingState,
                onNewStroke = viewModel::onNewStroke,

                isTabOpen = state.isTabOpen,
                toggleTab = viewModel::toggleTab
            )

            if (state.activePopup != null) {

                val placeholder = when (state.activePopup.target) {
                    PopupTarget.SECTION -> stringResource(id = R.string.enter_section_name)
                    PopupTarget.GROUP -> stringResource(id = R.string.enter_group_name)
                }

                val onAcceptAction = when (state.activePopup.target) {
                    PopupTarget.SECTION -> when (state.activePopup) {
                        is ActivePopup.Add -> viewModel::createNewSection
                        is ActivePopup.Edit -> viewModel::updateSection
                    }
                    PopupTarget.GROUP -> when (state.activePopup) {
                        is ActivePopup.Add -> viewModel::createNewGroup
                        is ActivePopup.Edit -> viewModel::updateGroup
                    }
                }

                val onAcceptText = when (state.activePopup) {
                    is ActivePopup.Add -> stringResource(id = R.string.create)
                    is ActivePopup.Edit -> stringResource(id = R.string.save)
                }

                Popup(
                    placeholder = placeholder,
                    value = state.activePopup.title,
                    onValueChange = viewModel::onPopupNameChange,
                    onAccept = onAcceptAction,
                    onAcceptText = onAcceptText,
                    onDismiss = viewModel::onDismissPopup
                )
            }
        }

        is State.Error -> {}
    }
}
