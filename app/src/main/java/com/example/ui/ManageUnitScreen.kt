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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Room
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UnitApartemen
import com.example.ui.theme.AlertRedText
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BluePrimaryDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageUnitScreen(
    viewModel: RoomBoxViewModel,
    onBack: () -> Unit
) {
    val units by viewModel.allUnits.collectAsState()
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var editingUnit by remember { mutableStateOf<UnitApartemen?>(null) }

    var noUnitInput by remember { mutableStateOf("") }
    var namaInput by remember { mutableStateOf("") }
    var noHpInput by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                editingUnit = null
            },
            title = {
                Text(
                    text = if (editingUnit == null) "Tambah Unit Apartemen Baru" else "Edit Data Unit ${editingUnit?.noUnit}",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = noUnitInput,
                        onValueChange = { noUnitInput = it },
                        label = { Text("Nomor Unit (Contoh: A-1205)") },
                        leadingIcon = { Icon(Icons.Default.Room, contentDescription = null) },
                        enabled = editingUnit == null, // PK cannot be edited
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    OutlinedTextField(
                        value = namaInput,
                        onValueChange = { namaInput = it },
                        label = { Text("Nama Lengkap Penghuni") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    OutlinedTextField(
                        value = noHpInput,
                        onValueChange = { noHpInput = it },
                        label = { Text("No. HP / Kontak Aktif") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (noUnitInput.isBlank() || namaInput.isBlank()) {
                            Toast.makeText(context, "Nomor Unit dan Nama wajib diisi!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (editingUnit == null) {
                            viewModel.addUnit(noUnitInput, namaInput, noHpInput)
                            Toast.makeText(context, "Unit berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.updateUnit(
                                UnitApartemen(editingUnit!!.noUnit, namaInput.trim(), noHpInput.trim())
                            )
                            Toast.makeText(context, "Unit berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        }
                        showDialog = false
                        editingUnit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        editingUnit = null
                    }
                ) {
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
                        text = "Master Data Unit Apartemen",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BluePrimaryDark)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingUnit = null
                    noUnitInput = ""
                    namaInput = ""
                    noHpInput = ""
                    showDialog = true
                },
                containerColor = BluePrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("btn_add_unit")
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Unit")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tambah Unit", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BluePrimaryDark.copy(alpha = 0.08f))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Total terdaftar: ${units.size} Unit Kamar",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }

            if (units.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada data unit kamar terdaftar", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("rv_unit_list")
                ) {
                    items(units, key = { it.noUnit }) { unit ->
                        UnitItemCard(
                            unit = unit,
                            onEdit = {
                                editingUnit = unit
                                noUnitInput = unit.noUnit
                                namaInput = unit.namaPenghuni
                                noHpInput = unit.noHp
                                showDialog = true
                            },
                            onDelete = {
                                viewModel.deleteUnit(unit)
                                Toast.makeText(context, "Unit ${unit.noUnit} dihapus", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UnitItemCard(
    unit: UnitApartemen,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Surface(
                    color = BluePrimary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(end = 14.dp)
                ) {
                    Text(
                        text = unit.noUnit,
                        fontWeight = FontWeight.ExtraBold,
                        color = BluePrimaryDark,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
                Column {
                    Text(
                        text = unit.namaPenghuni,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = unit.noHp.ifBlank { "No. HP tidak dicatat" },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.Gray
                            )
                        )
                    }
                }
            }

            Row {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.testTag("btn_edit_unit")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Unit",
                        tint = Color(0xFF1E88E5)
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.testTag("btn_delete_unit")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus Unit",
                        tint = AlertRedText
                    )
                }
            }
        }
    }
}
