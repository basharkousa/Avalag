package com.bashar.avalag.ph2androidArchitectureKotlin

import org.junit.Assert
import org.junit.Test

interface AccountRemoteDataSource{
    fun getAccounts():List<BankAccountDto>
}

class FakeAccountRemoteDataSource : AccountRemoteDataSource{
    var response: List<BankAccountDto> = emptyList()
    override fun getAccounts(): List<BankAccountDto> = response
}

class AccountRepositoryImpl(private val dataSource: AccountRemoteDataSource) : AccountRepository{
    override fun getAccounts(): List<BankAccount> {
        return dataSource.getAccounts().toDomain()
    }

    override fun findAccountById(id: Int): BankAccount? {
        return dataSource.getAccounts().find { it.accountId == id }?.toDomain()
    }
}

class Lesson7DataSource {
    @Test
    fun `Fake remote data sourcecontains`(){
        val accountDataSource = FakeAccountRemoteDataSource()
        accountDataSource.response = listOf(BankAccountDto(
            accountId = 10,
            accountHolder = "Bashar",
            currentBalance = 500.0
        ))
        val repository = AccountRepositoryImpl(accountDataSource)
        val bankAccount = repository.findAccountById(10)
        Assert.assertEquals(BankAccount(
            id = 10,
            ownerName = "Bashar",
            balance = 500.0
        ),bankAccount)
    }
}