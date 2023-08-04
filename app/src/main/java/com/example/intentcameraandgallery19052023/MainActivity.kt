package com.example.intentcameraandgallery19052023

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private var btnCamera: Button? = null
    private var btnGallery: Button? = null
    private var img: ImageView? = null

    private var REQUEST_CODE_CAMERA = 123;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGallery = findViewById(R.id.buttonGallery)
        btnCamera = findViewById(R.id.buttonCamera)
        img = findViewById(R.id.imageView)

        btnCamera?.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    requestPermissions(
                        arrayOf(Manifest.permission.CAMERA),
                        REQUEST_CODE_CAMERA
                    )
                } else {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
            } else {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                resultActivityCamera.launch(intent)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CAMERA) {
            val resultCamera = grantResults.getOrNull(0)
            if (resultCamera == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                resultActivityCamera.launch(intent)
            }
        }
    }

    private val resultActivityCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK && it.data != null) {
            val bitmap = it.data?.extras?.get("data") as Bitmap
            img?.setImageBitmap(bitmap)
        }
    }
}