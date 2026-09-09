package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accounts.domain.model

data class BankAccount(
    val id: Int,
    val ownerName: String,
    val balance: Double,
    )