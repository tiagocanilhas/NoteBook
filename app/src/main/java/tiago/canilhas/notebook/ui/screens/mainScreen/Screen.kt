package tiago.canilhas.notebook.ui.screens.mainScreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import tiago.canilhas.notebook.R
import tiago.canilhas.notebook.ui.components.CreatePopup
import tiago.canilhas.notebook.ui.screens.mainScreen.views.IdleView
import tiago.canilhas.notebook.ui.screens.mainScreen.views.LoadingView


@Composable
fun Screen(
    viewModel: ViewModel,
    onNotebookClicked: (id: Long) -> Unit
) {
    val currentState by viewModel.stateFlow.collectAsState()

    when (val state = currentState) {
        is State.Loading -> LoadingView()

        is State.Idle -> {
            IdleView(
                books = state.notebooks,
                onAddBook = viewModel::onAddBookClicked,
                onBookClick = { book -> onNotebookClicked(book.id) }
            )

            if (state.isPopupShowing) {
                CreatePopup(
                    placeholder = stringResource(id = R.string.enter_book_name),
                    value = state.popupTextFieldValue,
                    onValueChange = viewModel::onPopupNameChange,
                    onAccept = viewModel::createNewNotebook,
                    onDismiss = viewModel::onDismissAddBookPopup
                )
            }
        }

        is State.Error -> {
            Text("An error occurred: ${state.exception.localizedMessage}")
        }
    }
}