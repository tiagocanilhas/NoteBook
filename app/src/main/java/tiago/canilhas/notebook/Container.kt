package tiago.canilhas.notebook

import tiago.canilhas.notebook.data.repository.GroupRepository
import tiago.canilhas.notebook.data.repository.NotebookRepository
import tiago.canilhas.notebook.data.repository.PageRepository
import tiago.canilhas.notebook.data.repository.SectionRepository

interface Container {
    val notebookRepository: NotebookRepository
    val sectionRepository: SectionRepository
    val groupRepository: GroupRepository
    val pageRepository: PageRepository
}