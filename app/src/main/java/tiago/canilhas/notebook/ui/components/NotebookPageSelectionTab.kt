package tiago.canilhas.notebook.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tiago.canilhas.notebook.data.db.entity.Page
import tiago.canilhas.notebook.data.db.entity.Section

@Composable
fun NotebookPageSelectionTab(
    sections: List<Section>,
    pages: List<Page>,
    currentSelectedSectionId: Long?,
    currentSelectedPageId: Long?,
    onSectionSelected: (Long) -> Unit,
    onAddSection: () -> Unit,
    onPageSelected: (Long) -> Unit,
    onAddPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
    ){
        ScrollableTab(
            options = sections.map { it.name },
            selectedIndex = sections.indexOfFirst { it.id == currentSelectedSectionId },
            onOptionSelected = { idx -> onSectionSelected(sections[idx].id) },
            onAddClicked = onAddSection
        )

        if (currentSelectedSectionId != null)
            ScrollableTab(
                options = pages.map { it.title },
                selectedIndex = pages.indexOfFirst { it.id == currentSelectedPageId },
                onOptionSelected = { idx -> onPageSelected(pages[idx].id) },
                onAddClicked = onAddPage
            )
    }
}

@Preview
@Composable
fun NotebookPageSelectionTabPreview() {
    val sections = listOf(
        Section(id = 1, notebookId = 1, name = "Section 1"),
        Section(id = 2, notebookId = 1, name = "Section 2"),
        Section(id = 3, notebookId = 1, name = "Section 3"),
    )

    val pages = listOf(
        Page(id = 1, sectionId = 1, title = "Page 1", content = "Content 1"),
        Page(id = 2, sectionId = 1, title = "Page 2", content = "Content 2"),
        Page(id = 3, sectionId = 1, title = "Page 3", content = "Content 3"),
    )

    NotebookPageSelectionTab(
        sections = sections,
        pages = pages,
        currentSelectedSectionId = 1,
        currentSelectedPageId = 2,
        onSectionSelected = {},
        onAddSection = {},
        onPageSelected = {},
        onAddPage = {}
    )
}