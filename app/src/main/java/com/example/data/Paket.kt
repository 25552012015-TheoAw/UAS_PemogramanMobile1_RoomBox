package com.example.data

/**
 * Project: RoomBox (Manajemen Paket Apartemen)
 * Author : TheoAleksanderWilliam
 * NIM    : 25552012015
 */

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Paket",
    foreignKeys = [
        ForeignKey(
            entity = UnitApartemen::class,
            parentColumns = ["no_unit"],
            childColumns = ["no_unit_fk"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["no_unit_fk"])]
)
data class Paket(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_paket")
    val idPaket: Int = 0,

    @ColumnInfo(name = "no_resi")
    val noResi: String,

    @ColumnInfo(name = "nama_penerima")
    val namaPenerima: String,

    @ColumnInfo(name = "ekspedisi")
    val ekspedisi: String,

    @ColumnInfo(name = "tanggal_masuk")
    val tanggalMasuk: String,

    @ColumnInfo(name = "timestamp_masuk")
    val timestampMasuk: Long,

    @ColumnInfo(name = "status_ambil")
    val statusAmbil: String = "PENDING",

    @ColumnInfo(name = "tanggal_ambil")
    val tanggalAmbil: String? = null,

    @ColumnInfo(name = "no_unit_fk")
    val noUnitFk: String
)
