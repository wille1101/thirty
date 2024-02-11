package com.github.wille1101.thirty.controller

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.wille1101.thirty.R
import com.github.wille1101.thirty.databinding.FragmentScoreBinding
import com.github.wille1101.thirty.model.ScoreBoard
import com.github.wille1101.thirty.model.ScoreBoardViewModel

class ScoreFragment : Fragment() {
    private var _binding: FragmentScoreBinding? = null
    private val binding get() = _binding!!
    private var tablePointLabels: HashMap<String, TextView> = HashMap()
    private lateinit var scoreBoard: ScoreBoard

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_ScoreFragment_to_MainFragment)
            }
        })

        _binding = FragmentScoreBinding.inflate(inflater, container, false)

        createPointLabelsList()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scoreBoard = if (savedInstanceState != null) {
            if (Build.VERSION.SDK_INT >= 33) {
                savedInstanceState.getParcelable("SCORE_BOARD_PARCEL", ScoreBoard::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                savedInstanceState.getParcelable("SCORE_BOARD_PARCEL")!!
            }
        } else {
            val scoreBoardViewModel: ScoreBoardViewModel by activityViewModels()
            scoreBoardViewModel.getScoreBoard()
        }

        var totalPoints = 0
        for ((k, v) in scoreBoard.scoreBoard) {
            tablePointLabels[k]!!.text = v.toString()
            totalPoints += v
        }

        val totalScoreLabel = "Total score: $totalPoints points"
        binding.totalScoreLabel.text = totalScoreLabel

        binding.backToMainButton.setOnClickListener {
            findNavController().navigate(R.id.action_ScoreFragment_to_MainFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("SCORE_BOARD_PARCEL", scoreBoard)
    }

    /**
     * Creates a list of all the labels in the table that needs to be updated with the amount
     * of points.
     */
    private fun createPointLabelsList() {
        tablePointLabels["Low"] = binding.tablePointsLow
        tablePointLabels["4"] = binding.tablePoints4
        tablePointLabels["5"] = binding.tablePoints5
        tablePointLabels["6"] = binding.tablePoints6
        tablePointLabels["7"] = binding.tablePoints7
        tablePointLabels["8"] = binding.tablePoints8
        tablePointLabels["9"] = binding.tablePoints9
        tablePointLabels["10"] = binding.tablePoints10
        tablePointLabels["11"] = binding.tablePoints11
        tablePointLabels["12"] = binding.tablePoints12
    }

}