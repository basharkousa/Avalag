package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accounts.domain.repository

import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accounts.domain.model.BankAccount


interface AccountRepository {
    fun findAccount(accountId: Int): BankAccount?
    fun updateAccount(account: BankAccount)
}