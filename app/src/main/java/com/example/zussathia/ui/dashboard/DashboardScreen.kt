package com.example.zussathia.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.zussathia.nav.Screen
import com.example.zussathia.ui.theme.BackgroundGrey
import com.example.zussathia.ui.components.EmptyProductState
import com.example.zussathia.ui.components.GlassMorphicBottomNavigation
import com.example.zussathia.ui.components.ProductGridSelection
import com.example.zussathia.ui.StoreViewModel
import com.example.zussathia.ui.components.TitleSection
import com.example.zussathia.ui.components.ShimmerBox
import com.example.zussathia.ui.components.ProductGridShimmer
import com.example.zussathia.utils.Status

@Composable
fun DashboardScreen(
    navController: NavController,
    storeViewModel: StoreViewModel
) {

    val categoryDataState = storeViewModel.storeCategories.observeAsState().value
    val productDataState = storeViewModel.storeProducts.observeAsState().value
    val addedFavItemState = storeViewModel.addToFav.observeAsState().value
    val deletedFavItemState = storeViewModel.deleteFavItem.observeAsState().value
    val cartCount = storeViewModel.cartItemCount.observeAsState(0).value

    var selectedCategoryID by remember { mutableStateOf("") }
    var showFavLoadingOverlay by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        storeViewModel.getAllFavItem()
        storeViewModel.getCartItems()
    }

    LaunchedEffect(addedFavItemState) {
        addedFavItemState?.let {
            showFavLoadingOverlay = when (it.status) {
                Status.SUCCESS, Status.ERROR -> false
                Status.LOADING -> true
            }
        }
    }

    LaunchedEffect(deletedFavItemState) {
        deletedFavItemState?.let {
            showFavLoadingOverlay = when (it.status) {
                Status.SUCCESS, Status.ERROR -> false
                Status.LOADING -> true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGrey)
    ) {
        val categories = categoryDataState?.data?.data ?: emptyList()
        val allProduct = productDataState?.data?.data ?: emptyList()
        val filterProducts = allProduct.filter {
            it.category._id == selectedCategoryID && it.title.contains(
                searchQuery,
                ignoreCase = true
            )
        }


        if (selectedCategoryID.isEmpty() && categories.isNotEmpty()) {
            selectedCategoryID = categories.first()._id
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                TitleSection(
                    searchQuery = searchQuery,
                    onSearchQueryChanged = { searchQuery = it },
                    isSearchActive = isSearchActive,
                    onSearchActiveChanged = { isSearchActive = it },
                    cartCount = cartCount,
                    onCartClick = { navController.navigate(Screen.Carts.route) }
                )
            }
            item {
                if (categoryDataState != null) {
                    when (categoryDataState.status) {
                        Status.SUCCESS -> {
                            CategorySection(
                                categories = categories,
                                selectedID = selectedCategoryID,
                                onCategorySelected = { selectedCategoryID = it }
                            )
                        }

                        Status.LOADING -> {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                item(5) {
                                    ShimmerBox(
                                        modifier = Modifier
                                            .height(42.dp)
                                            .width(90.dp)
                                    )
                                }
                            }
                        }

                        Status.ERROR -> {
                            Text(
                                text = "Failed to load categories:${categoryDataState.message}",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            if (productDataState?.status == Status.SUCCESS && filterProducts.isNotEmpty()) {
                item {
                    ProductGridSelection(
                        filterProducts,
                        storeViewModel,
                        onProductClick = { productID ->
                            navController.navigate(Screen.ProductDetails.createRoute(productID))
                        }
                    )
                }
            }
            if (productDataState?.status == Status.LOADING) {
                item { ProductGridShimmer() }
            }
            item { Spacer(modifier = Modifier.height(120.dp)) }
        }
        if (productDataState?.status == Status.SUCCESS && filterProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                EmptyProductState(true)
            }
        }
        if (showFavLoadingOverlay) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.Black,
                    strokeWidth = 4.dp
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            GlassMorphicBottomNavigation(
                currentScreen = "home",
                onHomeClick = { /*Already in current screen*/ },
                onFavClick = { navController.navigate(Screen.Favorites.route) },
                onCartClick = { navController.navigate(Screen.Carts.route) },
            )
        }
    }
}