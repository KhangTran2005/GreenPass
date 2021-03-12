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
import kotlinx.android.synthetic.main.fragment_cases_dialog.*

class CasesDialog(
        var cases: Int,
        var deaths: Int,
        var recovered: Int
) : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (getDialog() != null && getDialog()?.getWindow() != null) {
            getDialog()?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            getDialog()?.getWindow()?.requestFeature(Window.FEATURE_NO_TITLE);
        }
        return inflater.inflate(R.layout.fragment_cases_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Tactive_tv.text = cases.toString()
        Tdeaths_tv.text = deaths.toString()
        Trecovered_tv.text = recovered.toString()
    }

    companion object{
        const val TAG = "cases_dialog"
    }
}