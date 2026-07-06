package com.example.data

/**
 * Project: RoomBox (Manajemen Paket Apartemen)
 * Author : TheoAleksanderWilliam
 * NIM    : 25552012015
 */

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Database(
    entities = [UnitApartemen::class, Paket::class],
    version = 1,
    exportSchema = false
)
abstract class RoomBoxDatabase : RoomDatabase() {

    abstract fun unitApartemenDao(): UnitApartemenDao
    abstract fun paketDao(): PaketDao

    companion object {
        @Volatile
        private var INSTANCE: RoomBoxDatabase? = null

        fun getDatabase(context: Context): RoomBoxDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomBoxDatabase::class.java,
                    "roombox_database"
                )
                .addCallback(RoomBoxDatabaseCallback(context))
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class RoomBoxDatabaseCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateInitialData(database.unitApartemenDao(), database.paketDao())
                }
            }
        }

        suspend fun populateInitialData(unitDao: UnitApartemenDao, paketDao: PaketDao) {
            if (unitDao.getUnitCount() == 0) {
                val units = listOf(
                    UnitApartemen("A-101", "Budi Santoso", "081234567890"),
                    UnitApartemen("A-1205", "Siti Aminah", "081987654321"),
                    UnitApartemen("B-302", "Andi Wijaya", "081567890123"),
                    UnitApartemen("C-508", "Rina Oktaviani", "081345678901"),
                    UnitApartemen("D-701", "Hendra Gunawan", "087812345678")
                )
                units.forEach { unitDao.insertUnit(it) }

                val now = System.currentTimeMillis()
                val fourDaysAgo = now - (4L * 24 * 60 * 60 * 1000)
                val oneDayAgo = now - (1L * 24 * 60 * 60 * 1000)
                val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))

                val initialPackets = listOf(
                    Paket(
                        noResi = "JP9876543210",
                        namaPenerima = "Siti Aminah",
                        ekspedisi = "JNE",
                        tanggalMasuk = sdf.format(Date(fourDaysAgo)),
                        timestampMasuk = fourDaysAgo,
                        statusAmbil = "PENDING",
                        noUnitFk = "A-1205"
                    ),
                    Paket(
                        noResi = "SPX1122334455",
                        namaPenerima = "Budi Santoso",
                        ekspedisi = "Shopee Express",
                        tanggalMasuk = sdf.format(Date(oneDayAgo)),
                        timestampMasuk = oneDayAgo,
                        statusAmbil = "PENDING",
                        noUnitFk = "A-101"
                    ),
                    Paket(
                        noResi = "JT5566778899",
                        namaPenerima = "Andi Wijaya",
                        ekspedisi = "J&T Express",
                        tanggalMasuk = sdf.format(Date(now - (5L * 24 * 60 * 60 * 1000))),
                        timestampMasuk = now - (5L * 24 * 60 * 60 * 1000),
                        statusAmbil = "SELESAI",
                        tanggalAmbil = sdf.format(Date(now - (2L * 24 * 60 * 60 * 1000))),
                        noUnitFk = "B-302"
                    )
                )
                initialPackets.forEach { paketDao.insertPaket(it) }
            }
        }
    }
}
