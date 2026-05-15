package com.hallisanthe.digital.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import com.hallisanthe.digital.models.Product

object FirestoreManager {
    private val firestore: FirebaseFirestore by lazy {
        val db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings
        db
    }

    private const val COLLECTION_PRODUCTS = "products"

    fun addProduct(product: Product, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection(COLLECTION_PRODUCTS)
            .add(product)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getProducts(onResult: (List<Product>) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection(COLLECTION_PRODUCTS)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onFailure(error)
                    return@addSnapshotListener
                }
                val products = value?.toObjects(Product::class.java) ?: emptyList()
                onResult(products)
            }
    }
}
