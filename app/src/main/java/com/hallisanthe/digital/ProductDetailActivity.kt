package com.hallisanthe.digital

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hallisanthe.digital.databinding.ActivityProductDetailBinding
import com.hallisanthe.digital.models.Product

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product = intent.getSerializableExtra("product") as? Product
        if (product == null) {
            finish()
            return
        }

        setupUI(product)
    }

    private fun setupUI(product: Product) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapsingToolbar.title = product.name
        
        binding.tvDetailName.text = product.name
        binding.tvDetailPrice.text = "₹${product.price}"

        com.bumptech.glide.Glide.with(this)
            .load(product.imageUrl)
            .into(binding.ivProductLarge)

        binding.btnSendMessage.setOnClickListener {
            Toast.makeText(this, getString(R.string.message_sent), Toast.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
