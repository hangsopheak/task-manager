package kh.edu.rupp.taskmanagement.concepts

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Entity(tableName = "notes")
data class NoteEntity(@PrimaryKey val id: Int, val text: String)

@Dao
interface NoteDao {
    @Insert suspend fun insert(note: NoteEntity)

    @Query("SELECT * FROM notes")
    fun observe(): Flow<List<NoteEntity>>
}

@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

class RoomDemoViewModel(app: Application) : AndroidViewModel(app) {
    private val db = Room.databaseBuilder(app, NoteDatabase::class.java, "notes.db").build()
    private val dao = db.noteDao()

    val notes = dao.observe()

    private var nextNumber = 1

    fun addNote() {
        viewModelScope.launch {
            dao.insert(NoteEntity(nextNumber, "Note $nextNumber"))
            nextNumber++
        }
    }
}

@Composable
fun RoomDemo(vm: RoomDemoViewModel = viewModel()) {
    val notes by vm.notes.collectAsState(initial = emptyList())
    Column(Modifier.padding(16.dp)) {
        Button(onClick = { vm.addNote() }) { Text("Insert") }
        for (note in notes) {
            Text("${note.id}: ${note.text}")
        }
    }
}
