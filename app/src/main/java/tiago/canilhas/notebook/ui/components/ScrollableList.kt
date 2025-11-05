package tiago.canilhas.notebook.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun <T> ScrollableList(
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
    modifier: Modifier = Modifier,
    onAdd: (() -> Unit)? = null
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(250.dp),
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        items(items) { item ->
            itemContent(item)
        }

        onAdd?.let {
            item {
                Card(
                    cardText = "+",
                    onClick = { onAdd() },
                )
            }
        }
    }
}

@Preview
@Composable
fun ScrollableListPreview() {
    ScrollableList(
        items = List(3) { "Item $it" },
        itemContent = { item ->
            Card(
                cardText = item,
                onClick = {}
            )
        },
        onAdd = {}
    )
}
