package com.example.masterplan.ui.chores

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Chore::class], version = 1, exportSchema = false)
abstract class ChoreDatabase : RoomDatabase() {
    abstract fun choreDao(): ChoreDao

    companion object {
        @Volatile private var INSTANCE: ChoreDatabase? = null

        fun getInstance(context: Context): ChoreDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext, ChoreDatabase::class.java, "chores.db"
                ).build().also { INSTANCE = it }
            }
    }
}
