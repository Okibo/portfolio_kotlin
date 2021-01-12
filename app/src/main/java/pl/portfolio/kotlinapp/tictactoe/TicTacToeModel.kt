package pl.portfolio.kotlinapp.tictactoe

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class TicTacToeModel() : Parcelable{
    private var gameBoard: Array<ByteArray> = Array<ByteArray>(3) { ByteArray(3) }
    private val redToken: Byte = 2 //R
    private val yellowToken: Byte = 1 //Y
    private var mData = 0
    private val zeroByte: Byte = 0

    constructor(parcel: Parcel) : this() {
        mData = parcel.readInt()
    }

    fun botMove(): Pair<Int, Int>? {
        val rnd = Random()
        while (true) {
            val row = rnd.nextInt(3)
            val column = rnd.nextInt(3)
            if (gameBoard[row][column] == zeroByte) {
                gameBoard[row][column] = redToken
                return Pair(row, column)
            }
        }
    }

    fun resetBoard() {
        gameBoard = Array<ByteArray>(3) { ByteArray(3) }
    }

    fun isSpotTaken(pair: Pair<Int, Int>?): Boolean {
        val row: Int = pair!!.first
        val column: Int = pair.second
        return gameBoard[row][column] == redToken || gameBoard[row][column] == yellowToken
    }

    fun makeMove(firstPlayer: Boolean, pair: Pair<Int, Int>?) {
        val row: Int = pair!!.first
        val column: Int = pair.second
        gameBoard[row][column] = if (firstPlayer) yellowToken else redToken
    }

    fun checkIfWon(): Boolean {
        for (i in gameBoard.indices) {

            // Checking if row won
            if (gameBoard[i][0] == gameBoard[i][1] && gameBoard[i][1] == gameBoard[i][2] && gameBoard[i][0] != zeroByte && gameBoard[i][1] != zeroByte && gameBoard[i][2] != zeroByte) {
                return true
            }

            // Checking if column won
            if (gameBoard[0][i] == gameBoard[1][i] && gameBoard[1][i] == gameBoard[2][i] && gameBoard[0][i] != zeroByte && gameBoard[1][i] != zeroByte && gameBoard[2][i] != zeroByte) {
                return true
            }
        }

        // Checking if vertically from top left won
        if (gameBoard[0][0] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][2] && gameBoard[0][0] != zeroByte && gameBoard[1][1] != zeroByte && gameBoard[2][2] != zeroByte) {
            return true
        }

        // Checking if vertically from to right won
        if (gameBoard[0][2] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][0] && gameBoard[0][2] != zeroByte && gameBoard[1][1] != zeroByte && gameBoard[2][0] != zeroByte) {
             return true
        }

        return false
    }

    fun getRedToken(): Byte {
        return redToken
    }

    fun getYellowToken(): Byte {
        return yellowToken
    }

    fun getToken(row: Int, column: Int): Byte {
        return gameBoard[row][column]
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(mData)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TicTacToeModel> {
        override fun createFromParcel(parcel: Parcel): TicTacToeModel {
            return TicTacToeModel(parcel)
        }

        override fun newArray(size: Int): Array<TicTacToeModel?> {
            return arrayOfNulls(size)
        }
    }
}