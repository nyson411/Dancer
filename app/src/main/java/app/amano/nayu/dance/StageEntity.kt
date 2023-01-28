package app.amano.nayu.dance

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="stages")
data class Stages(
    @PrimaryKey(autoGenerate = true)
    val uid:Int=0,
    @ColumnInfo(name="stage_count")
    val stage_count: Int,
)
