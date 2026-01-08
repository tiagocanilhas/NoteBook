package tiago.canilhas.notebook.data.db.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import tiago.canilhas.notebook.data.db.entity.Page
import kotlinx.coroutines.flow.Flow

@Dao
interface PageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(page: Page): Long

    @Query("SELECT * FROM pages WHERE id = :pageId")
    fun getPageById(pageId: Long): Flow<Page>

    @Query("SELECT * FROM pages WHERE sectionId = :sectionId ORDER BY createdAt ASC")
    fun getPagesForSection(sectionId: Long): Flow<List<Page>>

    @Update
    suspend fun update(page: Page)

    @Delete
    suspend fun delete(page: Page)
}