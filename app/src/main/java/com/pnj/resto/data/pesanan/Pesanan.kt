package com.pnj.resto.data.pesanan

import androidx.room.*
import com.pnj.resto.data.makanan.Makanan
import java.io.Serializable

@Entity(tableName = "pesanan",
    foreignKeys = [ForeignKey(
        entity = Makanan::class,
        childColumns = ["id_makanan"],
        parentColumns = ["id"]
    )]
)

data class Pesanan(
    @ColumnInfo(name = "waktu_pesan") var waktu_pesan: String = "",
    @ColumnInfo(name = "id_makanan") var id_makanan: Int = 0,
    @ColumnInfo(name = "jumlah") var jumlah: Int = 0
) : Serializable {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
