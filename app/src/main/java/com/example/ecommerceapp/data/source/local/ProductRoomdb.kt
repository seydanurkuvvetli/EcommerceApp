package com.example.ecommerceapp.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ecommerceapp.data.model.ProductEntity

@Database(entities = [ProductEntity::class], version = 2,exportSchema = false)
abstract class ProductRoomDB: RoomDatabase() {
    abstract  fun productDao():ProductDao
    companion object{
        @Volatile
        private var instance:ProductRoomDB?=null
        private val lock=Any()
        val migration1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Geçiş işlemini burada tanımlayın
                database.execSQL("ALTER TABLE fav_products ADD COLUMN userId TEXT")
            }
        }

        operator fun invoke(context: Context)= instance ?: synchronized(lock){
            instance ?: makeDatabase(context).also { instance=it }
        }

        private fun makeDatabase(context:Context)= Room.databaseBuilder(
            context.applicationContext,ProductRoomDB::class.java,"productdatabase"
        ).addMigrations(migration1to2).fallbackToDestructiveMigration().build()

    }

}