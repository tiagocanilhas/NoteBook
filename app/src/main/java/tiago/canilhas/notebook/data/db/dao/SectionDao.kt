package tiago.canilhas.notebook.data.db.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import tiago.canilhas.notebook.data.db.entity.Section
import kotlinx.coroutines.flow.Flow

@Dao
interface SectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(section: Section): Long

    @Query("SELECT * FROM sections WHERE notebookId = :notebookId ORDER BY name ASC")
    fun getSectionsForNotebook(notebookId: Long): Flow<List<Section>>

    @Update
    suspend fun update(section: Section)

    @Delete
    suspend fun delete(section: Section)
}