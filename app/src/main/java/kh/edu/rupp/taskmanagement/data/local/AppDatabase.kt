package kh.edu.rupp.taskmanagement.data.local

import androidx.room.Database
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TaskEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        // three new columns, one line each: never fallbackToDestructiveMigration here
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE tasks ADD COLUMN placeLabel TEXT")
                db.execSQL("ALTER TABLE tasks ADD COLUMN latitude REAL")
                db.execSQL("ALTER TABLE tasks ADD COLUMN longitude REAL")
            }
        }
    }
}
