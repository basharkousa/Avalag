package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.presentation

import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.domain.model.AccountDetails

data class AccountDetailsUiState(
    val accountDetails: AccountDetails? = null,
    val errorMessage: String? = null,

)