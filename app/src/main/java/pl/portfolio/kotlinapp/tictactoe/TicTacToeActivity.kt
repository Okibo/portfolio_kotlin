package pl.portfolio.kotlinapp.tictactoe

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import pl.portfolio.kotlinapp.R

class TicTacToeActivity: AppCompatActivity() {
    private val maxMoves: Int = 9
    private val translationValue: Float = 1000f
    private val animationDuration: Long = 1000
    private val fadeOut = 0f
    private val fadeIn = 1f
    private var vsBot = false
    private var firstPlayer = true
    private var wonGame = false
    private var moveCounter = 0
    private var gameModel: TicTacToeModel? = null
    private val tokenArray = Array(3) {arrayOfNulls<ImageView>(3)}

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tic_tac_toe)
        title = getString(R.string.ttt_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val resultLayout = findViewById<ConstraintLayout>(R.id.ttt_result_layout)
        val buttonsLayout = findViewById<LinearLayout>(R.id.ttt_bottom_layout)
        resultLayout.translationY = (translationValue * -1)
        buttonsLayout.translationY = translationValue

        populateTokenArray()

        if (savedInstanceState != null) {
            gameModel = savedInstanceState.getParcelable(getString(R.string.ttt_model_key))
            firstPlayer = savedInstanceState.getBoolean(getString(R.string.ttt_first_player_key))
            wonGame = savedInstanceState.getBoolean(getString(R.string.ttt_game_won_key))
            vsBot = savedInstanceState.getBoolean(getString(R.string.ttt_vs_bot_key))
            moveCounter = savedInstanceState.getInt(getString(R.string.ttt_move_counter_key))
            displayCheckedTokens()
            checkMove()
        } else {
            val vsBotKey = resources.getString(R.string.ttt_vs_bot_key)
            val callingIntent = intent
            vsBot = callingIntent.getBooleanExtra(vsBotKey, false)
            gameModel = TicTacToeModel()
        }
    }

    override fun onSaveInstanceState(out: Bundle) {
        out.putParcelable(getString(R.string.ttt_model_key), gameModel)
        out.putBoolean(getString(R.string.ttt_first_player_key), firstPlayer)
        out.putBoolean(getString(R.string.ttt_game_won_key), wonGame)
        out.putBoolean(getString(R.string.ttt_vs_bot_key), vsBot)
        out.putInt(getString(R.string.ttt_move_counter_key), moveCounter)
        super.onSaveInstanceState(out)
    }

    private fun populateTokenArray() {
        tokenArray[0][0] = findViewById(R.id.token00)
        tokenArray[0][1] = findViewById(R.id.token01)
        tokenArray[0][2] = findViewById(R.id.token02)
        tokenArray[1][0] = findViewById(R.id.token10)
        tokenArray[1][1] = findViewById(R.id.token11)
        tokenArray[1][2] = findViewById(R.id.token12)
        tokenArray[2][0] = findViewById(R.id.token20)
        tokenArray[2][1] = findViewById(R.id.token21)
        tokenArray[2][2] = findViewById(R.id.token22)
    }

    private fun displayCheckedTokens() {
        val yellowToken = gameModel!!.getYellowToken().toChar()
        val redToken = gameModel!!.getRedToken().toChar()
        for (row in 0..2) {
            for (column in 0..2) {
                val token = gameModel!!.getToken(row, column).toChar()
                if (token.toInt() != 0) {
                    val tokenImg = tokenArray[row][column]!!
                    val alpha = AlphaAnimation(fadeOut, fadeIn)
                    alpha.duration = 0
                    alpha.fillAfter = true
                    if (token == yellowToken) {
                        tokenImg.setImageResource(R.drawable.tictactoe_yellow)
                    } else if (token == redToken) {
                        tokenImg.setImageResource(R.drawable.tictactoe_red)
                    }
                    tokenImg.startAnimation(alpha)
                }
            }
        }
    }

    fun showToken(view: View) {
        val target = view as ImageView
        var pair: Pair<Int, Int>? = null
        for (row in tokenArray.indices) {
            for (column in 0 until tokenArray[row].count()) {
                if (tokenArray[row][column] === target) {
                    pair = Pair(row, column)
                    break
                }
            }
        }
        val isSpotTaken = gameModel!!.isSpotTaken(pair)
        if (isSpotTaken && !wonGame) {
            Toast.makeText(applicationContext, R.string.ttt_already_checked, Toast.LENGTH_SHORT)
                .show()
            return
        }
        gameModel!!.makeMove(firstPlayer, pair)
        animateMove(target)
        if (vsBot && moveCounter < maxMoves) {
            val botPair: Pair<Int, Int>? = gameModel!!.botMove()
            val row: Int = botPair!!.first
            val column: Int = botPair.second
            val botTarget = tokenArray[row][column]!!
            animateMove(botTarget)
        }
    }

    private fun animateMove(target: ImageView) {
        if (!wonGame) {
            val alpha = AlphaAnimation(fadeOut, fadeIn)
            alpha.duration = animationDuration
            alpha.fillAfter = true
            target.setImageResource(if (firstPlayer) R.drawable.tictactoe_yellow else R.drawable.tictactoe_red)
            target.startAnimation(alpha)
            moveCounter++
            checkMove()
        }
    }

    private fun checkMove() {
        wonGame = gameModel!!.checkIfWon()
        if (!wonGame && moveCounter == maxMoves) {
            showResult()
        } else if (wonGame) {
            showResult()
        } else {
            firstPlayer = !firstPlayer
        }
    }

    fun restartGame(view: View) {
        val duration = 200
        for (row in 0..2) {
            for (column in 0..2) {
                val token = tokenArray[row][column]!!
                val currentAlpha = token.alpha
                if (currentAlpha == 1f) {
                    val alpha = AlphaAnimation(fadeIn, fadeOut)
                    alpha.duration = duration.toLong()
                    alpha.fillAfter = true
                    token.startAnimation(alpha)
                }
            }
        }
        firstPlayer = true
        wonGame = false
        moveCounter = 0

        val resultLayout = findViewById<ConstraintLayout>(R.id.ttt_result_layout)
        val buttonsLayout = findViewById<LinearLayout>(R.id.ttt_bottom_layout)
        resultLayout.animate().translationYBy((translationValue * -1)).duration = duration.toLong()
        buttonsLayout.animate().translationYBy(translationValue).duration = duration.toLong()
        gameModel!!.resetBoard()
    }

    fun goBack(view: View) {
        finish()
    }

    private fun showResult() {
        val token = findViewById<ImageView>(R.id.ttt_winner_token)
        val resultLbl = findViewById<TextView>(R.id.ttt_result)
        val winner = findViewById<TextView>(R.id.ttt_winner)
        val resultLayout = findViewById<ConstraintLayout>(R.id.ttt_result_layout)
        val buttonsLayout = findViewById<LinearLayout>(R.id.ttt_bottom_layout)

        if (wonGame) {
            val imgRes = if (firstPlayer) R.drawable.tictactoe_yellow else R.drawable.tictactoe_red
            val winnerRes = if (firstPlayer) R.string.ttt_yellow_player else R.string.ttt_red_player
            token.setImageResource(imgRes)
            winner.setText(winnerRes)
        } else {
            winner.text = ""
            val alpha = AlphaAnimation(fadeIn, fadeOut)
            alpha.duration = 0
            alpha.fillAfter = true
            token.startAnimation(alpha)
        }
        resultLbl.setText(if (wonGame) R.string.ttt_result_won else R.string.ttt_result_tie)
        resultLayout.animate().translationYBy(translationValue).duration = animationDuration
        buttonsLayout.animate().translationYBy((translationValue * -1)).duration = animationDuration
    }
}