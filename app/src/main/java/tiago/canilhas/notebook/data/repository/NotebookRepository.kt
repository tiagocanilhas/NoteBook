package tiago.canilhas.notebook.data.repository

import tiago.canilhas.notebook.data.db.dao.NotebookDao
import tiago.canilhas.notebook.data.db.entity.Notebook

class NotebookRepository(
    private val notebookDao: NotebookDao,
) {
    fun getAllNotebooks() = notebookDao.getAllNotebooks()

    fun getNotebookById(id: Long) = notebookDao.getNotebookById(id)

    suspend fun insertNotebook(notebook: Notebook) {
        notebookDao.insert(notebook)

        // TODO: Call API to make sync with Cloud in the future
    }

    suspend fun updateNotebook(notebook: Notebook) {
        notebookDao.update(notebook)

        // TODO: Call API to make sync with Cloud in the future
    }

    suspend fun deleteNotebook(notebook: Notebook) {
        notebookDao.delete(notebook)

        // TODO: Call API to make sync with Cloud in the future
    }
}