package com.ramadhani.laundry

data class TransaksiData(
        val layanan: String = "",
        val namaPelanggan: String = "",
        val satuan: Int = 0,
        val tanggal: String = "",
        val totalHarga: Int = 0
)
