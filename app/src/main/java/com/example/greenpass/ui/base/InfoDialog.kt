package com.example.greenpass.ui.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.greenpass.R
import kotlinx.android.synthetic.main.info_dialog.*

class InfoDialog(
        var title: String,
        var content: String
) : DialogFragment() {
    private var mCallback: OnDialogDismissListener? = null

    interface OnDialogDismissListener{
        fun onDialogDismissListener()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            mCallback = activity as OnDialogDismissListener
        }
        catch(e: ClassCastException){
            throw java.lang.ClassCastException(activity.toString() + "must implement OnDialogDismissListener")
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if (getDialog() != null && getDialog()?.getWindow() != null) {
            getDialog()?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            getDialog()?.getWindow()?.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return inflater.inflate(R.layout.info_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog_title.text = title
        dialog_content.text = content
    }

    companion object{
        const val TAG = "info_dialog"
    }
}