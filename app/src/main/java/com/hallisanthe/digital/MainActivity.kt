package com.hallisanthe.digital

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.hallisanthe.digital.adapters.ProductAdapter
import com.hallisanthe.digital.databinding.ActivityMainBinding
import com.hallisanthe.digital.firebase.FirestoreManager
import com.hallisanthe.digital.models.Product

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProductAdapter
    private var allProducts = listOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupBottomNav()
        setupSearchView()
        fetchProducts()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(emptyList()) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }
        binding.recyclerViewProducts.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewProducts.adapter = adapter
    }

    private fun setupBottomNav() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_browse -> {
                    // Already in Browse
                    true
                }
                R.id.nav_sell -> {
                    startActivity(Intent(this, UploadActivity::class.java))
                    false // Don't highlight 'Sell' in main since it's a separate activity
                }
                else -> false
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterProducts(newText)
                return true
            }
        })
    }

    private fun fetchProducts() {
        binding.progressBar.visibility = View.VISIBLE
        FirestoreManager.getProducts({ products ->
            binding.progressBar.visibility = View.GONE
            allProducts = products
            updateUI(products)
        }, {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        })
    }

    private fun filterProducts(query: String?) {
        val filtered = if (query.isNullOrEmpty()) {
            allProducts
        } else {
            allProducts.filter { it.name.contains(query, ignoreCase = true) }
        }
        updateUI(filtered)
    }

    private fun updateUI(products: List<Product>) {
        if (products.isEmpty()) {
            binding.emptyStateLayout.visibility = View.VISIBLE
            binding.recyclerViewProducts.visibility = View.GONE
        } else {
            binding.emptyStateLayout.visibility = View.GONE
            binding.recyclerViewProducts.visibility = View.VISIBLE
            adapter.updateList(products)
        }
    }
}
