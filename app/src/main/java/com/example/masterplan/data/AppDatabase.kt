package com.example.masterplan.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.masterplan.ui.calories.CalorieDao
import com.example.masterplan.ui.calories.CalorieEntry
import com.example.masterplan.ui.calories.DailyGoal
import com.example.masterplan.ui.calories.EntryType
import com.example.masterplan.ui.chores.Chore
import com.example.masterplan.ui.chores.ChoreDao
import com.example.masterplan.ui.habits.Habit
import com.example.masterplan.ui.habits.HabitDao

class EntryTypeConverter {
    @TypeConverter fun fromType(type: EntryType) = type.name
    @TypeConverter fun toType(name: String) = EntryType.valueOf(name)
}

@Database(
    entities = [Chore::class, Habit::class, CalorieEntry::class, DailyGoal::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(EntryTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun choreDao(): ChoreDao
    abstract fun habitDao(): HabitDao
    abstract fun calorieDao(): CalorieDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS habits (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, completedDate TEXT)")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS calorie_entries (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, calories INTEGER NOT NULL, type TEXT NOT NULL, date TEXT NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS daily_goal (date TEXT PRIMARY KEY NOT NULL, goal INTEGER NOT NULL)")
            }
        }

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "chores.db"
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build().also { INSTANCE = it }
            }
    }
}
