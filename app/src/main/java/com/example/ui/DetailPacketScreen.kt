package com.example.ui

/**
 * Project: RoomBox (Manajemen Paket Apartemen)
 * Author : TheoAleksanderWilliam
 * NIM    : 25552012015
 */

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Room
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PaketWithUnit
import com.example.ui.theme.AlertRedBg
import com.example.ui.theme.AlertRedText
import com.example.ui.theme.BadgeDoneBg
import com.example.ui.theme.BadgeDoneText
import com.example.ui.theme.BadgePendingBg
import com.example.ui.theme.BadgePendingText
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BluePrimaryDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPacketScreen(
    viewModel: RoomBoxViewModel,
    packetId: Int,
    onBack: () -> Unit,
    onSuccessDeletedOrUpdated: () -> Unit
) {
    val context = LocalContext.current
    var item by remember { mutableStateOf<PaketWithUnit?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(packetId) {
        viewModel.getPaketDetail(packetId) { result ->
            item = result
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Konfirmasi Hapus Paket", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin menghapus catatan rekam paket resi ${item?.paket?.noResi}? Langkah ini tidak dapat dibatalkan.") },
            confirmButton = {
                Button(
                    onClick = {
                        item?.let {
                            viewModel.deletePaket(it.paket.idPaket) {
                                Toast.makeText(context, "Paket resi ${it.paket.noResi} telah dihapus", Toast.LENGTH_SHORT).show()
                                showDeleteConfirm = false
                                onSuccessDeletedOrUpdated()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AlertRedText)
                ) {
                    Text("Hapus Sekarang")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Batal")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Informasi Paket",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BluePrimaryDark)
            )
        }
    ) { innerPadding ->
        if (item == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Memuat data detail paket...", color = Color.Gray)
            }
        } else {
            val paket = item!!.paket
            val unit = item!!.unit
            val isPending = paket.statusAmbil == "PENDING"
            val now = System.currentTimeMillis()
            val daysInLocker = ((now - paket.timestampMasuk) / (24L * 60 * 60 * 1000)).toInt()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // PARCEL INFO CARD
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = BluePrimary.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "UNIT ${paket.noUnitFk}",
                                        fontWeight = FontWeight.ExtraBold,
                                        color = BluePrimaryDark,
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                    )
                                }
                            }

                            // STATUS RETENSI BARANG (tv_detail_status)
                            Surface(
                                color = if (isPending) BadgePendingBg else BadgeDoneBg,
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isPending) Icons.Default.Inventory else Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (isPending) BadgePendingText else BadgeDoneText,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isPending) "BELUM DIAMBIL" else "SUDAH DIAMBIL",
                                        color = if (isPending) BadgePendingText else BadgeDoneText,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.testTag("tv_detail_status")
                                    )
                                }
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.4f))

                        // 1. NOMOR RESI (tv_detail_resi)
                        DetailItemRow(
                            icon = Icons.Default.Inventory,
                            label = "Nomor Resi Paket",
                            value = paket.noResi,
                            testTag = "tv_detail_resi",
                            valueColor = BluePrimaryDark,
                            valueBold = true
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // 2. NAMA PENGHUNI / PENERIMA (tv_detail_penghuni)
                        DetailItemRow(
                            icon = Icons.Default.Person,
                            label = "Nama Penerima / Penghuni Unit",
                            value = "${paket.namaPenerima} ${unit?.let { "(${it.namaPenghuni})" } ?: ""}",
                            testTag = "tv_detail_penghuni",
                            valueBold = true
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // 3. NO HP KONTAK
                        DetailItemRow(
                            icon = Icons.Default.Phone,
                            label = "Nomor Kontak Penghuni",
                            value = unit?.noHp?.ifBlank { "Tidak terdaftar" } ?: "Tidak terdaftar"
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // 4. EKSPEDISI
                        DetailItemRow(
                            icon = Icons.Default.LocalShipping,
                            label = "Jasa Kurir / Ekspedisi",
                            value = paket.ekspedisi
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // 5. TANGGAL MASUK
                        DetailItemRow(
                            icon = Icons.Default.CalendarToday,
                            label = "Waktu Penerimaan di Lobi",
                            value = paket.tanggalMasuk
                        )

                        if (!isPending && paket.tanggalAmbil != null) {
                            Spacer(modifier = Modifier.height(14.dp))
                            DetailItemRow(
                                icon = Icons.Default.CheckCircle,
                                label = "Waktu Pengambilan oleh Penghuni",
                                value = paket.tanggalAmbil!!,
                                valueColor = BadgeDoneText,
                                valueBold = true
                            )
                        }

                        // STORAGE DURATION SUMMARY
                        Spacer(modifier = Modifier.height(20.dp))
                        Surface(
                            color = if (daysInLocker > 3 && isPending) AlertRedBg else BluePrimary.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Room,
                                    contentDescription = null,
                                    tint = if (daysInLocker > 3 && isPending) AlertRedText else BluePrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (isPending) "Durasi Mengendap di Loker: $daysInLocker Hari" else "Status: Paket telah selesai diserahkan",
                                        fontWeight = FontWeight.Bold,
                                        color = if (daysInLocker > 3 && isPending) AlertRedText else MaterialTheme.colorScheme.onSurface,
                                        fontSize = 13.sp
                                    )
                                    if (daysInLocker > 3 && isPending) {
                                        Text(
                                            text = "Perhatian: Melebihi batas toleransi simpan (Overdue Alert >3 Hari)",
                                            color = AlertRedText.copy(alpha = 0.9f),
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // ACTION BUTTONS
                if (isPending) {
                    // TOMBOL BESAR WARNA HIJAU UPDATE AMBIL (btn_update_ambil)
                    Button(
                        onClick = {
                            viewModel.markPaketTaken(paket.idPaket) {
                                Toast.makeText(context, "Status paket resi ${paket.noResi} diubah menjadi SUDAH DIAMBIL", Toast.LENGTH_SHORT).show()
                                onSuccessDeletedOrUpdated()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)), // Warna Hijau
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("btn_update_ambil")
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "UPDATE AMBIL (SELESAI)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // TOMBOL SEKUNDER WARNA MERAH HAPUS PAKET (btn_delete_packet)
                    OutlinedButton(
                        onClick = { showDeleteConfirm = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AlertRedText),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, AlertRedText),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_delete_packet")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = AlertRedText)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("HAPUS PAKET", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimaryDark),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text("SELESAI / KEMBALI", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItemRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    testTag: String? = null,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    valueBold: Boolean = false
) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (valueBold) FontWeight.Bold else FontWeight.Medium,
                    color = valueColor
                ),
                modifier = if (testTag != null) Modifier.testTag(testTag) else Modifier
            )
        }
    }
}
