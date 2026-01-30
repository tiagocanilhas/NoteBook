package tiago.canilhas.notebook.data.repository

import tiago.canilhas.notebook.data.db.dao.GroupDao
import tiago.canilhas.notebook.data.db.dao.NotebookDao
import tiago.canilhas.notebook.data.db.dao.PageDao
import tiago.canilhas.notebook.data.db.dao.SectionDao
import tiago.canilhas.notebook.data.db.entity.Group
import tiago.canilhas.notebook.data.db.entity.Notebook
import tiago.canilhas.notebook.data.db.entity.Page
import tiago.canilhas.notebook.data.db.entity.Section

class PageRepository(
    private val pageDao: PageDao
) {
    fun getPagesForGroup(groupId: Long) = pageDao.getPagesForGroup(groupId)

    fun getPageById(pageId: Long) = pageDao.getPageById(pageId)

    suspend fun insertPage(groupId: Long): Long {
        val newOrder = (pageDao.getMaxOrder(groupId) ?: 0) + 1

        val page = Page.create(
            groupId = groupId,
            nextOrder = newOrder
        )

        // TODO: Call API to make sync with Cloud in the future

        return pageDao.insert(page)
    }

    suspend fun updatePage(page: Page) {
        page.updatedAt = System.currentTimeMillis()

        // TODO: Call API to make sync with Cloud in the future

        pageDao.update(page)
    }
}