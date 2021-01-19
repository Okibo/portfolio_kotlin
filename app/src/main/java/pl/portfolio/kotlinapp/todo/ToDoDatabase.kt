package pl.portfolio.kotlinapp.todo

import android.content.ContentValues
import android.content.Context
import android.text.TextUtils
import android.util.Log

private const val TAG = "ToDoDatabase"
class ToDoDatabase(private val context: Context) {
    private val database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE,null)

    init{
        var sql = "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY NOT NULL, $COL_ISCHECKED integer, $COL_TITLE text, $COL_NOTE text)"
        database.execSQL(sql)

        Log.d(TAG, "Database $DATABASE_NAME with table $TABLE_NAME connected")
    }

    fun addEntry(title: String, note: String) : ToDoEntry{
        val values = ContentValues().apply {
            put(COL_ISCHECKED,0)
            put(COL_TITLE,title)
            put(COL_NOTE, note)
        }

        val generatedId = database.insert("$TABLE_NAME",null, values)

        Log.d(TAG, "addEntry: new entry with id: $generatedId was added")

        return ToDoEntry(generatedId,false,title,note)
    }

    fun updateEntry(entry: ToDoEntry){
        val values = ContentValues().apply {
            put(COL_ISCHECKED,if (entry.isChecked) 1 else 0)
            put(COL_TITLE,entry.title)
            put(COL_NOTE, entry.note)
        }

        database.update("$TABLE_NAME", values, "$COL_ID = ?", arrayOf(entry.id.toString()))

        Log.d(TAG, "updateEntry: entry with id ${entry.id} has been updated")
    }

    fun deleteEntries(entriesIds: ArrayList<String>){
        database.execSQL("DELETE FROM $TABLE_NAME WHERE $COL_ID IN (${TextUtils.join(",", entriesIds)})")
        Log.d(TAG, "deleteEntry: entries with ids: ${entriesIds.joinToString()} has been deleted")
    }

    fun getAllEntries(): ArrayList<ToDoEntry>{
        val result = arrayListOf<ToDoEntry>()
        val query = database.rawQuery("SELECT * FROM $TABLE_NAME", null)
        query.use {
            while(it.moveToNext()){
                with(it){
                    val id = getLong(0)
                    val isChecked:Boolean = (getInt(1) == 1)
                    val title = getString(2)
                    val note = getString(3)
                    result.add(ToDoEntry(id,isChecked,title,note))
                }
            }
        }
        return result
    }

    fun closeConnection(){
        database.close()
        Log.d(TAG, "closeConnection: DataBase connection closed")
    }

    companion object {
        val DATABASE_NAME = "todo.db"
        val TABLE_NAME = "todoentries"
        val COL_ID = "_id"
        val COL_ISCHECKED = "ischecked"
        val COL_TITLE = "title"
        val COL_NOTE = "note"
    }
}