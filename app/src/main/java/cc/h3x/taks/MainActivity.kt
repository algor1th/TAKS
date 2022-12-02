package cc.h3x.taks

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import cc.h3x.taks.databinding.ActivityMainBinding
import com.google.android.material.color.DynamicColors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener { view ->
            startActivity(Intent(this, AddTodo::class.java))
        }

        val db = Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java, getString(R.string.taskDbName)
        ).allowMainThreadQueries().build()

        binding.rvTodoItems.layoutManager = LinearLayoutManager(this)

        val adapter = TodoAdapter(db.todoDao())
        binding.rvTodoItems.adapter = adapter

        ItemTouchHelper(SwipeItem(adapter)).attachToRecyclerView(binding.rvTodoItems)

        DynamicColors.applyToActivityIfAvailable(this)
    }
}