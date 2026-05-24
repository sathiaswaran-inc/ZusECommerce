package com.example.zussathia.ui.productdetails

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.zussathia.data.model.ProductData
import com.example.zussathia.ui.theme.BackgroundGrey
import com.example.zussathia.ui.StoreViewModel
import com.example.zussathia.ui.theme.TextDark
import com.example.zussathia.utils.Status

@Composable
fun ProductDetailsScreen(
    product: ProductData,
    navController: NavController,
    storeViewModel: StoreViewModel
) {
    val context = LocalContext.current
    val isCurrentlyFav = storeViewModel.favProducts[product._id] ?: false
    val pagerState = rememberPagerState(pageCount = { product.images.size })

    val addToCartState = storeViewModel.addToCart.observeAsState().value

    var selectedQuantity by remember { mutableIntStateOf(0) }
    var showCheckoutSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        storeViewModel.resetAddToCartState()
    }

    LaunchedEffect(addToCartState) {
        addToCartState?.let {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(context, it.data?.message ?: "Added to cart", Toast.LENGTH_SHORT)
                        .show()

                    if (selectedQuantity > 1) {
                        storeViewModel.updateCartQuantity(product._id, selectedQuantity)
                    }

                    selectedQuantity = 0
                    storeViewModel.getCartItems()
                }

                Status.ERROR -> {
                    Toast.makeText(
                        context,
                        it.message ?: "Failed to add to cart",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGrey)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(1.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "back",
                        tint = TextDark,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Product Details",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                    color = TextDark
                )
                Spacer(modifier = Modifier.size(48.dp))
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(400.dp)
                    .shadow(0.5.dp, RoundedCornerShape(40.dp))
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.White)
            ) {
                if (product.images.isNotEmpty()) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        SubcomposeAsyncImage(
                            model = product.images[page],
                            contentDescription = "product image $page",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = Color.Black,
                                        strokeWidth = 2.dp
                                    )
                                }
                            }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(product.images.size) { iteration ->
                            val color =
                                if (pagerState.currentPage == iteration) Color.Black else Color.LightGray
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                        }
                    }
                } else {
                    SubcomposeAsyncImage(
                        model = product.imageCover,
                        contentDescription = "product cover",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color.Black, strokeWidth = 2.dp)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.category.name,
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Text(
                            text = product.title,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                lineHeight = 32.sp
                            ),
                            color = TextDark,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "by ${product.brand.name}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$${product.price}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextDark
                            )
                        )
                    }

                    IconButton(
                        onClick = {
                            if (isCurrentlyFav) {
                                storeViewModel.deleteItemFromWishList(product._id)
                            } else {
                                storeViewModel.addItemToWishList(product._id)
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .shadow(2.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Icon(
                            if (isCurrentlyFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "fav",
                            tint = if (isCurrentlyFav) Color.Red else TextDark,
                            modifier = Modifier.size(26.dp)

                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color.White)
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                ) {
                    IconButton(
                        onClick = {
                            if (selectedQuantity > 0) selectedQuantity--
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Text(
                            text = "−",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (selectedQuantity > 0) TextDark else Color.LightGray
                        )
                    }

                    Text(
                        text = selectedQuantity.toString(),
                        modifier = Modifier.width(40.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextDark
                    )

                    IconButton(
                        onClick = {
                            if (selectedQuantity < product.quantity) selectedQuantity++
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Increase",
                            tint = if (selectedQuantity < product.quantity) TextDark else Color.LightGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 1.dp)
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFF39C12),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.ratingsAverage} Rating",
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(Color.Gray, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "${product.sold} Sold",
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(120.dp))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(BackgroundGrey.copy(alpha = 0.95f))
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (selectedQuantity > 0) {
                        val cartProducts =
                            storeViewModel.cartData.value?.data?.data?.products ?: emptyList()
                        val itemInCart = cartProducts.find { it.productId == product._id }

                        if (itemInCart != null) {
                            storeViewModel.updateCartQuantity(
                                product._id,
                                itemInCart.count + selectedQuantity
                            )
                        } else {
                            storeViewModel.addProductToCart(product._id)
                        }
                    } else {
                        Toast.makeText(context, "Please select quantity", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .height(56.dp)
                    .weight(1f)
                    .shadow(1.dp, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Add to Cart",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Button(
                onClick = {
                    if (selectedQuantity > 0) {
                        showCheckoutSuccess = true
                    } else {
                        Toast.makeText(context, "Minimum order is 1", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier
                    .height(56.dp)
                    .weight(1f),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Buy Now",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        if (showCheckoutSuccess) {
            AlertDialog(
                onDismissRequest = { showCheckoutSuccess = false },
                confirmButton = {
                    TextButton(onClick = { showCheckoutSuccess = false }) {
                        Text("OK", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                },
                title = { Text("Purchase Successful") },
                text = { Text("You have successfully bought $selectedQuantity ${product.title}!") },
                shape = RoundedCornerShape(20.dp),
                containerColor = Color.White
            )
        }
    }
}
