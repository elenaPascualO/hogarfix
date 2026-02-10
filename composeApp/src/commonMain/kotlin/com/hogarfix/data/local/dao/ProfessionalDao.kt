package com.hogarfix.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hogarfix.data.local.entity.ProfessionalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfessionalDao {

    @Query("SELECT * FROM professionals ORDER BY name ASC")
    fun getAll(): Flow<List<ProfessionalEntity>>

    @Query("SELECT * FROM professionals WHERE id = :id")
    suspend fun getById(id: Long): ProfessionalEntity?

    @Query("SELECT * FROM professionals WHERE specialty = :specialty ORDER BY personalRating DESC, name ASC")
    fun getBySpecialty(specialty: String): Flow<List<ProfessionalEntity>>

    @Query("SELECT * FROM professionals WHERE name LIKE '%' || :query || '%' OR notes LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchByText(query: String): Flow<List<ProfessionalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(professional: ProfessionalEntity): Long

    @Update
    suspend fun update(professional: ProfessionalEntity)

    @Delete
    suspend fun delete(professional: ProfessionalEntity)

    @Query("DELETE FROM professionals WHERE id = :id")
    suspend fun deleteById(id: Long)
}
