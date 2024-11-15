package com.ramadhani.laundry

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RiwayatAdapter(
    private val context: Context,
    private val riwayatList: ArrayList<TransaksiData>
) : RecyclerView.Adapter<RiwayatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_riwayat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaksi = riwayatList[position]
        holder.bind(transaksi)
    }

    override fun getItemCount(): Int {
        return riwayatList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNamaPelanggan: TextView = itemView.findViewById(R.id.tvNamaPelanggan)
        private val tvLayanan: TextView = itemView.findViewById(R.id.tvLayanan)
        private val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlah)
        private val tvHargaTotal: TextView = itemView.findViewById(R.id.tvHargaTotal)
        private val tvTanggalTransaksi: TextView = itemView.findViewById(R.id.tvTanggalTransaksi)

        fun bind(transaksi: TransaksiData) {
            tvNamaPelanggan.text = transaksi.namaPelanggan
            tvLayanan.text = "Layanan: ${transaksi.layanan}"
            tvJumlah.text = "Jumlah: ${transaksi.satuan} kg"
            tvHargaTotal.text = "Harga: Rp${transaksi.totalHarga}"
            tvTanggalTransaksi.text = "Tanggal: ${transaksi.tanggal}"
        }
    }
}
