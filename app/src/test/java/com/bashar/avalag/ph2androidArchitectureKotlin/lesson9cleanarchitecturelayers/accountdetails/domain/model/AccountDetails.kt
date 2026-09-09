package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.domain.model

data class AccountDetails(
    val id: Int,
    val ownerName: String,
    val balance: Double,
    val isActive: Boolean
)