package pl.portfolio.kotlinapp.mainmenu

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import pl.portfolio.kotlinapp.R
import pl.portfolio.kotlinapp.tictactoe.TicTacToeMenuActivity

private var applicationMap = mutableListOf<MainMenuItem>()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Portfolio"

        val appListView = findViewById<ListView>(R.id.appListView)
        applicationMap.clear()
        applicationMap.add(MainMenuItem("Simple Game",Intent(this, TicTacToeMenuActivity::class.java)))

        appListView.adapter = MainMenuAdapter(this, applicationMap)
    }

    private class MainMenuAdapter(private val context: Context, private val appList: List<MainMenuItem>): BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(context)
            val row = layoutInflater.inflate(R.layout.menu_record, parent, false)
            val button = row.findViewById<Button>(R.id.recordBtn)

            button.text = appList.get(position).content
            button.setOnClickListener(){
                context.startActivity(appList.get(position).intent)
            }
            return row
        }

        override fun getItem(position: Int): Any {
            return "Test string"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return applicationMap.count()
        }
    }
}