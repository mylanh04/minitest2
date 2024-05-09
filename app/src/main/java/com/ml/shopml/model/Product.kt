package com.ml.shopml.model

data class Product(
    var id: String? = null,
    var name: String? = null,
    var price: Double = 0.0,
    var category: String? = null,
    var image: String? = null
)
