package com.github.wille1101.thirty.model

import androidx.lifecycle.ViewModel

class ScoreBoardViewModel : ViewModel() {
    private var scoreBoard: ScoreBoard = ScoreBoard()


    fun addScoreBoard(scoreBoard: ScoreBoard) {
        this.scoreBoard = scoreBoard
    }

    fun getScoreBoard(): ScoreBoard {
        return scoreBoard
    }
}