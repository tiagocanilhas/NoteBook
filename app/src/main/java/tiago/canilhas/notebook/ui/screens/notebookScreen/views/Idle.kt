package tiago.canilhas.notebook.ui.screens.notebookScreen.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tiago.canilhas.notebook.data.db.entity.Notebook
import tiago.canilhas.notebook.data.db.entity.Page
import tiago.canilhas.notebook.data.db.entity.Section
import tiago.canilhas.notebook.ui.components.NotebookPageSelectionTab
import tiago.canilhas.notebook.ui.components.TopBar

@Composable
fun IdleView (
    notebook: Notebook,
    sections: List<Section>,
    pages: List<Page>,
    currentSelectedSectionId: Long?,
    currentSelectedPageId: Long?,

    onSectionSelected: (Long) -> Unit,
    onAddSection: () -> Unit,
    onPageSelected: (Long) -> Unit,
    onAddPage: () -> Unit,
    onBackClicked: () -> Unit,

    isTabOpen: Boolean,
    toggleTab: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                toggleTab = toggleTab
            )
        },
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (isTabOpen)
                NotebookPageSelectionTab(
                    sections = sections,
                    pages = pages,
                    currentSelectedSectionId = currentSelectedSectionId,
                    currentSelectedPageId = currentSelectedPageId,
                    onSectionSelected = onSectionSelected,
                    onAddSection = onAddSection,
                    onPageSelected = onPageSelected,
                    onAddPage = onAddPage,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
        }
    }
}

@Preview
@Composable
fun IdleViewPreview() {
    IdleView(
        notebook = Notebook(id = 1, name = "My Notebook", colorHex = "#FFFFFF"),
        sections = listOf(
            Section(id = 1, notebookId = 1, name = "Section 1"),
            Section(id = 2, notebookId = 1, name = "Section 2"),
            Section(id = 3, notebookId = 1, name = "Section 3")
        ),
        pages = listOf(
            Page(id = 1, sectionId = 1, title = "Page 1", content = ""),
            Page(id = 2, sectionId = 1, title = "Page 2", content = ""),
        ),
        currentSelectedSectionId = 1,
        currentSelectedPageId = 1,
        onSectionSelected = {},
        onAddSection = {},
        onPageSelected = {},
        onAddPage = {},
        onBackClicked = {},
        isTabOpen = true,
        toggleTab = {}
    )
}