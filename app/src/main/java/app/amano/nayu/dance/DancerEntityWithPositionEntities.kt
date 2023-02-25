package app.amano.nayu.dance


import androidx.room.Embedded
import androidx.room.Relation

data class DancerEntityWithPositionEntities(
    @Embedded val dancer:DancerEntity,
    @Relation(
        parentColumn = "uid",
        entityColumn = "uid"
    )
    val PositionEntities: List<PositionEntity>

)