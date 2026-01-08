package tiago.canilhas.notebook.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import tiago.canilhas.notebook.R
import tiago.canilhas.notebook.data.db.entity.Page
import tiago.canilhas.notebook.data.db.entity.Section

@Composable
fun NotebookPageSelectionTab(
    sections: List<Section>,
    pages: List<Page>,
    currentSelectedSectionId: Long?,
    currentSelectedPageId: Long?,
    onSectionSelected: (Long) -> Unit,
    onSectionLongClicked: (Long) -> Unit,
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
            backgroundColor = colorResource(R.color.section_tab_background),
            optionSelectedColor = colorResource(R.color.section_tab_option_selected),
            optionUnselectedColor = colorResource(R.color.section_tab_option_unselected),
            addColor = colorResource(R.color.section_tab_add),
            onOptionSelected = { idx -> onSectionSelected(sections[idx].id) },
            onOptionLongClicked = { idx -> onSectionLongClicked(sections[idx].id) },
            onAddClicked = onAddSection
        )

        if (currentSelectedSectionId != null)
            ScrollableTab(
                options = pages.map { it.title },
                selectedIndex = pages.indexOfFirst { it.id == currentSelectedPageId },
                backgroundColor = colorResource(R.color.page_tab_background),
                optionSelectedColor = colorResource(R.color.page_tab_option_selected),
                optionUnselectedColor = colorResource(R.color.page_tab_option_unselected),
                addColor = colorResource(R.color.page_tab_add),
                onOptionSelected = { idx -> onPageSelected(pages[idx].id) },
                onOptionLongClicked = { _ -> },
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
        onSectionLongClicked = {},
        onPageSelected = {},
        onAddPage = {}
    )
}