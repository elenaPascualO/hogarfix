package com.hogarfix.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hogarfix.data.local.entity.HomeItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeItemDao {

    @Query("SELECT * FROM home_items ORDER BY name ASC")
    fun getAll(): Flow<List<HomeItemEntity>>

    @Query("SELECT * FROM home_items WHERE id = :id")
    suspend fun getById(id: Long): HomeItemEntity?

    @Query("SELECT * FROM home_items WHERE category = :category ORDER BY name ASC")
    fun getByCategory(category: String): Flow<List<HomeItemEntity>>

    @Query("SELECT * FROM home_items WHERE warrantyEndDate IS NOT NULL AND warrantyEndDate BETWEEN :today AND :futureDate ORDER BY warrantyEndDate ASC")
    fun getWithExpiringWarranty(today: Long, futureDate: Long): Flow<List<HomeItemEntity>>

    @Query("SELECT * FROM home_items WHERE name LIKE '%' || :query || '%' OR brand LIKE '%' || :query || '%' OR model LIKE '%' || :query || '%' OR notes LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchByText(query: String): Flow<List<HomeItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: HomeItemEntity): Long

    @Update
    suspend fun update(item: HomeItemEntity)

    @Delete
    suspend fun delete(item: HomeItemEntity)

    @Query("DELETE FROM home_items WHERE id = :id")
    suspend fun deleteById(id: Long)
}
