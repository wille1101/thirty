package com.github.wille1101.thirty.controller

import android.os.Build.VERSION
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.wille1101.thirty.R
import com.github.wille1101.thirty.databinding.FragmentGameBinding
import com.github.wille1101.thirty.model.GameManager
import com.github.wille1101.thirty.model.ScoreBoardViewModel
import com.github.wille1101.thirty.view.NotValidChoiceDialog

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var buttonsList: List<ImageButton>
    private val diceImgs = listOf(
        R.mipmap.dice1,
        R.mipmap.dice2,
        R.mipmap.dice3,
        R.mipmap.dice4,
        R.mipmap.dice5,
        R.mipmap.dice6
    )
    private var gm  = GameManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        gm.choiceArray = resources.getStringArray(R.array.choice_array).toMutableList()

        context?.let {
            ArrayAdapter(
                it,
                R.layout.spinner_item_style,
                gm.choiceArray
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.choiceSpinner.adapter = adapter
            }
        }


        createListOfButtons()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            gm = if (VERSION.SDK_INT >= 33) {
                savedInstanceState.getParcelable("GAME_MANAGER_PARCEL", GameManager::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                savedInstanceState.getParcelable("GAME_MANAGER_PARCEL")!!
            }

            updateSpinnerAdapter()
            setButtonImages()
            setAllLabels()

            for (i in 0..5) {
                if (gm.dicesSelected[i]) {
                    setButtonColor(buttonsList[i])
                } else {
                    removeButtonColor(buttonsList[i])
                }
            }

            if (gm.validPoints == gm.currentPoints) {
                binding.nextRoundButton.alpha = 1F
            } else {
                binding.nextRoundButton.alpha = 0.5F
            }

            if (gm.nrOfRolls == 3) {
                binding.rollButton.alpha = 0.5F
            } else {
                binding.rollButton.alpha = 1F
            }

        }


        binding.rollButton.setOnClickListener {
            gm.rollDices()
            setButtonImages()
            setRollLabel()

            if (gm.nrOfRolls == 3) {
                binding.rollButton.alpha = 0.5F
            }

        }

        binding.nextRoundButton.setOnClickListener {
            gm.currentChoice = binding.choiceSpinner.selectedItem as String

            if (gm.nrOfRounds == 10 && gm.nextRound()) {
                val scoreBoardViewModel: ScoreBoardViewModel by activityViewModels()
                scoreBoardViewModel.addScoreBoard(gm.scoreBoard)

                findNavController().navigate(R.id.action_GameFragment_to_ScoreFragment)
            } else if (gm.nextRound()) {
                updateSpinnerAdapter()
                resetDiceButtonsBackground()
                setButtonImages()
                setAllLabels()

                binding.rollButton.alpha = 1F
                binding.nextRoundButton.alpha = 1F

                if (gm.nrOfRounds == 10) {
                    val finishGameButton = getString(R.string.finish_button)
                    binding.nextRoundButton.text = finishGameButton
                }

            }  else {
                val alertDialog = NotValidChoiceDialog()
                alertDialog.show(parentFragmentManager, "AlertDialog")
            }

        }

        setDiceButtonListeners()

        if (gm.nrOfRolls == 0) {
            gm.rollDices()
            setButtonImages()
            setRollLabel()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("GAME_MANAGER_PARCEL", gm)
    }

    /**
     * Creates a new adapter for the spinner. When a choice is picked it's removed from the
     * choice array, so the spinner needs to be updated with this new smaller array.
     */
    private fun updateSpinnerAdapter() {
        val adapter = context?.let { ArrayAdapter(it, R.layout.spinner_item_style, gm.choiceArray) }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.choiceSpinner.adapter = adapter
    }

    /**
     * Creates a list of all the dice buttons in the view.
     */
    private fun createListOfButtons() {
        buttonsList = listOf(binding.diceButton1, binding.diceButton2, binding.diceButton3,
            binding.diceButton4, binding.diceButton5, binding.diceButton6)
    }

    /**
     * Gets the dice value of each dice and sets the corresponding image of that value
     */
    private fun setButtonImages() {
        for (i in 0..5) {
            buttonsList[i].setImageResource(diceImgs[gm.getDiceRoll(i)-1])
        }
    }

    /**
     * Sets the corresponding click listener for each dice button.
     */
    private fun setDiceButtonListeners() {
        for (i in 0..5) {
            buttonsList[i].setOnClickListener{

                if (gm.selectDice(i)) {
                    setButtonColor(buttonsList[i])
                } else {
                    removeButtonColor(buttonsList[i])
                }

                setPointsLabel()

                gm.currentChoice = binding.choiceSpinner.selectedItem as String
                if (gm.iterateDicesCheckingChoiceIsValid()) {
                    binding.nextRoundButton.alpha = 1F
                } else {
                    binding.nextRoundButton.alpha = 0.5F
                }
            }

        }
    }

    /**
     * Updates the round label with the current round number.
     */
    private fun setRoundLabel() {
        val roundLabel = "Round: ${gm.nrOfRounds}/10"
        binding.roundLabel.text = roundLabel
    }

    /**
     * Updates the roll label with the current roll number.
     */
    private fun setRollLabel() {
        val rollLabel = "Roll: ${gm.nrOfRolls}/3"
        binding.rollLabel.text = rollLabel
    }

    /**
     * Updates the points label with the current amount of points of the selection.
     */
    private fun setPointsLabel() {
        val pointsLabel = "${gm.currentPoints} points"
        binding.pointsLabel.text = pointsLabel
    }

    /**
     * Sets all the labels in the view with their current values.
     * Sets the round, roll and points label.
     */
    private fun setAllLabels() {
        setRoundLabel()
        setRollLabel()
        setPointsLabel()
    }

    /**
     * Removes the background color of all dice buttons. Used when a new round starts to remove
     * previous selections.
     */
    private fun resetDiceButtonsBackground() {
        for (i in 0..5) {
            removeButtonColor(buttonsList[i])
        }

    }

    /**
     * Sets the background color of a dice button. Used when a dice is selected.
     * @param btn The button to set the background of.
     */
    private fun setButtonColor(btn: ImageButton) {
        btn.setBackgroundResource(R.drawable.button_shadow_style)
    }

    /**
     * Sets the background color of a dice button to a transparent color,
     * removing the current background color. Used when a dice is de-selected.
     * @param btn The button to remove the background of.
     */
    private fun removeButtonColor(btn: ImageButton) {
        btn.setBackgroundResource(R.drawable.button_transparent_style)
    }

}
