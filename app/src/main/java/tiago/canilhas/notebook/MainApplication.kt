package tiago.canilhas.notebook

import android.app.Application
import androidx.room.Room
import tiago.canilhas.notebook.data.db.AppDatabase
import tiago.canilhas.notebook.data.repository.GroupRepository
import tiago.canilhas.notebook.data.repository.NotebookRepository
import tiago.canilhas.notebook.data.repository.PageRepository
import tiago.canilhas.notebook.data.repository.SectionRepository

class MainApplication : Application(), Container {
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context = this,
            klass = AppDatabase::class.java,
            name = "notebook.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    override val notebookRepository: NotebookRepository by lazy {
        NotebookRepository(database.notebookDao())
    }

    override val sectionRepository: SectionRepository by lazy {
        SectionRepository(database.sectionDao())
    }

    override val groupRepository: GroupRepository by lazy {
        GroupRepository(database.groupDao())
    }

    override val pageRepository: PageRepository by lazy {
        PageRepository(database.pageDao())
    }

    override fun onCreate() {
        super.onCreate()
    }

}