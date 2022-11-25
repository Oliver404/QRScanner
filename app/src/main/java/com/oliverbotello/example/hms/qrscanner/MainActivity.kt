package com.oliverbotello.example.hms.qrscanner

import android.Manifest
import android.content.Intent
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
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import com.oliverbotello.example.hms.qrscanner.utils.REQUEST_SCAN_QR


class MainActivity : AppCompatActivity(), View.OnClickListener, ActivityResultCallback<Boolean> {
    // Components
    private lateinit var btnScan: FloatingActionButton
    private lateinit var txtVwResult: TextView
    // Vars
    private val requestPermissionLauncher = registerForActivityResult(RequestPermission(), this)
    private val options = HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE)
        .setViewType(1)
        .create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            // if result is for QR Code
            if (requestCode == REQUEST_SCAN_QR) {
                val qrCodeResult: HmsScan? = data.getParcelableExtra(ScanUtil.RESULT)

                setResult(qrCodeResult?.showResult)
            }
        }
    }

    private fun initView() {
        txtVwResult = findViewById(R.id.txtvw_result)
        btnScan = findViewById(R.id.fltbtn_scan_qr)

        btnScan.setOnClickListener(this)
    }

    private fun setResult(result: String?) {
        txtVwResult.text = result?:"No Data"
    }

    private fun validatePermission(): Boolean =
        ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED

    private fun scanQrCode() {
        ScanUtil.startScan(this, REQUEST_SCAN_QR, options)
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