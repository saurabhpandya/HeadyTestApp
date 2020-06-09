package com.fidato.headytestapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fidato.headytestapp.db.dao.CatProductMappingDao
import com.fidato.headytestapp.db.dao.CategoryDao
import com.fidato.headytestapp.db.dao.ProductVarientMappingDao
import com.fidato.headytestapp.db.dao.RankingsDao
import com.fidato.headytestapp.db.model.CatProductMapping
import com.fidato.headytestapp.db.model.Category
import com.fidato.headytestapp.db.model.ProductVarientMapping
import com.fidato.headytestapp.db.model.Rankings

@Database(
    entities = [Category::class, CatProductMapping::class, ProductVarientMapping::class, Rankings::class],
    version = 1,
    exportSchema = false
)
abstract class HeadyDB : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun catProductMappingDao(): CatProductMappingDao

    abstract fun productVarientMappingDao(): ProductVarientMappingDao

    abstract fun rankingsDao(): RankingsDao

    companion object {

        @Volatile
        private var INSTANCE: HeadyDB? = null

        fun getDatabase(context: Context): HeadyDB? {
            if (INSTANCE == null) {
                synchronized(HeadyDB::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            HeadyDB::class.java, "heady_db"
                        ).build()
                    }
                }
            }
            return INSTANCE
        }
    }

}