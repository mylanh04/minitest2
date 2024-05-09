package com.ml.shopml

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ml.shopml.ui.screens.home.HomeScreen
import com.ml.shopml.ui.screens.home.HomeViewModel
import com.ml.shopml.ui.theme.ShopMLTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopMLTheme {
                val homeViewModel by viewModels<HomeViewModel>()
                HomeScreen(homeViewModel)
            }
        }
    }
}

