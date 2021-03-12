package com.example.infocovid.activities


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.greenpass.R
import kotlinx.android.synthetic.main.fragment_pie_dialog.*

class PieDialog(
        private var active: Int,
        private var deaths: Int,
        private var recovered: Int
) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (getDialog() != null && getDialog()?.getWindow() != null) {
            getDialog()?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            getDialog()?.getWindow()?.requestFeature(Window.FEATURE_NO_TITLE);
        }
        return inflater.inflate(R.layout.fragment_pie_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        active_tv.text = "${active}"
        deaths_tv.text = "${deaths}"
        recovered_tv.text = "${recovered}"
    }

    companion object{
        const val TAG = "pie_dialog"
    }
}