package kh.edu.rupp.taskmanagement.data.local

import android.content.Context
import androidx.room.Room

// one database for the whole app, built the first time it is asked for
object DatabaseProvider {
    @Volatile
    private var instance: AppDatabase? = null

    fun get(context: Context): AppDatabase =
        instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "task-manager.db"
            )
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build().also { instance = it }
        }
}
