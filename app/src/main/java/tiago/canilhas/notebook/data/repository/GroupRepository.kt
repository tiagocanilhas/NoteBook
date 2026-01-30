package tiago.canilhas.notebook.data.repository

import tiago.canilhas.notebook.data.db.dao.GroupDao
import tiago.canilhas.notebook.data.db.dao.NotebookDao
import tiago.canilhas.notebook.data.db.dao.PageDao
import tiago.canilhas.notebook.data.db.dao.SectionDao
import tiago.canilhas.notebook.data.db.entity.Group
import tiago.canilhas.notebook.data.db.entity.Notebook
import tiago.canilhas.notebook.data.db.entity.Page
import tiago.canilhas.notebook.data.db.entity.Section

class GroupRepository(
    private val groupDao: GroupDao,
) {
    suspend fun insertGroup(sectionId: Long, title: String): Long {
        // TODO: Call API to make sync with Cloud in the future

        val newOrder = (groupDao.getMaxOrder(sectionId) ?: 0) + 1

        val group = Group.create(
            sectionId = sectionId,
            title = title,
            nextOrder = newOrder
        )

        return groupDao.insert(group)
    }

    fun getGroupsForSection(sectionId: Long) = groupDao.getGroupsForSection(sectionId)


    suspend fun updateGroup(group: Group) {
        // TODO: Call API to make sync with Cloud in the future

        groupDao.update(group)
    }

    suspend fun deleteGroup(group: Group) {
        // TODO: Call API to make sync with Cloud in the future

        groupDao.delete(group)
    }
}