package com.bashar.avalag.ph1kotlinRefresh

data class Account(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: String,
    val isActive: Boolean,
    val iban: String? = null
)