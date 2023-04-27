package com.pnj.resto.data.pesanan

import androidx.room.*

@Dao

interface PesananDao {
    @Insert
    suspend fun insertPesanan(pesanan: Pesanan)

    @Update(entity = Pesanan::class)
    suspend fun updatePesanan(pesanan: Pesanan)

    @Delete
    suspend fun deletePesanan(pesanan: Pesanan)

    @Query("SELECT * FROM pesanan ORDER BY id DESC")
    suspend fun getAllPesanan(): List<Pesanan>
}