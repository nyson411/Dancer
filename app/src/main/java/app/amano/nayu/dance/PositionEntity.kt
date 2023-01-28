package app.amano.nayu.dance

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="positions")
data class Position(
    @PrimaryKey(autoGenerate = true)
    val uid:Int=0,
    @ColumnInfo(name="dancer_id")
    val dancerId: Int,
    @ColumnInfo(name="x")
    val x:Float,
    @ColumnInfo(name="y")
    val y:Float,
)
