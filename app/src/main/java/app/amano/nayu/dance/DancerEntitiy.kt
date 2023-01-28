package app.amano.nayu.dance


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="dancers")
data class Dancers(
    @PrimaryKey(autoGenerate = true)
    val uid:Int=0,
    @ColumnInfo(name="name")
    val name: String,
    @ColumnInfo(name="color")
    val color:String,
    @ColumnInfo(name="stage_id")
    val stageId:Int,
)
