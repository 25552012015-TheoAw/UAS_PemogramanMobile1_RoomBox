package com.example.data

/**
 * Project: RoomBox (Manajemen Paket Apartemen)
 * Author : TheoAleksanderWilliam
 * NIM    : 25552012015
 */

import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RoomBoxRepository(
    private val unitDao: UnitApartemenDao,
    private val paketDao: PaketDao
) {
    // Unit Operations
    fun getAllUnits(): Flow<List<UnitApartemen>> = unitDao.getAllUnits()

    suspend fun getUnitById(noUnit: String): UnitApartemen? = unitDao.getUnitById(noUnit)

    suspend fun insertUnit(unit: UnitApartemen) = unitDao.insertUnit(unit)

    suspend fun updateUnit(unit: UnitApartemen) = unitDao.updateUnit(unit)

    suspend fun deleteUnit(unit: UnitApartemen) = unitDao.deleteUnit(unit)

    // Paket Operations
    fun getAllPaketWithUnit(): Flow<List<PaketWithUnit>> = paketDao.getAllPaketWithUnit()

    fun searchPaket(query: String, statusFilter: String): Flow<List<PaketWithUnit>> {
        return if (statusFilter == "SEMUA") {
            if (query.isBlank()) paketDao.getAllPaketWithUnit()
            else paketDao.searchPaketWithUnit(query)
        } else {
            val status = if (statusFilter == "BELUM DIAMBIL") "PENDING" else "SELESAI"
            if (query.isBlank()) paketDao.getPaketByStatus(status)
            else paketDao.searchPaketByStatus(query, status)
        }
    }

    suspend fun getPaketById(id: Int): PaketWithUnit? = paketDao.getPaketWithUnitById(id)

    fun getPendingCount(): Flow<Int> = paketDao.getPendingCount()

    fun getPendingPaketList(): Flow<List<Paket>> = paketDao.getPendingPaketList()

    suspend fun insertPaket(
        noResi: String,
        namaPenerima: String,
        ekspedisi: String,
        noUnitFk: String
    ) {
        val now = System.currentTimeMillis()
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        val paket = Paket(
            noResi = noResi,
            namaPenerima = namaPenerima,
            ekspedisi = ekspedisi,
            tanggalMasuk = sdf.format(Date(now)),
            timestampMasuk = now,
            statusAmbil = "PENDING",
            noUnitFk = noUnitFk
        )
        paketDao.insertPaket(paket)
    }

    suspend fun markPaketAsTaken(id: Int) {
        val now = System.currentTimeMillis()
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        paketDao.markAsTaken(id, sdf.format(Date(now)))
    }

    suspend fun deletePaketById(id: Int) = paketDao.deletePaketById(id)
}
