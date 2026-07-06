package com.example.ui

/**
 * Project: RoomBox (Manajemen Paket Apartemen)
 * Author : TheoAleksanderWilliam
 * NIM    : 25552012015
 */

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class RoomBoxViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RoomBoxRepository

    init {
        val database = RoomBoxDatabase.getDatabase(application)
        repository = RoomBoxRepository(database.unitApartemenDao(), database.paketDao())
    }

    val allUnits: StateFlow<List<UnitApartemen>> = repository.getAllUnits()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val pendingCount: StateFlow<Int> = repository.getPendingCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val overdueCount: StateFlow<Int> = repository.getPendingPaketList()
        .map { list ->
            val now = System.currentTimeMillis()
            val threeDaysMillis = 3L * 24 * 60 * 60 * 1000
            list.count { now - it.timestampMasuk > threeDaysMillis }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _statusFilter = MutableStateFlow("SEMUA") // SEMUA, BELUM DIAMBIL, SUDAH DIAMBIL
    val statusFilter: StateFlow<String> = _statusFilter.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val filteredPacketList: StateFlow<List<PaketWithUnit>> = combine(_searchQuery, _statusFilter) { query, filter ->
        Pair(query, filter)
    }.flatMapLatest { (query, filter) ->
        repository.searchPaket(query, filter)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setStatusFilter(filter: String) {
        _statusFilter.value = filter
    }

    // Unit Actions
    fun addUnit(noUnit: String, namaPenghuni: String, noHp: String) {
        viewModelScope.launch {
            repository.insertUnit(UnitApartemen(noUnit.trim().uppercase(), namaPenghuni.trim(), noHp.trim()))
        }
    }

    fun updateUnit(unit: UnitApartemen) {
        viewModelScope.launch {
            repository.updateUnit(unit)
        }
    }

    fun deleteUnit(unit: UnitApartemen) {
        viewModelScope.launch {
            repository.deleteUnit(unit)
        }
    }

    // Paket Actions
    fun addPaket(noResi: String, namaPenerima: String, ekspedisi: String, noUnitFk: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.insertPaket(
                noResi = noResi.trim().uppercase(),
                namaPenerima = namaPenerima.trim(),
                ekspedisi = ekspedisi.trim(),
                noUnitFk = noUnitFk.trim()
            )
            onSuccess()
        }
    }

    fun markPaketTaken(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.markPaketAsTaken(id)
            onSuccess()
        }
    }

    fun deletePaket(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deletePaketById(id)
            onSuccess()
        }
    }

    fun getPaketDetail(id: Int, onResult: (PaketWithUnit?) -> Unit) {
        viewModelScope.launch {
            val detail = repository.getPaketById(id)
            onResult(detail)
        }
    }

    fun generateSimulatedBarcode(): String {
        val prefixes = listOf("JP", "SPX", "JT", "SCP", "GOS", "GRB")
        val prefix = prefixes.random()
        val digits = (1..10).map { Random.nextInt(0, 10) }.joinToString("")
        return "$prefix$digits"
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: androidx.lifecycle.viewmodel.CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return RoomBoxViewModel(application) as T
            }
        }
    }
}
