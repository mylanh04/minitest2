package com.ml.shopml.ui.screens.home

import android.net.Uri
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.ml.shopml.model.Product
import com.ml.shopml.repository.FirebaseStorageRepository
import com.ml.shopml.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val productRepository = ProductRepository()
    private val firebaseStorageRepository = FirebaseStorageRepository()

    private val _uiState = MutableStateFlow(HomeStateUi())
    val uiState: StateFlow<HomeStateUi> = _uiState

    val products: LiveData<List<Product>> = productRepository.getProducts()

    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onPriceChanged(price: String) {
        val parsedPrice = price.toDoubleOrNull()
        if (parsedPrice != null) {
            _uiState.value = _uiState.value.copy(price = parsedPrice)
        }
    }

    fun onCategoryChanged(category: String) {
        _uiState.value = _uiState.value.copy(category = category)
    }

    fun onImageChanged(image: Uri) {
        _uiState.value = _uiState.value.copy(image = image)
    }

    fun onAddProduct() {
        firebaseStorageRepository.uploadImage(_uiState.value.image).addOnSuccessListener { imageUrl ->
            val product = Product(
                name = _uiState.value.name,
                price = _uiState.value.price,
                category = _uiState.value.category,
                image = imageUrl
            )
            productRepository.addProduct(product)
        }
    }

    fun onUpdateProduct(product: Product) {
        productRepository.updateProduct(product)
    }

    fun onDeleteProduct(productId: String) {
        productRepository.deleteProduct(productId)
    }

}