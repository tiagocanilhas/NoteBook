package tiago.canilhas.notebook.ui.screens.mainScreen.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tiago.canilhas.notebook.data.db.entity.Notebook
import tiago.canilhas.notebook.ui.components.Card
import tiago.canilhas.notebook.ui.components.ScrollableList

@Composable
fun IdleView (
    books: List<Notebook>,
    onAddBook: () -> Unit,
    onBookClick: (Notebook) -> Unit,
) {
    Scaffold { innerpadding ->
        ScrollableList (
            items = books,
            itemContent = { book -> Card(
                underCardText = book.name,
                image = null,
                onClick = { onBookClick(book) }
            ) },
            onAdd = onAddBook,
            modifier = Modifier.padding(innerpadding)
        )
    }
}

@Preview
@Composable
fun IdleViewPreview() {
    val books = listOf(
        Notebook(id = 1, name = "Notebook 1", colorHex = "#FF0000", createdAt = 0),
        Notebook(id = 2, name = "Notebook 2", colorHex = "#00FF00", createdAt = 0),
    )

    IdleView(
        books = books,
        onAddBook = {},
        onBookClick = {}
    )
}