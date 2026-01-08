package tiago.canilhas.notebook.ui.screens.notebookScreen

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.util.UUID

data class PathData(
    val id: String = UUID.randomUUID().toString(),
    val strokeWidth: Float,
    private val safeColor: Int,
    private val safePoints: List<Pair<Float, Float>>
) {
    val color: Color
        get() = Color(safeColor)

    val path: List<Offset>
        get() = safePoints.map { Offset(it.first, it.second) }
}

fun createPathData(offsets: List<Offset>, color: Color, width: Float): PathData {
    return PathData(
        safePoints = offsets.map { Pair(it.x, it.y) },
        safeColor = color.toArgb(),
        strokeWidth = width
    )
}

data class DrawingState(
    val selectedColor: Color = Color.Black,
    val paths: List<PathData> = emptyList()
)

