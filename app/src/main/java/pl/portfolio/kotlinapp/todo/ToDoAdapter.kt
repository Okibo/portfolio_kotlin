package pl.portfolio.kotlinapp.todo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import pl.portfolio.kotlinapp.R


class ToDoAdapter(private val context: Context, private val todoList: ArrayList<ToDoEntry>, private val db: ToDoDatabase): RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.todo_record, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.todoCheck.isChecked = todoList[position].isChecked
        viewHolder.todoTitle.text = todoList[position].title
        viewHolder.todoNote.text = todoList[position].note
        viewHolder.todoCheck.setOnClickListener{
            todoList[position].isChecked = viewHolder.todoCheck.isChecked
            db.updateEntry(todoList[position])
        }
        viewHolder.itemView.setOnClickListener{
            showAddEntryDialog(position)
        }

    }

    private fun showAddEntryDialog(position: Int){
        val dialog = MaterialDialog(context)
                .noAutoDismiss()
                .customView(R.layout.todo_add_layout)

        dialog.findViewById<TextView>(R.id.todo_apply_button).setOnClickListener{
            val title: String = dialog.findViewById<EditText>(R.id.todo_add_title_edit).text.toString()
            val note: String = dialog.findViewById<EditText>(R.id.todo_add_note_edit).text.toString()

            if(title.isNotEmpty()){
                todoList[position].title = title
                todoList[position].note = note
                db.updateEntry(todoList[position])
                notifyDataSetChanged()
                dialog.dismiss()
            }else{
                Toast.makeText(context, R.string.todo_no_title_err, Toast.LENGTH_SHORT)
                        .show()
            }
        }

        dialog.findViewById<TextView>(R.id.todo_cancel_button).setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
        dialog.findViewById<TextView>(R.id.todo_apply_button).setText(R.string.todo_dialog_edit_btn)
        dialog.findViewById<EditText>(R.id.todo_add_title_edit).setText(todoList[position].title)
        dialog.findViewById<EditText>(R.id.todo_add_note_edit).setText(todoList[position].note)
    }

    override fun getItemCount() = todoList.size


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val todoCheck: CheckBox = v.findViewById(R.id.todocheck)
        val todoTitle: TextView = v.findViewById(R.id.todotitle)
        val todoNote: TextView = v.findViewById(R.id.todonote)
    }

    fun addEntry(entry: ToDoEntry){
        todoList.add(entry)
    }

    fun removeCheckedEntries(){
        val entriesIds = arrayListOf<String>()
        val entriesIterator = todoList.iterator()

        while(entriesIterator.hasNext()){
            val it = entriesIterator.next()
            if(it.isChecked){
                entriesIds.add(it.id.toString())
                entriesIterator.remove()
            }
        }
        db.deleteEntries(entriesIds)
    }
}