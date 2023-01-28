package app.amano.nayu.dance

import androidx.room.Embedded
import androidx.room.Relation

data class StageEntityWithDancerEntity(
    @Embedded val stage:StageEntity,
    @Relation  
)