package tiago.canilhas.notebook.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import tiago.canilhas.notebook.data.db.dao.PageDao

@Entity(
    tableName = "pages",
    foreignKeys = [
        ForeignKey(
            entity = Group::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["groupId"])]
)
data class Page(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val groupId: Long,
    var title: String,
    var order: Int,

    val width: Float = 1080f,
    val height: Float = 1920f,
    var content: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        fun create(
            groupId: Long,
            nextOrder: Int
        ): Page {
            val time = System.currentTimeMillis()

            return Page(
                groupId = groupId,
                title = "New Page",
                order = nextOrder,
                width = 1080f,
                height = 1920f,
                content = "",
                createdAt = time,
                updatedAt = time
            )
        }
    }
}