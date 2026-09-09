package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.data.remote

class AccountDetailsDto(
    val accountId: Int,
    val customerName: String,
    val availableBalance: Double,
    val isActive: Boolean
)