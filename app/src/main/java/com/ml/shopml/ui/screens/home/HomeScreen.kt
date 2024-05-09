package com.ml.shopml.ui.screens.home

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.ml.shopml.R
import com.ml.shopml.model.Product
import com.ml.shopml.ui.theme.BackgroundLight
import com.ml.shopml.ui.theme.Warning
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val uiState by homeViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val products by homeViewModel.products.observeAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                homeViewModel.onImageChanged(uri)
            }
        }
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundLight)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Dữ liệu sản phẩm",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(500),
                fontSize = 23.sp,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.name, onValueChange = { homeViewModel.onNameChanged(it) },
                label = { Text(text = "Tên sản phẩm") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.category, onValueChange = { homeViewModel.onCategoryChanged(it) },
                label = { Text(text = "Loại sản phẩm") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.price.toString(), onValueChange = { homeViewModel.onPriceChanged(it) },
                label = { Text(text = "Giá") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Ảnh Sản Phẩm")
            Spacer(modifier = Modifier.height(3.dp))
            Image(
                painter = if (uiState.image != Uri.EMPTY) rememberAsyncImagePainter(uiState.image) else painterResource(id = R.drawable.image_default),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(6.dp))
                    .clickable {
                        launcher.launch("image/*")
                    }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    homeViewModel.onAddProduct()
                    Toast.makeText(context, "Sản phẩm đang được tải lên", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Thêm sản phẩm".uppercase())
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Danh sách sản phẩm:",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))

            products?.reversed()?.forEach { product ->
                ProductItem(product = product, context = context, onDeleteProduct = { homeViewModel.onDeleteProduct(it) }, onUpdateProduct =  { homeViewModel.onUpdateProduct(it) })
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, context: Context, onDeleteProduct: (String) -> Unit, onUpdateProduct: (Product) -> Unit) {
    var showDialogUpdate by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(6.dp))
            .border(width = 1.dp, color = Color.Blue, shape = RoundedCornerShape(6.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = product.image),
            contentDescription = "",
            modifier = Modifier
                .size(64.dp)
                .clip(shape = RoundedCornerShape(6.dp))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = buildAnnotatedString {
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append("Tên SP: ")
                    pop()
                    append(product.name)
                },
                fontSize = 15.sp
            )
            Text(
                text = buildAnnotatedString {
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append("Giá SP: ")
                    pop()
                    append(product.price.toString())
                },
                fontSize = 15.sp
            )
            Text(
                text = buildAnnotatedString {
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append("Loại SP: ")
                    pop()
                    append(product.category)
                },
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { showDialogUpdate = true },
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .border(width = 1.dp, color = Warning, shape = RoundedCornerShape(6.dp))
                    .size(30.dp)
                    .padding(5.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "edit", tint = Warning)
            }
            Spacer(modifier = Modifier.height(4.dp))
            IconButton(
                onClick = {
                    product.id?.let {
                        onDeleteProduct(it)
                        Toast.makeText(context, "Sản phẩm đã được xóa", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .border(width = 1.dp, color = Color.Red, shape = RoundedCornerShape(6.dp))
                    .size(30.dp)
                    .padding(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete",
                    tint = Color.Red
                )
            }
        }
    }

    if (showDialogUpdate) {
        Dialog(onDismissRequest = {

        }) {
            var editedName by remember { mutableStateOf(product.name) }
            var editedPrice by remember { mutableDoubleStateOf(product.price) }
            var editedCategory by remember { mutableStateOf(product.category) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(6.dp),
                shape = RoundedCornerShape(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(text = "Chỉnh sửa sản phẩm", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = editedName!!, onValueChange = { editedName = it },
                        label = { Text(text = "Tên sản phẩm") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = editedCategory!!, onValueChange = { editedCategory = it },
                        label = { Text(text = "Loại sản phẩm") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = editedPrice.toString(), onValueChange = { editedPrice = it.toDouble() },
                        label = { Text(text = "Giá") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = product.image),
                            contentDescription = product.name,
                            modifier = Modifier
                                .height(150.dp)
                                .clip(shape = RoundedCornerShape(6.dp))
                        )
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = { showDialogUpdate = false }) {
                            Text(text = "Trở lại")
                        }
                        Button(onClick = {
                            product.name = editedName
                            product.category = editedCategory
                            product.price = editedPrice
                            onUpdateProduct(product)
                            Toast.makeText(context, "Sản phẩm đã được cập nhập", Toast.LENGTH_SHORT).show()
                            showDialogUpdate = false
                        }) {
                            Text(text = "Update")
                        }
                    }
                }
            }
        }
    }
}
