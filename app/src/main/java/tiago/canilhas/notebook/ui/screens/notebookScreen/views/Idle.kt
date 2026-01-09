package tiago.canilhas.notebook.ui.screens.notebookScreen.views

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tiago.canilhas.notebook.R
import tiago.canilhas.notebook.data.db.entity.Notebook
import tiago.canilhas.notebook.data.db.entity.Page
import tiago.canilhas.notebook.data.db.entity.Section
import tiago.canilhas.notebook.ui.components.NotebookPageSelectionTab
import tiago.canilhas.notebook.ui.components.Tab
import tiago.canilhas.notebook.ui.components.TopBar
import tiago.canilhas.notebook.ui.screens.notebookScreen.DrawingState
import tiago.canilhas.notebook.ui.screens.notebookScreen.PathData

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun IdleView(
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
    val tabWidth =
        if (currentSelectedSectionId == null) Tab.TAB_FULL_WIDTH.dp
        else (Tab.TAB_FULL_WIDTH * 2).dp

    val animationWidth by animateDpAsState(
        targetValue = if (isTabOpen) tabWidth else 0.dp,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "animation"
    )

    BackHandler(enabled = isTabOpen) { toggleTab() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                toggleTab = toggleTab
            )
        },
    ) { innerPadding ->
        Layout(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            content = {
                if (animationWidth > 0.dp)
                    Box(modifier = Modifier.layoutId("tab")) {
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
                            modifier = Modifier.fillMaxHeight()
                        )
                    }

                Box(modifier = Modifier.layoutId("canvas")) {
                    if (currentSelectedPageId != null) {
                        DrawingCanvas(
                            paths = drawingState.paths,
                            onPathEnd = onNewStroke,
                            currentPath = null,
                            onNewPathStart = {},
                            onPathUpdate = {},
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(R.string.select_page))
                        }
                    }
                }
            }
        ) { measurables, constraints ->
            val currentVisibleWidthPx = animationWidth.roundToPx()
            val tabWidthPx = tabWidth.roundToPx()

            val tabMeasurable = measurables.find { it.layoutId == "tab" }
            val tabPlaceable = tabMeasurable?.measure(
                constraints.copy(minWidth = tabWidthPx, maxWidth = tabWidthPx)
            )

            val canvasWidthPx = constraints.maxWidth - currentVisibleWidthPx
            val canvasMeasurable = measurables.first { it.layoutId == "canvas" }
            val canvasPlaceable = canvasMeasurable.measure(
                constraints.copy(minWidth = canvasWidthPx, maxWidth = canvasWidthPx)
            )

            layout(constraints.maxWidth, constraints.maxHeight) {
                if (tabPlaceable != null){
                    val tabXOffset = currentVisibleWidthPx - tabWidthPx
                    tabPlaceable.placeRelative(x = tabXOffset, y = 0)
                }
                canvasPlaceable.placeRelative(x = currentVisibleWidthPx, y = 0)
            }
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