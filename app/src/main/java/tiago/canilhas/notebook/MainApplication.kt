package tiago.canilhas.notebook

import android.app.Application
import androidx.room.Room
import tiago.canilhas.notebook.data.db.AppDatabase
import tiago.canilhas.notebook.data.repository.NotebookRepository

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

    override val repository: NotebookRepository by lazy {
        NotebookRepository(
            notebookDao = database.notebookDao(),
            sectionDao = database.sectionDao(),
            pageDao = database.pageDao()
        )
    }

    override fun onCreate() {
        super.onCreate()
    }

}