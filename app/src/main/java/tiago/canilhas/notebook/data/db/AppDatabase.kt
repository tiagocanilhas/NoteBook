package tiago.canilhas.notebook.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import tiago.canilhas.notebook.data.db.dao.NotebookDao
import tiago.canilhas.notebook.data.db.dao.PageDao
import tiago.canilhas.notebook.data.db.dao.SectionDao
import tiago.canilhas.notebook.data.db.entity.Notebook
import tiago.canilhas.notebook.data.db.entity.Page
import tiago.canilhas.notebook.data.db.entity.Section

@Database(
    entities = [
        Notebook::class,
        Section::class,
        Page::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notebookDao(): NotebookDao
    abstract fun sectionDao(): SectionDao
    abstract fun pageDao(): PageDao
}