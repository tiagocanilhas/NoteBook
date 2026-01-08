package tiago.canilhas.notebook.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pages",
    foreignKeys = [
        ForeignKey(
            entity = Section::class,
            parentColumns = ["id"],
            childColumns = ["sectionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["sectionId"])]
)
data class Page(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sectionId: Long,
    var title: String,
    var content: String,
    val createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        fun create(sectionId: Long): Page {
            val currentTime = System.currentTimeMillis()
            return Page(
                sectionId = sectionId,
                title = "New Page",
                content = "",
                createdAt = currentTime,
                updatedAt = currentTime
            )
        }
    }
}