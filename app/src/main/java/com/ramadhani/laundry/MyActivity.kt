package com.ramadhani.laundry

import UserData
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private var userList: ArrayList<UserData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layanan) // Ganti dengan layout Anda

        recyclerView = findViewById(R.id.rvLayanan) // Ganti dengan ID RecyclerView Anda
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inisialisasi UserAdapter
        userAdapter = UserAdapter(this, userList)
        recyclerView.adapter = userAdapter
    }
}
