package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accounts.data.repository

import com.bashar.avalag.ph2androidArchitectureKotlin.AccountRemoteDataSource
import com.bashar.avalag.ph2androidArchitectureKotlin.AccountRepository
import com.bashar.avalag.ph2androidArchitectureKotlin.BankAccount
import com.bashar.avalag.ph2androidArchitectureKotlin.toDomain

class AccountRepositoryImpl(private val dataSource: AccountRemoteDataSource) : AccountRepository{
    override fun getAccounts(): List<BankAccount> {
        return dataSource.getAccounts().toDomain()
    }

    override fun findAccountById(id: Int): BankAccount? {
        return dataSource.getAccounts().find { it.accountId == id }?.toDomain()
    }
}