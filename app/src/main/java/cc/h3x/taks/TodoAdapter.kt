package cc.h3x.taks

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cc.h3x.taks.databinding.ItemViewBinding

class TodoAdapter(private val todosDb: TodoDao) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private lateinit var binding: ItemViewBinding
    private lateinit var todos: MutableList<Todo>

    init {
        Thread {
            todos = todosDb.getAll().toMutableList()
        }.start()
    }

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView
        val done: CheckBox
        val card: CardView

        init {
            textView = itemView.findViewById(R.id.todoText)
            done = itemView.findViewById(R.id.todoDone)
            card = itemView.findViewById(R.id.todoCard)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_view, parent, false
        )

        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currTodo = todos[position]
        holder.textView.text = currTodo.description
        holder.done.isChecked = currTodo.complete

        if (currTodo.complete) {
            holder.textView.paintFlags = holder.textView.paintFlags or STRIKE_THRU_TEXT_FLAG
        }

        holder.card.setOnClickListener { view ->
            Thread {
                currTodo.complete = !currTodo.complete
                todosDb.update(currTodo)
            }.start()
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    fun deleteItem(i: Int) {
        Thread {
            todosDb.delete(todos.get(i))
            todos.removeAt(i)
        }.start()
        notifyItemRemoved(i)
    }
}