package com.hallisanthe.digital.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hallisanthe.digital.databinding.ItemProductBinding
import com.hallisanthe.digital.models.Product

class ProductAdapter(
    private var products: List<Product>,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.binding.tvProductName.text = product.name
        holder.binding.tvProductPrice.text = "₹${product.price}"
        
        com.bumptech.glide.Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(holder.binding.ivProductThumb)

        holder.itemView.setOnClickListener { onItemClick(product) }
    }

    override fun getItemCount(): Int = products.size

    fun updateList(newList: List<Product>) {
        products = newList
        notifyDataSetChanged()
    }
}
