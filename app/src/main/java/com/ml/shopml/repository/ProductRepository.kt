package com.ml.shopml.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ml.shopml.model.Product

class ProductRepository {
    private val databaseReference: DatabaseReference =FirebaseDatabase.getInstance().getReference("products")

    fun getProducts(): LiveData<List<Product>> {
        val productLiveData = MutableLiveData<List<Product>>()
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val products: MutableList<Product> = ArrayList()
                dataSnapshot.children.forEach { snapshot ->
                    products.add(snapshot.getValue(Product::class.java)!!)
                }
                productLiveData.value = products
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DatabaseError", error.message)
            }
        })
        return productLiveData
    }

    fun addProduct(product: Product) {
        val id = databaseReference.push().key
        product.id = id
        databaseReference.child(id!!).setValue(product)
    }

    fun updateProduct(product: Product) {
        databaseReference.child(product.id!!).setValue(product)
    }

    fun deleteProduct(productId: String) {
        databaseReference.child(productId).removeValue()
    }
}