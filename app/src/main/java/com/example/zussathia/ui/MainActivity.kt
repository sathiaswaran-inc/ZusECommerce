package com.example.zussathia.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.zussathia.nav.AppNavigations
import com.example.zussathia.ui.theme.ZusSathiaTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity: ComponentActivity() {

    private val storeViewModel: StoreViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storeViewModel.getAllCategory()
        storeViewModel.getAllProducts()
        storeViewModel.getAllFavItem()

        setContent {
            ZusSathiaTheme {
                AppNavigations(storeViewModel = storeViewModel)
            }
        }
    }
}