package com.capstone.recipefinder.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.capstone.recipefinder.R
import com.capstone.recipefinder.data.model.ResponseScanFood
import com.capstone.recipefinder.data.preference.LoginPreference
import com.capstone.recipefinder.data.remote.ApiConfig
import com.capstone.recipefinder.databinding.ActivityScanFoodBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ScanFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanFoodBinding
    private lateinit var currentPhotoPath: String
    private lateinit var PreferenceLogin: LoginPreference
    private var getFile: File? = null

    companion object {

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!getallPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun getallPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddGallery.setOnClickListener { getstartGallery() }
        binding.btnAddCamera.setOnClickListener { getstartTakePhoto() }

        supportActionBar?.title = "App Story"
        PreferenceLogin = LoginPreference(this)

        binding.btnUpload.setOnClickListener {
            getmoreStories()
            AlertDialog.Builder(this@ScanFoodActivity).apply {
                setMessage("Successfully Added")
                setPositiveButton("next") { _, _ ->
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(
                        intent,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this@ScanFoodActivity as Activity)
                            .toBundle()
                    )
                    finish()
                }
                create()
                show()
            }
        }

    }

    private fun getstartGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "images/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun getstartTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createFileTemp(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@ScanFoodActivity,
                "com.capstone.recipefinder",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.ivGallery.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@ScanFoodActivity)

            getFile = myFile

            binding.ivGallery.setImageURI(selectedImg)
        }
    }



    private fun getmoreStories() {

        if (getFile != null) {
            val file = getreduceFileImage(getFile as File)
            val descriptionText = binding.etAdd.text.toString()
            val description = descriptionText.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val token = "Bearer ${PreferenceLogin.getUser().token}"
            val service = ApiConfig().getApiService().uploadImage(token, imageMultipart, description)
            service.enqueue(object : Callback<ResponseScanFood> {
                override fun onResponse(call: Call<ResponseScanFood>, response: Response<ResponseScanFood>) {
                    if (response.isSuccessful) {
                        val responseBody =response.body()
                        if (responseBody != null && !responseBody.error) {
                            Toast.makeText(applicationContext, "Gambar Berhasil diupload", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Upload Gagal", Toast.LENGTH_SHORT).show()

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseScanFood>, t: Throwable) {
                    Toast.makeText(applicationContext, "Upload Error", Toast.LENGTH_SHORT).show()

                }

            })
        } else {
            Toast.makeText(applicationContext, "Berhasil Upload", Toast.LENGTH_SHORT).show()

        }
    }

}