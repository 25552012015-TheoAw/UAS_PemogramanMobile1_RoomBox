package com.example.data

/**
 * Project: RoomBox (Manajemen Paket Apartemen)
 * Author : TheoAleksanderWilliam
 * NIM    : 25552012015
 */

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitApartemenDao {
    @Query("SELECT * FROM UnitApartemen ORDER BY no_unit ASC")
    fun getAllUnits(): Flow<List<UnitApartemen>>

    @Query("SELECT * FROM UnitApartemen WHERE no_unit = :noUnit")
    suspend fun getUnitById(noUnit: String): UnitApartemen?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnit(unit: UnitApartemen)

    @Update
    suspend fun updateUnit(unit: UnitApartemen)

    @Delete
    suspend fun deleteUnit(unit: UnitApartemen)

    @Query("SELECT COUNT(*) FROM UnitApartemen")
    suspend fun getUnitCount(): Int
}
