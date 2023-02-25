package app.amano.nayu.dance

import androidx.room.Embedded
import androidx.room.Relation

data class StageEntityWithDancerEntities(
    @Embedded val stage:StageEntity,
    @Relation(
        parentColumn = "uid",
        entityColumn = "uid"
    )
    val DancerEntities: List<DancerEntity>

)