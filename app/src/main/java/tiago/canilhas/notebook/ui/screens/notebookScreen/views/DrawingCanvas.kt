package tiago.canilhas.notebook.ui.screens.notebookScreen.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import tiago.canilhas.notebook.ui.screens.notebookScreen.PathData
import tiago.canilhas.notebook.ui.screens.notebookScreen.createPathData

@Composable
fun DrawingCanvas(
    paths: List<PathData>, // Os traços antigos (da BD)
    currentPath: PathData?, // (Opcional: se quiser passar o traço atual do VM)

    onNewPathStart: (Offset) -> Unit, // (Opcional: para lógica avançada)
    onPathUpdate: (Offset) -> Unit,   // (Opcional)
    onPathEnd: (PathData) -> Unit,    // O MAIS IMPORTANTE: Envia o traço final

    modifier: Modifier = Modifier
) {
    val tempPathPoints = remember { mutableStateListOf<Offset>() }

    Canvas(
        modifier = modifier
            .background(Color.White)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        tempPathPoints.clear()
                        tempPathPoints.add(offset)
                        // onNewPathStart(offset)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        tempPathPoints.add(change.position)
                        // onPathUpdate(change.position)
                    },
                    onDragEnd = {
                        if (tempPathPoints.isNotEmpty()) {

                            val newPath = createPathData(
                                offsets = tempPathPoints.toList(),
                                color = Color.Black,
                                width = 5f
                            )

                            onPathEnd(newPath)
                            tempPathPoints.clear()
                        }
                    }
                )
            }
    ) {
        // Old paths from the database
        paths.forEach { pathData ->
            drawPathData(pathData)
        }

        // Current temporary path being drawn
        if (tempPathPoints.isNotEmpty()) {
            val path = Path()

            path.moveTo(tempPathPoints.first().x, tempPathPoints.first().y)

            for (i in 1 until tempPathPoints.size) {
                path.lineTo(tempPathPoints[i].x, tempPathPoints[i].y)
            }

            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(
                    width = 5f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}

fun DrawScope.drawPathData(data: PathData) {
    val path = Path()
    if (data.path.isNotEmpty()) {

        path.moveTo(data.path.first().x, data.path.first().y)

        for (i in 1 until data.path.size) {
            path.lineTo(data.path[i].x, data.path[i].y)
        }

        drawPath(
            path = path,
            color = data.color,
            style = Stroke(
                width = data.strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Preview
@Composable
fun DrawingCanvasPreview() {
    DrawingCanvas(
        paths = listOf(
            createPathData(
                offsets = listOf(
                    Offset(50f, 50f),
                    Offset(150f, 150f),
                    Offset(250f, 100f)
                ),
                color = Color.Red,
                width = 8f
            ),
            createPathData(
                offsets = listOf(
                    Offset(100f, 200f),
                    Offset(200f, 250f),
                    Offset(300f, 200f)
                ),
                color = Color.Blue,
                width = 5f
            )
        ),
        currentPath = null,
        onNewPathStart = {},
        onPathUpdate = {},
        onPathEnd = {}
    )
}