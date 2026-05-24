package com.example.zussathia.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.zussathia.ui.StoreViewModel
import com.example.zussathia.ui.cart.CartScreen
import com.example.zussathia.ui.dashboard.DashboardScreen
import com.example.zussathia.ui.fav.FavoritesScreen
import com.example.zussathia.ui.productdetails.ProductDetailsScreen

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Favorites : Screen("favorites")
    data object ProductDetails : Screen("productDetails/{productID}") {
        fun createRoute(productID: String) = "productDetails/$productID"
    }

    data object Carts : Screen("carts")
}

@Composable
fun AppNavigations(storeViewModel: StoreViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController, storeViewModel)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(navController, storeViewModel)
        }
        composable(
            route = Screen.ProductDetails.route,
            arguments = listOf(navArgument("productID") { type = NavType.StringType })
        ) { backStakeEntry ->
            val productID = backStakeEntry.arguments?.getString("productID") ?: ""
            val productDataState = storeViewModel.storeProducts.observeAsState().value
            val product = productDataState?.data?.data?.find { it._id == productID }
            if (product != null) {
                ProductDetailsScreen(
                    product = product,
                    navController = navController,
                    storeViewModel = storeViewModel
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }
        }
        composable(Screen.Carts.route) {
            CartScreen(navController, storeViewModel)
        }
    }
}