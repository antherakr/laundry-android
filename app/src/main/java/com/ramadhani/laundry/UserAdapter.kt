package com.ramadhani.laundry

import UserData
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserAdapter(val c: Context, val userList: ArrayList<UserData>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        var name: TextView = v.findViewById(R.id.tvNamaLayanan)
        var mbNum: TextView = v.findViewById(R.id.tvHargaLayanan)
        var mMenus: ImageView = v.findViewById(R.id.editmenus)

        init {
            mMenus.setOnClickListener { popupMenus(it) }
        }

        private fun popupMenus(v: View) {
            val position = userList[adapterPosition]
            val popupMenus = PopupMenu(c, v)
            popupMenus.inflate(R.menu.show_menu)
            popupMenus.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.editText -> {
                        val dialogView = LayoutInflater.from(c).inflate(R.layout.add_layanan, null)
                        val nameInput = dialogView.findViewById<EditText>(R.id.userName)
                        val priceInput = dialogView.findViewById<EditText>(R.id.userMb)

                        nameInput.setText(position.userName)
                        priceInput.setText(position.userMb)

                        AlertDialog.Builder(c)
                            .setView(dialogView)
                            .setPositiveButton("Ok") { dialogInterface, _ ->
                                val newName = nameInput.text.toString()
                                val newPrice = priceInput.text.toString()

                                updateDataInFirebase(position.userId, newName, newPrice)
                                dialogInterface.dismiss()
                            }
                            .setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }
                            .create()
                            .show()
                        true
                    }
                    R.id.delete -> {
                        AlertDialog.Builder(c)
                            .setTitle("Hapus")
                            .setIcon(R.drawable.ic_warning)
                            .setMessage("Apakah kamu yakin ingin menghapus data ini?")
                            .setPositiveButton("Ya") { dialog, _ ->
                                deleteDataInFirebase(position.userId)
                                dialog.dismiss()
                            }
                            .setNegativeButton("Tidak") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                        true
                    }
                    else -> true
                }
            }
            popupMenus.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.card_layanan, parent, false)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val newList = userList[position]
        holder.name.text = newList.userName
        holder.mbNum.text = newList.userMb
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    private fun updateDataInFirebase(userId: String?, newName: String, newPrice: String) {
        val database = FirebaseDatabase.getInstance("https://laundry-3df75-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("Layanan")

        if (userId != null) {
            val updatedData = mapOf(
                "userName" to newName,
                "userMb" to newPrice
            )

            myRef.child(userId).updateChildren(updatedData).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(c, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(c, "Gagal memperbarui data", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(c, "Gagal menemukan ID data untuk diperbarui", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteDataInFirebase(userId: String?) {
        val database = FirebaseDatabase.getInstance("https://laundry-3df75-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("Layanan")

        if (userId != null) {
            myRef.child(userId).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userList.removeIf { it.userId == userId }
                    notifyDataSetChanged()
                    Toast.makeText(c, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(c, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(c, "Gagal menemukan ID data untuk dihapus", Toast.LENGTH_SHORT).show()
        }
    }
}
