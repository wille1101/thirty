package com.github.wille1101.thirty.model

import android.os.Parcel
import android.os.Parcelable

class ScoreBoard() : Parcelable {
    var scoreBoard: HashMap<String, Int> = HashMap()

    constructor(parcel: Parcel) : this() {
        for (i in 1..parcel.readInt()) {
            parcel.readString()?.let { addChoice(it, parcel.readInt()) }
        }
    }

    /**
     * Adds a choice to the scoreboard.
     * @param choice The name of the choice.
     * @param points The number of points for the given choice.
     */
    fun addChoice(choice: String, points: Int) {
        scoreBoard[choice] = points
    }

    /**
     * Checks if the score board contains the provided choice.
     * @param choice The choice to check.
     * @return True if the choice is in the score board, otherwise false.
     */
    fun containsChoice(choice: String): Boolean {
        return scoreBoard.containsKey(choice)
    }


    /**
     * Parcel functions
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(scoreBoard.size)
        for ((key, value) in scoreBoard) {
            parcel.writeString(key)
            parcel.writeInt(value)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScoreBoard> {
        override fun createFromParcel(parcel: Parcel): ScoreBoard {
            return ScoreBoard(parcel)
        }

        override fun newArray(size: Int): Array<ScoreBoard?> {
            return arrayOfNulls(size)
        }
    }

}