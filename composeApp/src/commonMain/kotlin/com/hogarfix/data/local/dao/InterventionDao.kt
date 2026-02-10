package com.hogarfix.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hogarfix.data.local.entity.InterventionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InterventionDao {

    @Query("SELECT * FROM interventions ORDER BY date DESC")
    fun getAll(): Flow<List<InterventionEntity>>

    @Query("SELECT * FROM interventions WHERE id = :id")
    suspend fun getById(id: Long): InterventionEntity?

    @Query("SELECT * FROM interventions ORDER BY date DESC LIMIT :limit")
    fun getRecent(limit: Int): Flow<List<InterventionEntity>>

    @Query("SELECT * FROM interventions WHERE homeItemId = :itemId ORDER BY date DESC")
    fun getByHomeItem(itemId: Long): Flow<List<InterventionEntity>>

    @Query("SELECT * FROM interventions WHERE professionalId = :professionalId ORDER BY date DESC")
    fun getByProfessional(professionalId: Long): Flow<List<InterventionEntity>>

    @Query("SELECT * FROM interventions WHERE category = :category ORDER BY date DESC")
    fun getByCategory(category: String): Flow<List<InterventionEntity>>

    @Query("SELECT * FROM interventions WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR notes LIKE '%' || :query || '%' ORDER BY date DESC")
    fun searchByText(query: String): Flow<List<InterventionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(intervention: InterventionEntity): Long

    @Update
    suspend fun update(intervention: InterventionEntity)

    @Delete
    suspend fun delete(intervention: InterventionEntity)

    @Query("DELETE FROM interventions WHERE id = :id")
    suspend fun deleteById(id: Long)
}
