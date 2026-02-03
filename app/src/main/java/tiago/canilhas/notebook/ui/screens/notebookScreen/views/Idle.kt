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
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tiago.canilhas.notebook.R
import tiago.canilhas.notebook.data.db.entity.Group
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
    currentSelectedSectionId: Long?,
    onSectionSelected: (Long) -> Unit,
    onSectionLongClicked: (Long) -> Unit,
    onAddSection: () -> Unit,

    groups: List<Group>,
    currentSelectedGroupId: Long?,
    onGroupSelected: (Long) -> Unit,
    onGroupLongClicked: (Long) -> Unit,
    onAddGroup: () -> Unit,

    pages: List<Page>,
    currentSelectedPageId: Long?,
    onPageSelected: (Long) -> Unit,
    onAddPage: () -> Unit,
    onPageNameChange: (String) -> Unit,

    onBackClicked: () -> Unit,

    drawingState: DrawingState,
    onNewStroke: (PathData) -> Unit,

    isTabOpen: Boolean,
    toggleTab: () -> Unit
) {
    val tabWidth = (Tab.TAB_FULL_WIDTH * 3).dp

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
                            currentSelectedSectionId = currentSelectedSectionId,
                            onSectionSelected = onSectionSelected,
                            onSectionLongClicked = onSectionLongClicked,
                            onAddSection = onAddSection,

                            groups = groups,
                            currentSelectedGroupId = currentSelectedGroupId,
                            onGroupSelected = onGroupSelected,
                            onGroupLongClicked = onGroupLongClicked,
                            onAddGroup = onAddGroup,

                            pages = pages,
                            currentSelectedPageId = currentSelectedPageId,
                            onPageSelected = onPageSelected,
                            onAddPage = onAddPage,

                            modifier = Modifier.fillMaxHeight()
                        )
                    }

                Box(modifier = Modifier.layoutId("canvas")) {
                    if (currentSelectedPageId != null) {
                        key(currentSelectedPageId) {
                            DrawingCanvas(
                                title = drawingState.pageTitle,
                                onTitleChange = onPageNameChange,
                                paths = drawingState.paths,
                                onPathEnd = onNewStroke,
                                currentPath = null,
                                onNewPathStart = {},
                                onPathUpdate = {},
                                modifier = Modifier.fillMaxSize()
                            )
                        }
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

                    if (isTabOpen) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f))
                                .pointerInput(Unit) {
                                    awaitPointerEventScope {
                                        while (true) {
                                            val touch = awaitPointerEvent().changes.first().changedToUp()
                                            if (touch) toggleTab()
                                        }
                                    }
                                }
                        )
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


    IdleView(
        notebook = Notebook(id = 1, name = "My Notebook", colorHex = "#FFFFFF"),

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
        onAddPage = {},
        onPageNameChange = {},

        onBackClicked = {},

        drawingState = DrawingState(paths = emptyList()),
        onNewStroke = {},

        isTabOpen = true,
        toggleTab = {}
    )
}