package cc.h3x.taks

import androidx.room.*

@Entity
data class Todo (
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "description")val description: String,
    @ColumnInfo(name = "Complete")var complete: Boolean = false
)

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo")
    fun getAll(): List<Todo>

    @Insert
    fun insertAll(vararg  todos: Todo)

    @Delete
    fun delete(todo: Todo)

    @Update
    fun update(todo: Todo)
}

@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}