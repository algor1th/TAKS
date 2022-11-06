package cc.h3x.taks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import cc.h3x.taks.ui.theme.TAKSTheme

class AddTodo : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TAKSTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val db = Room.databaseBuilder(
                        applicationContext,
                        TodoDatabase::class.java, "default"
                    ).allowMainThreadQueries().build()
                    var x by remember { mutableStateOf("") }

                    val ctx = LocalContext.current

                    Row {
                        OutlinedTextField(value = x, onValueChange = { newVal -> x = newVal })
                        Column() {
                            Button(onClick = saveTodo(db, x, ctx)) {
                                Text("Save")
                            }
                        }

                    }
                }
            }
        }
    }

    @Composable
    private fun saveTodo(
        db: TodoDatabase,
        description: String,
        ctx: Context
    ) = {
        db.todoDao().insertAll(
            Todo(0, description, false)
        )
        startActivity(Intent(ctx, MainActivity::class.java))
    }
}

@Composable
fun TodoDisplay(todo: Todo) {
    Card(
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
        {
            var todo by remember {
                mutableStateOf(todo)
            }
            Row {
                Checkbox(checked = todo.complete, onCheckedChange = { newC ->
                    todo.complete = newC
                })
                Text(text = todo.description)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val todo by remember {
        mutableStateOf(
            Todo(
                description = "Create a Todo list",
                complete = false
            )
        )
    }
    TAKSTheme {
        TodoDisplay(todo)
    }
}