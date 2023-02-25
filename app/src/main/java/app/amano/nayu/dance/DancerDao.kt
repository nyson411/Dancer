package app.amano.nayu.dance

import androidx.room.*

@Dao
interface DancerDao {
    // データを追加
    @Insert
    fun insert(dancer: DancerEntity)

    // データを更新
    @Update
    fun update(dancer: DancerEntity)

    // データを削除
    @Delete
    fun delete(dancer: DancerEntity)

    // 全てのデータを取得
    @Query("select * from dancers")
    fun getAll(): List<DancerEntity>

    // 全てのデータを削除
    @Query("delete from dancers")
    fun deleteAll()

    //
    @Query("select * from dancers where stage_id = :stageId")
    fun getDancers(stageId: Int): MutableList<DancerEntity>

    @Transaction
    @Query("SELECT * FROM dancers")
    fun getUsersWithPlaylists(): List<DancerEntityWithPositionEntities>
}