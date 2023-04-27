package com.pnj.resto.data.makanan

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.pnj.resto.R
import java.io.File

class MakananAdapter(private val makananList: ArrayList<Makanan>) :
    RecyclerView.Adapter<MakananAdapter.MakananViewHolder>(){
    class MakananViewHolder (makananItemView: View) : RecyclerView.ViewHolder(makananItemView) {
        val nama_makanan : TextView = makananItemView.findViewById(R.id.TVLNamaMakanan)
        val jenis_makanan : TextView = makananItemView.findViewById(R.id.TVLJenisMakanan)
        val harga_makanan : TextView = makananItemView.findViewById(R.id.TVLHargaMakanan)

        val img_makanan: ImageView = itemView.findViewById(R.id.IMLGambarMakanan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakananViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.makanan_list_layout, parent, false)
        return MakananViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MakananViewHolder, position: Int) {
        val currentItem = makananList[position]
        val foto_dir = currentItem.foto_makanan.toString()
        val imgFile = File("${Environment.getExternalStorageDirectory()}/${foto_dir}")
        val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

        holder.img_makanan.setImageBitmap(myBitmap)
        holder.nama_makanan.text = currentItem.nama_makanan.toString()
        holder.jenis_makanan.text = currentItem.jenis_makanan.toString()
        holder.harga_makanan.text = currentItem.harga.toString()

        holder.itemView.setOnClickListener {
            val activity = it.context as AppCompatActivity
            activity.startActivity(Intent(activity, EditMakananActivity::class.java).apply {
                putExtra("nama_makanan", currentItem.nama_makanan.toString())
                putExtra("jenis_makanan", currentItem.jenis_makanan.toString())
                putExtra("harga_makanan", currentItem.harga.toString())
                putExtra("foto_makanan", currentItem.foto_makanan.toString())
                putExtra("id", currentItem.id.toString())
            })
        }
    }

    override fun getItemCount(): Int {
        return makananList.size
    }
}