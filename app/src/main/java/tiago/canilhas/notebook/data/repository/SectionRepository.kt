package tiago.canilhas.notebook.data.repository

import tiago.canilhas.notebook.data.db.dao.SectionDao
import tiago.canilhas.notebook.data.db.entity.Section

class SectionRepository(
    private val sectionDao: SectionDao,
) {

    fun getSectionsForNotebook(notebookId: Long) = sectionDao.getSectionsForNotebook(notebookId)

    suspend fun insertSection(notebookId: Long, name: String) {

        val newSectionOrder = (sectionDao.getMaxOrder(notebookId) ?: 0) + 1

        val section = Section.create(
            notebookId = notebookId,
            title = name,
            nextOrder = newSectionOrder
        )

        // TODO: Call API to make sync with Cloud in the future

        sectionDao.insert(section)
    }

    suspend fun updateSection(section: Section) {
        // TODO: Call API to make sync with Cloud in the future

        sectionDao.update(section)
    }
}