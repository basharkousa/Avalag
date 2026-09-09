package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accounts.presentation

sealed interface AccountUiEvent {
    data object LoadAccounts : AccountUiEvent
    data class AccountSelected(val id: Int) : AccountUiEvent
}