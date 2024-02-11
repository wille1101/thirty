package com.github.wille1101.thirty.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.github.wille1101.thirty.R

class NotValidChoiceDialog: DialogFragment() {

    /**
     * Creates a dialog with the message that the selected choice is not valid.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.dialog_not_valid_choice)
                .setPositiveButton(R.string.dialog_ok_button) { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
