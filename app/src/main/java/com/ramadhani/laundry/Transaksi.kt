package com.ramadhani.laundry

import UserData
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class Transaksi : AppCompatActivity() {

    private lateinit var etNamaPelanggan: EditText
    private lateinit var spinnerLayanan: Spinner
    private lateinit var etSatuan: EditText
    private lateinit var tvHargaLayanan: TextView
    private lateinit var tvTanggalTransaksi: TextView
    private lateinit var btnSimpan: Button

    private lateinit var database: DatabaseReference
    private var layananList = ArrayList<UserData>()
    private var hargaPerSatuan: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)

        // Inisialisasi view
        etNamaPelanggan = findViewById(R.id.etNamaPelanggan)
        spinnerLayanan = findViewById(R.id.spinnerLayanan)
        etSatuan = findViewById(R.id.etSatuan)
        tvHargaLayanan = findViewById(R.id.tvHargaLayanan)
        tvTanggalTransaksi = findViewById(R.id.tvTanggalTransaksi)
        btnSimpan = findViewById(R.id.btnSimpan)

        // Inisialisasi Firebase Database reference
        database = FirebaseDatabase.getInstance("https://laundry-3df75-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Layanan")

        // Tampilkan tanggal saat ini
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        tvTanggalTransaksi.text = "Tanggal: $currentDate"

        // Panggil fungsi untuk memuat data layanan ke Spinner
        loadLayananData()

        // Event listener untuk menghitung total harga saat jumlah satuan berubah
        etSatuan.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateTotal()
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        // Event listener untuk menyimpan transaksi
        btnSimpan.setOnClickListener {
            saveTransaction()
        }
    }

    private fun loadLayananData() {
        // Ambil data layanan dari Firebase dan tambahkan ke Spinner
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                layananList.clear()
                val layananNames = ArrayList<String>()

                for (dataSnapshot in snapshot.children) {
                    // Mengambil objek UserData dan memastikan tidak null
                    val layanan = dataSnapshot.getValue(UserData::class.java)

                    layanan?.let { nonNullLayanan ->  // Pastikan layanan tidak null
                        nonNullLayanan.userName?.let { name ->  // Pastikan nama layanan tidak null
                            layananList.add(nonNullLayanan)
                            layananNames.add(name)
                        }
                    }
                }

                // Set adapter untuk Spinner
                val adapter = ArrayAdapter(this@Transaksi, android.R.layout.simple_spinner_item, layananNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerLayanan.adapter = adapter

                // Set listener untuk mengambil harga layanan yang dipilih
                spinnerLayanan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        hargaPerSatuan = layananList[position].userMb?.toIntOrNull() ?: 0
                        calculateTotal()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Transaksi, "Gagal memuat layanan: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun calculateTotal() {
        val satuan = etSatuan.text.toString().toIntOrNull() ?: 0
        val totalHarga = hargaPerSatuan * satuan
        tvHargaLayanan.text = "Harga: Rp $totalHarga"
    }

    private fun saveTransaction() {
        val namaPelanggan = etNamaPelanggan.text.toString().trim()
        val layanan = spinnerLayanan.selectedItem.toString()
        val satuan = etSatuan.text.toString().toIntOrNull() ?: 0
        val totalHarga = hargaPerSatuan * satuan
        val tanggal = tvTanggalTransaksi.text.toString().replace("Tanggal: ", "")

        if (namaPelanggan.isEmpty() || satuan == 0) {
            Toast.makeText(this, "Nama pelanggan dan satuan harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        // Buat data transaksi
        val transaksi = mapOf(
            "namaPelanggan" to namaPelanggan,
            "layanan" to layanan,
            "satuan" to satuan,
            "totalHarga" to totalHarga,
            "tanggal" to tanggal
        )

        // Simpan transaksi ke Firebase
        database.child("Transaksi").push().setValue(transaksi).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Transaksi berhasil disimpan", Toast.LENGTH_SHORT).show()
                etNamaPelanggan.text.clear()
                etSatuan.text.clear()
                tvHargaLayanan.text = "Harga: -"
            } else {
                Toast.makeText(this, "Gagal menyimpan transaksi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
