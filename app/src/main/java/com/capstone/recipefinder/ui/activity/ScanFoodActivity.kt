package com.capstone.recipefinder.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.capstone.recipefinder.data.model.ResponsePredictItem
import com.capstone.recipefinder.data.preference.LoginPreference
import com.capstone.recipefinder.data.remote.ApiConfig
import com.capstone.recipefinder.databinding.ActivityScanFoodBinding
import com.capstone.recipefinder.utils.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ScanFoodActivity : AppCompatActivity() {

    private var image1: String? = null
    private var name1: String? = null
    private var kategori1: String? = null
    private var kkal1: String? = null
    private var karbohidrat1: String? = null
    private var protein1: String? = null
    private var lemak1: String? = null
    private var desc1: String? = null
    private var ingredients1: String? = null


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
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Get Food"
        PreferenceLogin = LoginPreference(this)

        binding.btnAddCamera.setOnClickListener { startTakePhoto() }
        binding.btnAddGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { addFood() }

    }
    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createFileTemp(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@ScanFoodActivity,
                "com.capstone.recipefinder.fileprovider",
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

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
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

    private fun addFood() {

        if (getFile != null) {
            val file = getreduceFileImage(getFile as File)
//            val descriptionText = binding.etAdd.text.toString()
//            val description = descriptionText.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageFile
            )

            val service = ApiConfig().GetApiService().uploadImage(imageMultipart)
            service.enqueue(object : Callback<ResponsePredictItem> {
                override fun onResponse(call: Call<ResponsePredictItem>, response: Response<ResponsePredictItem>) {
                    if (response.isSuccessful) {
                        val responseBody =response.body()
                        if (responseBody != null ) {
                            this@ScanFoodActivity.let {
                                image1 = responseBody.img
                                name1 = responseBody.name
                                kategori1 = responseBody.kategori
                                kkal1 = responseBody.kkal
                                karbohidrat1 = responseBody.karbohidrat
                                protein1 = responseBody.protein
                                lemak1 = responseBody.lemak
                                desc1 = responseBody.description
                                ingredients1 = responseBody.ingredients
                                DetailPredictActivity()
                            }
                        }
                    }else{
                        Toast.makeText(this@ScanFoodActivity, "Gambar Berhasil di Upload",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponsePredictItem>, t: Throwable) {
                    Toast.makeText(this@ScanFoodActivity, "Gagal instance retrofit",Toast.LENGTH_SHORT).show()
                }

            })
        } else {
            Toast.makeText(this@ScanFoodActivity, "Silahkan masukkan gambar terlebih dahulu.",Toast.LENGTH_SHORT).show()
        }
    }
    private fun DetailPredictActivity() {
        val intent = Intent(this@ScanFoodActivity, DetailPredictActivity::class.java)
        intent.putExtra("imgage1", image1)
        intent.putExtra("name1", name1)
        intent.putExtra("kategori1", kategori1)
        intent.putExtra("kkal1", kkal1)
        intent.putExtra("karbohidrat1", karbohidrat1)
        intent.putExtra("protein1", protein1)
        intent.putExtra("lemak1", lemak1)
        intent.putExtra("description1", desc1)
        intent.putExtra("ingredients1", ingredients1)
        startActivity(intent)
    }

}

