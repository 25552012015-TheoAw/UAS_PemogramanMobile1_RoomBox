package com.example.ui

/**
 * Project: RoomBox (Manajemen Paket Apartemen)
 * Author : TheoAleksanderWilliam
 * NIM    : 25552012015
 */

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Room
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UnitApartemen
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BluePrimaryDark
import com.example.ui.theme.BlueSecondary
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPacketScreen(
    viewModel: RoomBoxViewModel,
    onBack: () -> Unit,
    onSuccessSaved: () -> Unit
) {
    val units by viewModel.allUnits.collectAsState()
    val context = LocalContext.current

    var noResi by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf<UnitApartemen?>(null) }
    var namaPenerima by remember { mutableStateOf("") }
    var selectedEkspedisi by remember { mutableStateOf("JNE") }

    var unitDropdownExpanded by remember { mutableStateOf(false) }
    var ekspedisiDropdownExpanded by remember { mutableStateOf(false) }

    var showScanDialog by remember { mutableStateOf(false) }
    var isScanning by remember { mutableStateOf(false) }

    val ekspedisiList = listOf(
        "JNE", "J&T Express", "Shopee Express", "SiCepat",
        "GoSend", "GrabExpress", "AnterAja", "Ninja Xpress",
        "Pos Indonesia", "Paxel", "Lazada Logistics"
    )

    // OCR Barcode scan simulation dialog
    if (showScanDialog) {
        val transition = rememberInfiniteTransition(label = "laser")
        val laserY by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "laser_y"
        )

        LaunchedEffect(showScanDialog) {
            isScanning = true
            delay(2000L) // Simulates 2 seconds of camera scanning / OCR ML Kit processing
            val generatedResi = viewModel.generateSimulatedBarcode()
            noResi = generatedResi
            isScanning = false
            delay(500L)
            showScanDialog = false
            Toast.makeText(context, "Resi berhasil dipindai: $generatedResi", Toast.LENGTH_SHORT).show()
        }

        AlertDialog(
            onDismissRequest = {
                if (!isScanning) showScanDialog = false
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = BluePrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Scanner Resi & OCR Kamera", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isScanning) "Mencari barcode atau teks nomor resi pada paket..." else "Berhasil membaca barcode!",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0F172A))
                            .border(2.dp, BlueSecondary, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Simulated view of camera
                        Icon(
                            imageVector = if (isScanning) Icons.Default.CameraAlt else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (isScanning) Color.White.copy(alpha = 0.2f) else Color(0xFF4CAF50),
                            modifier = Modifier.size(64.dp)
                        )

                        if (isScanning) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val y = size.height * laserY
                                drawLine(
                                    color = Color(0xFF00E676),
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = 4f
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Fitur OCR ML Kit akan menyalin nomor resi secara otomatis ke dalam form input.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showScanDialog = false }) {
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
                        text = "Input Paket Masuk",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Data Penerimaan Kurir",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "Lengkapi form log barang baru masuk di bawah ini",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // SCAN BARCODE / OCR BUTTON (btn_scan_resi)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(BluePrimary.copy(alpha = 0.06f))
                            .clickable { showScanDialog = true }
                            .padding(16.dp)
                            .testTag("btn_scan_resi")
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawRoundRect(
                                color = BluePrimary,
                                style = Stroke(
                                    width = 3f,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 16f), 0f)
                                ),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx())
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = "Scan Resi",
                                tint = BluePrimary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Scan Resi / Barcode Kamera (OCR)",
                                    fontWeight = FontWeight.Bold,
                                    color = BluePrimaryDark,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Ketuk untuk memindai resi secara otomatis",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 1. NOMOR RESI (et_no_resi)
                    OutlinedTextField(
                        value = noResi,
                        onValueChange = { noResi = it },
                        label = { Text("Nomor Resi Paket (Kurir)") },
                        leadingIcon = { Icon(Icons.Default.Inventory, contentDescription = null) },
                        placeholder = { Text("Contoh: JP9876543210") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("et_no_resi")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. SPINNER UNIT TUJUAN (sp_unit_tujuan)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = selectedUnit?.let { "${it.noUnit} - ${it.namaPenghuni}" } ?: "Pilih Unit Kamar Tujuan...",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Nomor Unit Apartemen") },
                            leadingIcon = { Icon(Icons.Default.Room, contentDescription = null) },
                            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("sp_unit_tujuan")
                                .clickable { unitDropdownExpanded = true }
                        )

                        DropdownMenu(
                            expanded = unitDropdownExpanded,
                            onDismissRequest = { unitDropdownExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            if (units.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("Belum ada data unit (Silakan tambah di menu Kelola Unit)") },
                                    onClick = { unitDropdownExpanded = false }
                                )
                            } else {
                                units.forEach { unit ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text("Unit ${unit.noUnit}", fontWeight = FontWeight.Bold)
                                                Text("Penghuni: ${unit.namaPenghuni}", fontSize = 12.sp, color = Color.Gray)
                                            }
                                        },
                                        onClick = {
                                            selectedUnit = unit
                                            namaPenerima = unit.namaPenghuni
                                            unitDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3. NAMA PENERIMA / PENGHUNI
                    OutlinedTextField(
                        value = namaPenerima,
                        onValueChange = { namaPenerima = it },
                        label = { Text("Nama Penerima Paket") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        placeholder = { Text("Nama penghuni atau penerima") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4. SPINNER EKSPEDISI (sp_ekspedisi)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = selectedEkspedisi,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Jasa Ekspedisi / Kurir") },
                            leadingIcon = { Icon(Icons.Default.LocalShipping, contentDescription = null) },
                            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("sp_ekspedisi")
                                .clickable { ekspedisiDropdownExpanded = true }
                        )

                        DropdownMenu(
                            expanded = ekspedisiDropdownExpanded,
                            onDismissRequest = { ekspedisiDropdownExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            ekspedisiList.forEach { eksp ->
                                DropdownMenuItem(
                                    text = { Text(eksp, fontWeight = FontWeight.Medium) },
                                    onClick = {
                                        selectedEkspedisi = eksp
                                        ekspedisiDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // BUTTON SIMPAN PAKET (btn_simpan_paket)
                    Button(
                        onClick = {
                            if (noResi.isBlank()) {
                                Toast.makeText(context, "Nomor Resi wajib diisi!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (selectedUnit == null) {
                                Toast.makeText(context, "Silakan pilih Unit Tujuan!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (namaPenerima.isBlank()) {
                                Toast.makeText(context, "Nama Penerima wajib diisi!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            viewModel.addPaket(
                                noResi = noResi,
                                namaPenerima = namaPenerima,
                                ekspedisi = selectedEkspedisi,
                                noUnitFk = selectedUnit!!.noUnit,
                                onSuccess = {
                                    Toast.makeText(context, "Paket resi $noResi berhasil disimpan di loker", Toast.LENGTH_SHORT).show()
                                    onSuccessSaved()
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("btn_simpan_paket")
                    ) {
                        Text(
                            text = "SIMPAN DATA PAKET",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
