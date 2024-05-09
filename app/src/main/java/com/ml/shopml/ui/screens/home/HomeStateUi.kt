package com.ml.shopml.ui.screens.home

import android.net.Uri

data class HomeStateUi(
    val name: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val image: Uri = Uri.EMPTY
)