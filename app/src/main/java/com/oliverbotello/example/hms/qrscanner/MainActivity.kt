package com.oliverbotello.example.hms.qrscanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity(), View.OnClickListener, ActivityResultCallback<Boolean> {
    private lateinit var btnScan: FloatingActionButton
    private lateinit var txtVwResult: TextView
    private val requestPermissionLauncher = registerForActivityResult(RequestPermission(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        txtVwResult = findViewById(R.id.txtvw_result)
        btnScan = findViewById(R.id.fltbtn_scan_qr)

        btnScan.setOnClickListener(this)
    }

    private fun validatePermission(): Boolean =
        ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED

    private fun scanQrCode() {
        Snackbar.make(
            btnScan,
            "Read QR Code",
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    override fun onClick(v: View?) {
        if (validatePermission()) scanQrCode()
        else requestPermission()
    }

    override fun onActivityResult(result: Boolean) {
        if (result)
            scanQrCode()
        else
            Snackbar.make(
                btnScan,
                "Access to camera is necessary",
                Snackbar.LENGTH_LONG
            ).show()
    }
}