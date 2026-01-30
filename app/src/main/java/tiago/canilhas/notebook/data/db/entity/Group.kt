package tiago.canilhas.notebook.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "groups",
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
data class Group(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sectionId: Long,
    var title: String,
    var order: Int,
) {
    companion object {
        fun create(
            sectionId: Long,
            title: String,
            nextOrder: Int
        ) = Group(
            sectionId = sectionId,
            title = title,
            order = nextOrder
        )
    }
}