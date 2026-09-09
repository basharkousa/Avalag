package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accounts.data.remote

data class BankAccountDto(
    val accountId: Int,
    val accountHolder: String,
    val currentBalance: Double
)