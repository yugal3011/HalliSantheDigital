package com.hallisanthe.digital

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.storage.FirebaseStorage
import com.hallisanthe.digital.databinding.ActivityUploadBinding
import com.hallisanthe.digital.firebase.FirestoreManager
import com.hallisanthe.digital.models.Product
import com.hallisanthe.digital.utils.ImageCompressor
import com.hallisanthe.digital.utils.PermissionHelper
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.*

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            binding.ivProductPreview.setImageURI(selectedImageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPickImage.setOnClickListener {
            if (PermissionHelper.hasPermissions(this)) {
                openImagePicker()
            } else {
                PermissionHelper.requestPermissions(this)
            }
        }

        binding.btnUpload.setOnClickListener {
            uploadProduct()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private fun uploadProduct() {
        val name = binding.etName.text.toString().trim()
        val priceStr = binding.etPrice.text.toString().trim()
        val price = priceStr.toDoubleOrNull() ?: 0.0

        if (name.isEmpty() || price <= 0 || selectedImageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                Toast.makeText(this@UploadActivity, getString(R.string.compressing), Toast.LENGTH_SHORT).show()
                val compressedFile = compressImage(selectedImageUri!!)
                
                Toast.makeText(this@UploadActivity, getString(R.string.uploading), Toast.LENGTH_SHORT).show()
                uploadToFirebase(compressedFile, name, price)
            } catch (e: Exception) {
                Toast.makeText(this@UploadActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun compressImage(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val tempFile = File(cacheDir, "temp_image.jpg")
        FileOutputStream(tempFile).use { output ->
            inputStream?.copyTo(output)
        }
        return ImageCompressor.compressImage(this, tempFile)
    }

    private fun uploadToFirebase(file: File, name: String, price: Double) {
        val storageRef = FirebaseStorage.getInstance().reference.child("products/${UUID.randomUUID()}.jpg")
        val uploadTask = storageRef.putFile(Uri.fromFile(file))

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { url ->
                val product = Product(name = name, price = price, imageUrl = url.toString())
                FirestoreManager.addProduct(product, {
                    Toast.makeText(this, getString(R.string.success_upload), Toast.LENGTH_SHORT).show()
                    finish()
                }, {
                    Toast.makeText(this, "Firestore Error: ${it.message}", Toast.LENGTH_SHORT).show()
                })
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Upload Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionHelper.REQ_CODE_PERMISSIONS && PermissionHelper.hasPermissions(this)) {
            openImagePicker()
        } else {
            Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
        }
    }
}
