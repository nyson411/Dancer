package app.amano.nayu.dance

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="stages")
data class StageEntity(
    @PrimaryKey(autoGenerate = true)
    val uid:Int=0,
    @ColumnInfo(name="name")
    val name:String,
    @ColumnInfo(name="scene_count")
    var scene_count: Int,

)
