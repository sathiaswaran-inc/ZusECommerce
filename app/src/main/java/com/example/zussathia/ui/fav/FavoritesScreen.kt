package com.example.zussathia.ui.fav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.zussathia.ui.components.ProductGridShimmer
import com.example.zussathia.utils.Status

@Composable
fun FavoritesScreen(
    navController: NavController,
    storeViewModel: StoreViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    val favItemState = storeViewModel.getFavItem.observeAsState().value
    val cartCount = storeViewModel.cartItemCount.observeAsState(0).value
    val favItems = favItemState?.data?.data ?: emptyList()
    val filterFavItems = favItems.filter { favs ->
        favs.title.contains(searchQuery, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGrey)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                TitleSection(
                    title = "My Favorites",
                    subTitle = "Your Saved Items",
                    searchQuery = searchQuery,
                    onSearchQueryChanged = { searchQuery = it },
                    isSearchActive = isSearchActive,
                    onSearchActiveChanged = { isSearchActive = it },
                    cartCount = cartCount,
                    onCartClick = { navController.navigate(Screen.Carts.route) }
                )
            }
            if (favItemState != null) {
                when (favItemState.status) {
                    Status.SUCCESS -> {
                        item {
                            ProductGridSelection(
                                products = filterFavItems,
                                storeViewModel = storeViewModel,
                                onProductClick = { productID ->
                                    navController.navigate(
                                        Screen.ProductDetails.createRoute(
                                            productID
                                        )
                                    )
                                }
                            )
                        }
                    }

                    Status.LOADING -> {
                        item { ProductGridShimmer() }
                    }

                    Status.ERROR -> {
                        item {
                            Text(
                                text = "Failed to load favorites: {favItemState.message}",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(120.dp)) }
        }

        if (favItemState?.status == Status.SUCCESS && filterFavItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                EmptyProductState(false)
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
                currentScreen = "favorites",
                onHomeClick = { navController.navigate(Screen.Dashboard.route) },
                onFavClick = { /*Already in current screen*/ },
                onCartClick = { navController.navigate(Screen.Carts.route) },
            )
        }
    }
}