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
    fun getPageById(pageId: Long): Flow<Page?>

    @Query("SELECT * FROM pages WHERE groupId = :groupId ORDER BY createdAt ASC")
    fun getPagesForGroup(groupId: Long): Flow<List<Page>>

    @Query("SELECT MAX(`order`) FROM pages WHERE groupId = :groupId")
    suspend fun getMaxOrder(groupId: Long): Int?

    @Query("UPDATE pages SET `order` = `order` - 1 WHERE groupId = :groupId AND `order` > :deletedOrder")
    suspend fun closeGap(groupId: Long, deletedOrder: Int)

    @Update
    suspend fun update(page: Page)

    @Delete
    suspend fun delete(page: Page)
}