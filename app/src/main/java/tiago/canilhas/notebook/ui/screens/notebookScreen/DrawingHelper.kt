package tiago.canilhas.notebook.ui.screens.notebookScreen

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tiago.canilhas.notebook.data.db.entity.Page
import tiago.canilhas.notebook.data.repository.NotebookRepository

class DrawingHelper(
    private val scope: CoroutineScope,
    private val repository: NotebookRepository
) {
    private val gson = Gson()
    private var saveJob: Job? = null

    // Convert from JSON String to List<PathData>
    fun parseJsonToPaths(json: String): List<PathData> {
        if (json.isBlank()) return emptyList()

        return try {
            val type = object : TypeToken<List<PathData>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    // Convert from List<PathData> to JSON String and save with debounce
    fun saveDrawingWithDebounce(page: Page, paths: List<PathData>) {
        saveJob?.cancel()

        saveJob = scope.launch {
            delay(1000L)

            val jsonString = gson.toJson(paths)

            val updatedPage = page.copy(content = jsonString)
            repository.updatePage(updatedPage)
        }
    }
}