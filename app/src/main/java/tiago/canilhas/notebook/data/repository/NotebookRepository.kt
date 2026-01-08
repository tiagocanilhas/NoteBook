package tiago.canilhas.notebook.data.repository

import tiago.canilhas.notebook.data.db.dao.NotebookDao
import tiago.canilhas.notebook.data.db.dao.PageDao
import tiago.canilhas.notebook.data.db.dao.SectionDao
import tiago.canilhas.notebook.data.db.entity.Notebook
import tiago.canilhas.notebook.data.db.entity.Page
import tiago.canilhas.notebook.data.db.entity.Section

class NotebookRepository(
    private val notebookDao: NotebookDao,
    private val sectionDao: SectionDao,
    private val pageDao: PageDao
) {

    /**
     * Notebook Functions
     */

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



    /**
     * Section Functions
     */

    fun getSectionsForNotebook(notebookId: Long) = sectionDao.getSectionsForNotebook(notebookId)

    suspend fun insertSection(section: Section) {
        sectionDao.insert(section)

        // TODO: Call API to make sync with Cloud in the future
    }

    suspend fun updateSection(section: Section) {
        // TODO: Call API to make sync with Cloud in the future

        sectionDao.update(section)
    }



    /**
     * Page Functions
     */

    fun getPagesForSection(sectionId: Long) = pageDao.getPagesForSection(sectionId)

    fun getPageById(pageId: Long) = pageDao.getPageById(pageId)

    suspend fun insertPage(page: Page): Long {
        // TODO: Call API to make sync with Cloud in the future

        return pageDao.insert(page)
    }

    suspend fun updatePage(page: Page) {
        page.updatedAt = System.currentTimeMillis()

        // TODO: Call API to make sync with Cloud in the future

        pageDao.update(page)
    }
}