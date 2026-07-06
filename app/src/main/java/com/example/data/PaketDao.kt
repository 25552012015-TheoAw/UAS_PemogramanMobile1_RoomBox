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
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PaketDao {
    @Transaction
    @Query("SELECT * FROM Paket ORDER BY id_paket DESC")
    fun getAllPaketWithUnit(): Flow<List<PaketWithUnit>>

    @Transaction
    @Query("SELECT * FROM Paket WHERE no_unit_fk LIKE '%' || :query || '%' OR no_resi LIKE '%' || :query || '%' OR nama_penerima LIKE '%' || :query || '%' ORDER BY id_paket DESC")
    fun searchPaketWithUnit(query: String): Flow<List<PaketWithUnit>>

    @Transaction
    @Query("SELECT * FROM Paket WHERE status_ambil = :status ORDER BY id_paket DESC")
    fun getPaketByStatus(status: String): Flow<List<PaketWithUnit>>

    @Transaction
    @Query("SELECT * FROM Paket WHERE (no_unit_fk LIKE '%' || :query || '%' OR no_resi LIKE '%' || :query || '%' OR nama_penerima LIKE '%' || :query || '%') AND status_ambil = :status ORDER BY id_paket DESC")
    fun searchPaketByStatus(query: String, status: String): Flow<List<PaketWithUnit>>

    @Transaction
    @Query("SELECT * FROM Paket WHERE id_paket = :id")
    suspend fun getPaketWithUnitById(id: Int): PaketWithUnit?

    @Query("SELECT COUNT(*) FROM Paket WHERE status_ambil = 'PENDING'")
    fun getPendingCount(): Flow<Int>

    @Query("SELECT * FROM Paket WHERE status_ambil = 'PENDING'")
    fun getPendingPaketList(): Flow<List<Paket>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaket(paket: Paket)

    @Update
    suspend fun updatePaket(paket: Paket)

    @Query("UPDATE Paket SET status_ambil = 'SELESAI', tanggal_ambil = :tanggalAmbil WHERE id_paket = :id")
    suspend fun markAsTaken(id: Int, tanggalAmbil: String)

    @Delete
    suspend fun deletePaket(paket: Paket)

    @Query("DELETE FROM Paket WHERE id_paket = :id")
    suspend fun deletePaketById(id: Int)
}
