package com.example.data

/**
 * Project: RoomBox (Manajemen Paket Apartemen)
 * Author : TheoAleksanderWilliam
 * NIM    : 25552012015
 */

import androidx.room.Embedded
import androidx.room.Relation

data class PaketWithUnit(
    @Embedded val paket: Paket,
    @Relation(
        parentColumn = "no_unit_fk",
        entityColumn = "no_unit"
    )
    val unit: UnitApartemen?
)
