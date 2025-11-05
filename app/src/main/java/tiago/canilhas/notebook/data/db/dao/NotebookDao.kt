package tiago.canilhas.notebook.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tiago.canilhas.notebook.data.db.entity.Notebook

@Dao
interface NotebookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notebook: Notebook): Long

    @Query("SELECT * FROM notebooks ORDER BY createdAt DESC")
    fun getAllNotebooks(): Flow<List<Notebook>>

    @Query("SELECT * FROM notebooks WHERE id = :notebookId")
    fun getNotebookById(notebookId: Long): Flow<Notebook>

    @Update
    suspend fun update(notebook: Notebook)

    @Delete
    suspend fun delete(notebook: Notebook)

}