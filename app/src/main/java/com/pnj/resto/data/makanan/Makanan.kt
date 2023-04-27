package com.pnj.resto.data.makanan

import androidx.room.*
import java.io.Serializable

@Entity(tableName = "makanan")

data class Makanan(
    @ColumnInfo(name = "nama_makanan") var nama_makanan: String = "",
    @ColumnInfo(name = "jenis-makanan") var jenis_makanan: String = "",
    @ColumnInfo(name = "harga") var harga: Double = 0.0,
    @ColumnInfo(name = "foto-makanan") var foto_makanan: String = ""
) : Serializable {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
