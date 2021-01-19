package pl.portfolio.kotlinapp.todo

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import pl.portfolio.kotlinapp.R

class ToDoActivity: AppCompatActivity() {

    private val todoListView: RecyclerView by lazy {findViewById<RecyclerView>(R.id.todoListView)}
    private val db : ToDoDatabase by lazy {ToDoDatabase(this)}
    private val adapter: ToDoAdapter by lazy {ToDoAdapter(this, db.getAllEntries(), db)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todolist_activity)
        title = "To Do list"

        val linearManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(todoListView.context,linearManager.orientation)
        todoListView.adapter = adapter
        todoListView.layoutManager = linearManager
        todoListView.addItemDecoration(dividerItemDecoration);
        todoListView.setHasFixedSize(true)
    }

    private fun showAddEntryDialog(){
        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .customView(R.layout.todo_add_layout)

        dialog.findViewById<TextView>(R.id.todo_apply_button).setOnClickListener{
            val title: String = dialog.findViewById<EditText>(R.id.todo_add_title_edit).text.toString()
            val note: String = dialog.findViewById<EditText>(R.id.todo_add_note_edit).text.toString()

            if(title.isNotEmpty()){
                adapter.addEntry(db.addEntry(title,note))
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }else{
                Toast.makeText(applicationContext, R.string.todo_no_title_err, Toast.LENGTH_SHORT)
                        .show()
            }
        }

        dialog.findViewById<TextView>(R.id.todo_cancel_button).setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.todo_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.todo_add_btn) {
            showAddEntryDialog()
            return true
        }
        if(id == R.id.todo_remove_entries_btn){
            adapter.removeCheckedEntries()
            adapter.notifyDataSetChanged()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        db.closeConnection()
    }
}

