package com.ramadhani.laundry

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.ramadhani.laundry.databinding.ActivityRiwayatBinding

class RiwayatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRiwayatBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var riwayatList: ArrayList<TransaksiData>
    private lateinit var adapter: RiwayatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi RecyclerView
        binding.rvRiwayat.layoutManager = LinearLayoutManager(this)
        riwayatList = ArrayList()
        adapter = RiwayatAdapter(this, riwayatList)
        binding.rvRiwayat.adapter = adapter

        // Inisialisasi Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Layanan/Transaksi") // Sesuaikan path dengan struktur data Anda

        // Mengambil Data dari Firebase
        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                riwayatList.clear() // Bersihkan list sebelum menambah data baru
                if (snapshot.exists()) {
                    Log.d("FirebaseData", "Data transaksi ditemukan")
                    for (dataSnap in snapshot.children) {
                        val transaksi = dataSnap.getValue(TransaksiData::class.java)
                        if (transaksi != null) {
                            riwayatList.add(transaksi) // Menambahkan transaksi ke list
                            Log.d("FirebaseData", "Transaksi ditambahkan: $transaksi")
                        }
                    }
                    adapter.notifyDataSetChanged() // Memberi tahu adapter untuk memperbarui tampilan
                } else {
                    Log.d("FirebaseData", "Tidak ada data transaksi ditemukan")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FirebaseData", "Gagal mengambil data: ${error.message}")
            }
        })
    }
}

