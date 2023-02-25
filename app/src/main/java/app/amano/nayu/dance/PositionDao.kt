package app.amano.nayu.dance

import androidx.room.*

@Dao
interface PositionDao {
    // データを追加
    @Insert
    fun insert(position:PositionEntity)

    // データを更新
    @Update
    fun update(position:PositionEntity)


    // データを削除
    @Delete
    fun delete(position:PositionEntity)

    // 全てのデータを取得
    @Query("select * from positions")
    fun getAll(): List<PositionEntity>

    // 全てのデータを削除
    @Query("delete from positions")
    fun deleteAll()

    // UserのuidがidのUserを取得
    @Query("select * from positions where dancer_id = :dancerId")
    fun getPositions(dancerId: Int): List<PositionEntity>

}