# 📦 RoomBox - Sistem Manajemen Paket Apartemen

**Proyek Perancangan Aplikasi Android**  
**Mata Kuliah:** Pemrograman Mobile 1  
**Dosen Pengajar:** Andri Nugraha Ramdhon, S.Kom., M.Kom.

---

## 📝 1. Deskripsi Aplikasi
**RoomBox** adalah aplikasi berbasis Android yang dirancang untuk mendigitalisasi sistem pencatatan paket di lobi apartemen. Aplikasi ini menggantikan sistem buku log manual yang rawan terselip dengan sistem digital yang lebih terorganisir untuk memantau arus masuk dan keluar barang milik penghuni.

## 🛠️ 2. Fitur Utama (CRUD)
Aplikasi ini mendukung fungsi pengelolaan data penuh:
* **➕ Create**: Mencatat data paket baru (Nomor Resi, Nama Penerima, Unit, Ekspedisi).
* **📖 Read**: Menampilkan daftar paket dengan fitur filter berdasarkan unit apartemen.
* **✏️ Update**: Memperbarui status paket dari "Belum Diambil" menjadi "Sudah Diambil".
* **🗑️ Delete**: Menghapus data catatan paket jika terjadi kesalahan input.

## 🚀 3. Teknologi yang Digunakan
* **📱 Bahasa Pemrograman**: Kotlin
* **🏗️ Arsitektur**: MVVM (Model-View-ViewModel) dengan pola *Single Activity*
* **🗄️ Database**: Room Persistence Library (SQLite)
* **⚙️ Framework/Library**:
    * Android Jetpack (Navigation Component & ViewBinding)
    * Material Components for Android

## 🗺️ 4. Struktur Navigasi
Aplikasi ini menggunakan sistem *Single Activity* dengan `MainActivity` sebagai host utama yang mengelola navigasi antar-fragmen berikut:
1. `SplashFragment`
2. `DashboardFragment`
3. `ManageUnitFragment`
4. `AddPacketFragment`
5. `PacketListFragment`
6. `DetailPacketFragment`

## 👤 5. Pengembangan
* **Nama**: Theo Aleksander William
* **NPM**: 25552012015
* **Institusi**: Departemen Teknik Informatika, Universitas Teknologi Bandung

---

### 💻 Cara Menjalankan Proyek
1. Pastikan Anda menggunakan **Android Studio** (versi terbaru disarankan).
2. Clone repositori ini atau buka folder proyek di Android Studio.
3. Sinkronisasi **Gradle** project.
4. Jalankan pada emulator atau perangkat fisik (min SDK 24+).

---
*Dokumentasi ini disusun untuk memenuhi tugas Ujian Akhir Semester (UAS) mata kuliah Pemrograman Mobile 1.*
