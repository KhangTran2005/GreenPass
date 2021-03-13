package com.example.greenpass.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.greenpass.R
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import kotlinx.android.synthetic.main.fragment_o_c_r.*


class OCR : Fragment() {

    private lateinit var mCameraSource: CameraSource
    private lateinit var textRecognizer: TextRecognizer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_o_c_r, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //create Text recognizer stuff
        textRecognizer = TextRecognizer.Builder(requireContext()).build()
        if (!textRecognizer.isOperational) {
            Toast.makeText(requireContext(), "Dependencies are not loaded yet...please try after few moment!!", Toast.LENGTH_SHORT).show()
            return
        }
        else{
            Log.d("debug", "Dependencies are loaded")
        }
        //  Init camera source to use high resolution and auto focus
        mCameraSource = CameraSource.Builder(requireContext(), textRecognizer)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedPreviewSize(1280, 1024)
            .setAutoFocusEnabled(true)
            .setRequestedFps(2.0f)
            .build()
        surface_camera_preview.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
            }
            override fun surfaceDestroyed(holder: SurfaceHolder) {
                mCameraSource.stop()
            }
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (isCameraPermissionGranted()) {
                        mCameraSource.start(surface_camera_preview.holder)
                    } else {
                        requestForPermission()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error:  ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        setUpDetectorProcessor()
    }

    private fun setUpDetectorProcessor(){
        textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
                val items = detections.detectedItems

                if (items.size() <= 0) {
                    return
                }

                tv_result.post {
                    val stringBuilder = StringBuilder()
                    for (i in 0 until items.size()) {
                        val item = items.valueAt(i)
                        stringBuilder.append(item.value)
                        stringBuilder.append("\n")
                    }
                    tv_result.text = stringBuilder.toString()
                    Log.d("debug", stringBuilder.toString())
                }
            }
        })
    }

    private fun isCameraPermissionGranted() : Boolean{
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestForPermission(){
        requestPermissions(arrayOf(Manifest.permission.CAMERA), 10)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            10 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(),
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show()

                    val refresh = OCRDirections.refresh()
                    findNavController().navigate(refresh)

                } else {
                    Toast.makeText(requireContext(),
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}