package com.example.data

/**
 * Project: RoomBox (Manajemen Paket Apartemen)
 * Author : TheoAleksanderWilliam
 * NIM    : 25552012015
 */

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UnitApartemen")
data class UnitApartemen(
    @PrimaryKey
    @ColumnInfo(name = "no_unit")
    val noUnit: String,

    @ColumnInfo(name = "nama_penghuni")
    val namaPenghuni: String,

    @ColumnInfo(name = "no_hp")
    val noHp: String
)
