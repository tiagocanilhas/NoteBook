package tiago.canilhas.notebook.data.db.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tiago.canilhas.notebook.data.db.entity.Group

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(group: Group): Long

    @Query("SELECT * FROM groups WHERE sectionId = :sectionId")
    fun getGroupsForSection(sectionId: Long): Flow<List<Group>>

    @Query("SELECT MAX(`order`) FROM groups WHERE sectionId = :groupId")
    suspend fun getMaxOrder(groupId: Long): Int?

    @Update
    suspend fun update(group: Group)

    @Delete
    suspend fun delete(group: Group)
}