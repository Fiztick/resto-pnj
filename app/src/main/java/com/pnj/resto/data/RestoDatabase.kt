package com.pnj.resto.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pnj.resto.data.makanan.Makanan
import com.pnj.resto.data.makanan.MakananDao
import com.pnj.resto.data.pesanan.Pesanan
import com.pnj.resto.data.pesanan.PesananDao

@Database(entities = [Makanan::class, Pesanan::class], version = 1)

abstract class RestoDatabase : RoomDatabase() {

    abstract fun getMakananDao(): MakananDao
    abstract fun getPesananDao(): PesananDao

    companion object {
        @Volatile
        private var instance: RestoDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            RestoDatabase::class.java,
            "resto-db"
        ).build()
    }
}