package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.presentation

sealed interface AccountDetailsUiEvent {
    data class LoadAccount(val id: Int) : AccountDetailsUiEvent
}