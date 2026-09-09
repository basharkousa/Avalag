package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.presentation

import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.domain.usecase.AccountResult
import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.domain.usecase.GetAccountDetailsUseCase

class AccountDetailsViewModel(private val getAccountUseCase: GetAccountDetailsUseCase) {
    var state: AccountDetailsUiState = AccountDetailsUiState()
        private set

    fun onEvent(event: AccountDetailsUiEvent) {
        when (event) {
            is AccountDetailsUiEvent.LoadAccount -> {
                loadAccount(event.id)
            }
        }
    }

    private fun loadAccount(id: Int) {
        val accountResult = getAccountUseCase(id)
        state = when (accountResult) {
            AccountResult.InactiveAccount -> {
                state.copy(accountDetails = null, errorMessage = "Inactive account!!")
            }
            AccountResult.NotFound -> {
                state.copy(accountDetails = null, errorMessage = "Account Not Found!!")
            }
            is AccountResult.Success -> {
                state.copy(accountDetails = accountResult.accountDetails, errorMessage = null)
            }
        }
    }
}

