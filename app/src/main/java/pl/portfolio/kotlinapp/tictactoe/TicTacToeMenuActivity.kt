package pl.portfolio.kotlinapp.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import pl.portfolio.kotlinapp.R
import pl.portfolio.kotlinapp.mainmenu.MainActivity
import pl.portfolio.kotlinapp.mainmenu.MainMenuItem

class TicTacToeMenuActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tic_tac_toe_menu)
        title = "Tic Tac Toe"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val vsHumanBtn = findViewById<Button>(R.id.ttt_vs_human_btn)
        val vsBotBtn = findViewById<Button>(R.id.ttt_vs_bot_btn)

        val listener = View.OnClickListener {
            val clicked = it as Button
            val vsBotKey: String = "ttt_vsHuman"
            val startGameIntent: Intent = Intent(this, TicTacToeActivity::class.java)

            startGameIntent.putExtra(vsBotKey,clicked == vsBotBtn)
            startActivity(startGameIntent)
        }

        vsHumanBtn.setOnClickListener(listener)
        vsBotBtn.setOnClickListener(listener)
    }
}