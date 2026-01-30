package tiago.canilhas.notebook.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import tiago.canilhas.notebook.R
import tiago.canilhas.notebook.data.db.entity.Group
import tiago.canilhas.notebook.data.db.entity.Page
import tiago.canilhas.notebook.data.db.entity.Section

@Composable
fun NotebookPageSelectionTab(
    // Sections
    sections: List<Section>,
    currentSelectedSectionId: Long?,
    onSectionSelected: (Long) -> Unit,
    onSectionLongClicked: (Long) -> Unit,
    onAddSection: () -> Unit,

    // Groups
    groups: List<Group>,
    currentSelectedGroupId: Long?,
    onGroupSelected: (Long) -> Unit,
    onGroupLongClicked: (Long) -> Unit,
    onAddGroup: () -> Unit,

    // Pages
    pages: List<Page>,
    currentSelectedPageId: Long?,
    onPageSelected: (Long) -> Unit,
    onAddPage: () -> Unit,

    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
    ){
        ScrollableTab(
            options = sections.map { it.title },
            selectedIndex = sections.indexOfFirst { it.id == currentSelectedSectionId },
            onOptionSelected = { idx -> onSectionSelected(sections[idx].id) },
            onOptionLongClicked = { idx -> onSectionLongClicked(sections[idx].id) },
            onAddClicked = onAddSection
        )

        ScrollableTab(
            options = groups.map { it.title },
            selectedIndex = groups.indexOfFirst { it.id == currentSelectedGroupId },
            onOptionSelected = { idx -> onGroupSelected(groups[idx].id) },
            onOptionLongClicked = { idx -> onGroupLongClicked(groups[idx].id) },
            onAddClicked = onAddGroup,
            disabled = currentSelectedSectionId == null,
        )

        ScrollableTab(
            options = pages.map { it.title },
            selectedIndex = pages.indexOfFirst { it.id == currentSelectedPageId },
            onOptionSelected = { idx -> onPageSelected(pages[idx].id) },
            onAddClicked = onAddPage,
            disabled = currentSelectedGroupId == null,
        )
    }
}

@Preview
@Composable
fun NotebookPageSelectionTabPreview() {
    val sections = listOf(
        Section(id = 1, notebookId = 1, title = "Section 1", order = 1),
        Section(id = 2, notebookId = 1, title = "Section 2", order = 2),
        Section(id = 3, notebookId = 1, title = "Section 3", order = 3),
    )

    val groups = listOf(
        Group(id = 1, sectionId = 1, title = "Group 1", order = 1),
        Group(id = 2, sectionId = 1, title = "Group 2", order = 2),
    )

    val pages = listOf(
        Page(id = 1, groupId = 1, title = "Page 1", order = 1, content = "Content 1"),
        Page(id = 2, groupId = 1, title = "Page 2", order = 2, content = "Content 2"),
        Page(id = 3, groupId = 1, title = "Page 3", order = 3, content = "Content 3"),
    )

    NotebookPageSelectionTab(
        sections = sections,
        currentSelectedSectionId = 1,
        onSectionSelected = {},
        onSectionLongClicked = {},
        onAddSection = {},

        groups = groups,
        currentSelectedGroupId = 1,
        onGroupSelected = {},
        onGroupLongClicked = {},
        onAddGroup = {},

        pages = pages,
        currentSelectedPageId = 1,
        onPageSelected = {},
        onAddPage = {}
    )
}