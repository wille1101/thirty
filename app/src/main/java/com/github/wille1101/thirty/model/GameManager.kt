package com.github.wille1101.thirty.model

import android.os.Parcel
import android.os.Parcelable

class GameManager() : Parcelable {
    var dicesSelected = mutableListOf(false, false, false, false, false, false)
    private var diceRoll = mutableListOf(0, 0, 0, 0, 0, 0)
    var nrOfRolls = 0
    var nrOfRounds = 1
    var currentPoints = 0
    var currentChoice = ""
    private var validChoice = true
    var scoreBoard = ScoreBoard()
    lateinit var choiceArray: MutableList<String>
    var validPoints = 0

    constructor(parcel: Parcel) : this() {
        for (i in 0..5) {
            dicesSelected[i] = parcel.readByte() != 0.toByte()
        }
        for (i in 0..5) {
            diceRoll[i] = parcel.readInt()
        }

        nrOfRolls = parcel.readInt()
        nrOfRounds = parcel.readInt()
        currentPoints = parcel.readInt()
        currentChoice = parcel.readString().toString()
        validChoice = parcel.readByte() != 0.toByte()

        for (i in 1..parcel.readInt()) {
            parcel.readString()?.let { scoreBoard.addChoice(it, parcel.readInt()) }
        }

        choiceArray = MutableList(parcel.readInt()) {" "}
        for (i in 0 until choiceArray.size) {
            choiceArray[i] = parcel.readString().toString()
        }

        validPoints = parcel.readInt()
    }

    /**
     * Function to reset the dices, add the selection to the score board and begin a new round.
     * Only if the current choice is valid. If not, the function does nothing.
     * @return true if a new round was started, otherwise false
     */
    fun nextRound(): Boolean {
        if (validChoice) {
            scoreBoard.addChoice(currentChoice, currentPoints)

            if (nrOfRounds < 10) {
                choiceArray.remove(currentChoice)
                nrOfRolls = 0
                currentPoints = 0

                resetDicesSelected()
                rollDices()

                nrOfRounds++
            }

            return true
        } else {
            return false
        }

    }

    /**
     * Rolls the dices that are not selected
     */
    fun rollDices() {
        if (nrOfRolls < 3 && dicesSelected.contains(false)) {
            generateDiceRoll()
        }

    }

    /**
     * Generates a new dice roll, re-rolling all un-selected dices. Also adds to the number of rolls.
     */
    private fun generateDiceRoll() {
        val tempDiceRoll = DiceRoll().getDices()

        for (i in 0..5) {
            if (!dicesSelected[i]) {
                diceRoll[i] = tempDiceRoll[i]
            }
        }

        nrOfRolls++
    }

    /**
     * Returns the dice value of the specified dice
     * @param dice to check the value of.
     */
    fun getDiceRoll(dice: Int): Int {
        return diceRoll[dice]
    }

    /**
     * Iterates through all selected dices checking if the selection is valid,
     * based on the dice values and if the choice already has been used.
     * @return true if the selection is valid, otherwise false
     */
    fun iterateDicesCheckingChoiceIsValid(): Boolean {
        if (!isAnyDiceSelected()) {
            validChoice = true
            return validChoice
        }

        val choice: Int = if (currentChoice == "Low") {
            3
        } else {
            currentChoice.toInt()
        }

        val selectedDiceValues = mutableListOf<Int>()

        for (i in 0..5) {
            if (dicesSelected[i]) {
                if (choice == 3) {
                    if (diceRoll[i] <= choice && !scoreBoard.containsChoice("Low")) {
                        validChoice = true
                    } else {
                        validChoice = false
                        break
                    }
                } else {
                    if (diceRoll[i] <= choice && !scoreBoard.containsChoice(choice.toString())) {
                        selectedDiceValues.add(diceRoll[i])
                    } else {
                        validChoice = false
                    }

                }

            }
        }

        if (choice == 3) {
            return validChoice
        }

        validPoints = 0
        selectedDiceValues.sortDescending()
        diceCheck(selectedDiceValues, choice, 0)
        validChoice = validPoints > 0 && validPoints % choice == 0 && validPoints == currentPoints

        return validChoice
    }

    /**
     * Recursive function to add together valid dice groups that equal the given choice.
     * The variable validPoints will be set to be equal to the amount of points which is given by
     * valid pairs in the current dice selection.
     * @param selectedDiceValues The values of the selected dices, sorted biggest to lowest value
     * @param choice The current choice
     * @param pointSum Keeps track of the current dice sum. If equal to choice, choice is added to
     * the temporary current points.
     */
    private fun diceCheck(selectedDiceValues: MutableList<Int>, choice: Int, pointSum: Int) {
        var tempPointSum = pointSum
        if (tempPointSum == choice) {
            validPoints += choice
            return
        }

        var index = 0
        while (index < selectedDiceValues.size) {

            // If the selected dice can be used to reach choice, add it to tempPoints,
            // remove it from the dice list and keep going with the new values.
            if (tempPointSum + selectedDiceValues[index] <= choice) {
                tempPointSum += selectedDiceValues[index]
                selectedDiceValues.removeAt(index)
                diceCheck(selectedDiceValues, choice, tempPointSum)

                tempPointSum = 0
                index = 0
            } else {
                index++
            }
        }

    }

    /**
     * Selects a dice and adds its value to the current points. Returns true if the dice
     * was selected, if the dice was de-selected returns false.
     * @param dice The dice to select.
     * @return true if the dice was selected, false if the dice was de-selected.
     */
    fun selectDice(dice: Int): Boolean {
        if (!dicesSelected[dice]) {
            currentPoints += diceRoll[dice]
            dicesSelected[dice] = true
            return true
        } else {
            currentPoints -= diceRoll[dice]
            dicesSelected[dice] = false
            return false
        }

    }

    /**
     * Checks if any of the dices are selected.
     * @return true if any dice is selected, otherwise false.
     */
    private fun isAnyDiceSelected(): Boolean {
        return dicesSelected.contains(true)
    }

    /**
     * Deselects all the dices.
     */
    private fun resetDicesSelected() {
        for (i in 0..5) {
            dicesSelected[i] = false
        }
    }

    /**
     * Parcel functions
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        for (i in 0..5) {
            parcel.writeByte(if (dicesSelected[i]) 1 else 0)
        }
        for (i in 0..5) {
            parcel.writeInt(diceRoll[i])
        }

        parcel.writeInt(nrOfRolls)
        parcel.writeInt(nrOfRounds)
        parcel.writeInt(currentPoints)
        parcel.writeString(currentChoice)
        parcel.writeByte(if (validChoice) 1 else 0)

        parcel.writeInt(scoreBoard.scoreBoard.size)
        for ((key, value) in scoreBoard.scoreBoard) {
            parcel.writeString(key)
            parcel.writeInt(value)
        }

        parcel.writeInt(choiceArray.size)
        for (i in 0 until choiceArray.size) {
            parcel.writeString(choiceArray[i])
        }

        parcel.writeInt(validPoints)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GameManager> {
        override fun createFromParcel(parcel: Parcel): GameManager {
            return GameManager(parcel)
        }

        override fun newArray(size: Int): Array<GameManager?> {
            return arrayOfNulls(size)
        }
    }

}