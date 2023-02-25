package app.amano.nayu.dance

import android.media.audiofx.DynamicsProcessing
import androidx.room.*

@Dao
interface StageDao {
    // データを追加
    @Insert
    fun insert(stage:StageEntity)

    // データを更新
    @Update
    fun update(stage:StageEntity)

    // データを削除
    @Delete
    fun delete(stage:StageEntity)

    // 全てのデータを取得
    @Query("select * from stages")
    fun getAll(): List<StageEntity>

    // 全てのデータを削除
    @Query("delete from stages")
    fun deleteAll()

    // UserのuidがidのUserを取得
    @Query("select * from stages where uid = :id")
    fun getUser(id: Int): StageEntity

    // Userのnameが引数のnameと同じUserを一つだけ取得
    @Query("select * from stages where name = :name")
    fun getUser(name: String): StageEntity
    @Transaction
    @Query("SELECT * FROM stages")
    fun getUsersWithPlaylists(): List<StageEntityWithDancerEntities>
}