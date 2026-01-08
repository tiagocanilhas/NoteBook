package tiago.canilhas.notebook.ui.screens.notebookScreen.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tiago.canilhas.notebook.R
import tiago.canilhas.notebook.data.db.entity.Notebook
import tiago.canilhas.notebook.data.db.entity.Page
import tiago.canilhas.notebook.data.db.entity.Section
import tiago.canilhas.notebook.ui.components.NotebookPageSelectionTab
import tiago.canilhas.notebook.ui.components.TopBar
import tiago.canilhas.notebook.ui.screens.notebookScreen.DrawingState
import tiago.canilhas.notebook.ui.screens.notebookScreen.PathData

@Composable
fun IdleView (
    notebook: Notebook,
    sections: List<Section>,
    pages: List<Page>,
    currentSelectedSectionId: Long?,
    currentSelectedPageId: Long?,

    onSectionSelected: (Long) -> Unit,
    onSectionLongClicked: (Long) -> Unit,
    onAddSection: () -> Unit,
    onPageSelected: (Long) -> Unit,
    onAddPage: () -> Unit,
    onBackClicked: () -> Unit,

    drawingState: DrawingState,
    onNewStroke: (PathData) -> Unit,

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
            AnimatedVisibility(
                visible = isTabOpen,
                enter = DrawerTransition.enterTransition,
                exit = DrawerTransition.exitTransition
            ) {
                NotebookPageSelectionTab(
                    sections = sections,
                    pages = pages,
                    currentSelectedSectionId = currentSelectedSectionId,
                    currentSelectedPageId = currentSelectedPageId,
                    onSectionSelected = onSectionSelected,
                    onSectionLongClicked = onSectionLongClicked,
                    onAddSection = onAddSection,
                    onPageSelected = onPageSelected,
                    onAddPage = onAddPage,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }

            if (currentSelectedPageId != null) {
                DrawingCanvas(
                    paths = drawingState.paths,
                    onPathEnd = onNewStroke,
                    currentPath = null,
                    onNewPathStart = {},
                    onPathUpdate = {},
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxHeight()
                )
            }
            else {
                Box(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxHeight()
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.select_page))
                }
            }
        }
    }
}

object DrawerTransition {
    private const val ANIM_DURATION = 300

    val enterTransition: EnterTransition =
        slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(durationMillis = 300)
        )

    val exitTransition: ExitTransition =
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(ANIM_DURATION)
        )
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
        onSectionLongClicked = {},
        onAddSection = {},
        onPageSelected = {},
        onAddPage = {},
        onBackClicked = {},
        drawingState = DrawingState(),
        onNewStroke = {},
        isTabOpen = true,
        toggleTab = {}
    )
}