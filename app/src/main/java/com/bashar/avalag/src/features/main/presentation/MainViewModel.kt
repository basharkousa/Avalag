package com.bashar.avalag.src.features.main.presentation

// main/presentation/MainViewModel.kt

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val _cartBadge = MutableStateFlow(0)
    val cartBadge: StateFlow<Int> = _cartBadge

    fun setCartCount(count: Int) { _cartBadge.value = count }
}
