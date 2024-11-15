package com.ramadhani.laundry

import UserData
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Layanan : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private var userList: ArrayList<UserData> = ArrayList()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layanan)

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.rvLayanan)
        recyclerView.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(this, userList)
        recyclerView.adapter = userAdapter

        // Inisialisasi Firebase Database reference
        database = FirebaseDatabase.getInstance("https://laundry-3df75-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Layanan")

        // Panggil fungsi fetchData untuk membaca data dari Firebase
        fetchData()

        // Inisialisasi tombol tambah
        val addButton = findViewById<ImageView>(R.id.IvLayanan)
        addButton.setOnClickListener {
            showAddDialog()
        }
    }

    // Fungsi untuk mengambil data dari Firebase dan menampilkan ke RecyclerView
    private fun fetchData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear() // Menghapus data lama
                for (dataSnapshot in snapshot.children) {
                    val user = dataSnapshot.getValue(UserData::class.java)
                    user?.let { userList.add(it) } // Menambahkan data baru ke list
                }
                userAdapter.notifyDataSetChanged() // Memberitahukan adapter bahwa data telah berubah
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Layanan, "Gagal mengambil data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Fungsi untuk menampilkan dialog tambah layanan
    private fun showAddDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.add_layanan, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.userName)
        val priceInput = dialogView.findViewById<EditText>(R.id.userMb)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Tambah") { dialog, _ ->
                val name = nameInput.text.toString().trim()
                val price = priceInput.text.toString().trim()
                if (name.isNotEmpty() && price.isNotEmpty()) {
                    addNewService(name, price)
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    // Fungsi untuk menambahkan layanan baru ke Firebase
    private fun addNewService(name: String, price: String) {
        val newServiceRef = database.push()
        val newService = UserData(userId = newServiceRef.key ?: "", userName = name, userMb = price)
        newServiceRef.setValue(newService).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Layanan berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal menambah layanan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
