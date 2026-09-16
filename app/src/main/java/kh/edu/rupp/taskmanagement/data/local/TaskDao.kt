package kh.edu.rupp.taskmanagement.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun observeTasks(): Flow<List<TaskEntity>>

    @Upsert
    suspend fun upsertAll(tasks: List<TaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Query("UPDATE tasks SET placeLabel = :label, latitude = :latitude, longitude = :longitude WHERE id = :id")
    suspend fun setPlace(id: String, label: String?, latitude: Double?, longitude: Double?)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteById(id: String)
}
