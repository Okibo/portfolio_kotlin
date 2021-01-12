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
    private val MAX_MOVES: Int = 9
    private val TRANSLATION_VALUE: Int = 1000
    private val ANIMATION_DURATION: Int = 1000
    private val TTT_MODEL_KEY: String = "ttt_game_model_instance"
    private val TTT_FIRST_PLAYER_KEY: String = "ttt_first_player"
    private val TTT_GAME_WON_KEY: String = "ttt_won_game"
    private val TTT_VS_BOT_KEY: String = "ttt_vs_bot"
    private val TTT_MOVE_COUNTER_KEY: String = "ttt_move_counter"
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
        title = "Tic Tac Toe"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val resultLayout = findViewById<ConstraintLayout>(R.id.ttt_result_layout)
        val buttonsLayout = findViewById<LinearLayout>(R.id.ttt_bottom_layout)
        resultLayout.translationY = (TRANSLATION_VALUE * -1).toFloat()
        buttonsLayout.translationY = TRANSLATION_VALUE.toFloat()

        populateTokenArray()

        if (savedInstanceState != null) {
            gameModel = savedInstanceState.getParcelable(TTT_MODEL_KEY)
            firstPlayer = savedInstanceState.getBoolean(TTT_FIRST_PLAYER_KEY)
            wonGame = savedInstanceState.getBoolean(TTT_GAME_WON_KEY)
            vsBot = savedInstanceState.getBoolean(TTT_VS_BOT_KEY)
            moveCounter = savedInstanceState.getInt(TTT_MOVE_COUNTER_KEY)
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
        out.putParcelable(TTT_MODEL_KEY, gameModel)
        out.putBoolean(TTT_FIRST_PLAYER_KEY, firstPlayer)
        out.putBoolean(TTT_GAME_WON_KEY, wonGame)
        out.putBoolean(TTT_VS_BOT_KEY, vsBot)
        out.putInt(TTT_MOVE_COUNTER_KEY, moveCounter)
        super.onSaveInstanceState(out)
    }

    private fun populateTokenArray() {
        tokenArray[0][0] = findViewById<ImageView>(R.id.token00)
        tokenArray[0][1] = findViewById<ImageView>(R.id.token01)
        tokenArray[0][2] = findViewById<ImageView>(R.id.token02)
        tokenArray[1][0] = findViewById<ImageView>(R.id.token10)
        tokenArray[1][1] = findViewById<ImageView>(R.id.token11)
        tokenArray[1][2] = findViewById<ImageView>(R.id.token12)
        tokenArray[2][0] = findViewById<ImageView>(R.id.token20)
        tokenArray[2][1] = findViewById<ImageView>(R.id.token21)
        tokenArray[2][2] = findViewById<ImageView>(R.id.token22)
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
                    pair = Pair<Int, Int>(row, column)
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
        if (vsBot && moveCounter < MAX_MOVES) {
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
            alpha.duration = ANIMATION_DURATION.toLong()
            alpha.fillAfter = true
            target.setImageResource(if (firstPlayer) R.drawable.tictactoe_yellow else R.drawable.tictactoe_red)
            target.startAnimation(alpha)
            moveCounter++
            checkMove()
        }
    }

    private fun checkMove() {
        wonGame = gameModel!!.checkIfWon()
        if (!wonGame && moveCounter == MAX_MOVES) {
            showResult()
        } else if (wonGame) {
            showResult()
        } else {
            firstPlayer = !firstPlayer
        }
    }

    fun restartGame(view: View?) {
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
        resultLayout.animate().translationYBy((TRANSLATION_VALUE * -1).toFloat()).duration = duration.toLong()
        buttonsLayout.animate().translationYBy(TRANSLATION_VALUE.toFloat()).duration = duration.toLong()
        gameModel!!.resetBoard()
    }

    fun goBack(view: View?) {
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
        resultLayout.animate().translationYBy(TRANSLATION_VALUE.toFloat())
            .setDuration(ANIMATION_DURATION.toLong())
        buttonsLayout.animate().translationYBy((TRANSLATION_VALUE * -1).toFloat())
            .setDuration(ANIMATION_DURATION.toLong())
    }
}