package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accounts.presentation

import com.bashar.avalag.ph2androidArchitectureKotlin.BankAccount

data class AccountUiState(
    val accounts: List<BankAccount> = emptyList(),
    val selectedAccount: BankAccount? = null,
    val errorMessage: String? = null
)