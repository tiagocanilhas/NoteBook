package tiago.canilhas.notebook

import tiago.canilhas.notebook.data.repository.NotebookRepository

interface Container {
    val repository: NotebookRepository
}