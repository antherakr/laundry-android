package com.ramadhani.laundry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    lateinit var CvLayanan : CardView
    lateinit var CvLaporan : CardView
    lateinit var CvTransaksi : CardView
    lateinit var CvRiwayat : CardView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CvLayanan = findViewById(R.id.CvLayanan)
        CvRiwayat = findViewById(R.id.CvRiwayat)
        CvLaporan = findViewById(R.id.CvLaporan)
        CvTransaksi = findViewById(R.id.CvTransaksi)

        CvLayanan.setOnClickListener {
            val keLayanan =
                Intent(this@MainActivity,
                    // (nama activity) -> layanan::class.java)
                    Layanan::class.java)
            startActivity(keLayanan)
        }

        CvTransaksi.setOnClickListener {
            val keTransaksi =
                Intent(this@MainActivity,
                    // (nama activity) -> layanan::class.java)
                    Transaksi::class.java)
            startActivity(keTransaksi)
        }

        CvRiwayat.setOnClickListener {
            val keRiwayat =
                Intent(this@MainActivity,
                    RiwayatActivity::class.java)
            startActivity(keRiwayat)
        }
    }
}