package app.amano.nayu.dance

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="positions")
data class PositionEntity(
    @PrimaryKey(autoGenerate = true)
    val uid:Int=0,
    @ColumnInfo(name="dancer_id")
    val dancerId: Int,
    @ColumnInfo(name="x")
    var x:Float,
    @ColumnInfo(name="y")
    var y:Float,
)
