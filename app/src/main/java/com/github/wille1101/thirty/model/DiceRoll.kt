package com.github.wille1101.thirty.model

class DiceRoll {
    private var rolledDices: MutableList<Int> = mutableListOf(0, 0, 0, 0, 0, 0)

    init {
        rollDices()
    }

    /**
     * Function to generate a new dice roll.
     */
    private fun rollDices() {
        for (i in 0..5) {
            rolledDices[i] = (1..6).random()
        }
    }

    /**
     * Function to return the list of dice rolls.
     * @return The list of dice rolls.
     */
    fun getDices(): MutableList<Int> {
        return rolledDices
    }

}