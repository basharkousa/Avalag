package com.bashar.avalag.ph2androidArchitectureKotlin

import org.junit.Assert
import org.junit.Test

class GetAccountsUseCase(private val repository: AccountRepository) {
    operator fun invoke(): List<BankAccount> = repository.getAccounts()
}

class FindAccountByIdUseCase(private val repository: AccountRepository) {
    operator fun invoke(id: Int): BankAccount? = repository.findAccountById(id)
}

class ViewModel(
    private val getAccountsUseCase: GetAccountsUseCase,
    private val findAccountUseCase: FindAccountByIdUseCase
) {
    var state: AccountUiState = AccountUiState()
        private set

    fun onEvent(event: AccountUiEvent) {
        when (event) {
            is AccountUiEvent.AccountSelected -> {
                getAccount(event.id)
            }

            AccountUiEvent.LoadAccounts -> {
                loadAccounts()
            }
        }
    }

    private fun loadAccounts() {
        state = state.copy(accounts = getAccountsUseCase())
    }

    private fun getAccount(id: Int){
        val account = findAccountUseCase(id)
        state = if (account != null) {
            state.copy(selectedAccount = account, errorMessage = null)
        } else {
            state.copy(selectedAccount = null, errorMessage = "Account not found")
        }
    }
}

class Lesson4UseCases {
    @Test
    fun `AccountsUseCase return accounts`() {
        val repository: AccountRepository = InMemoryAccountRepository()
        val getAccountsUseCase = GetAccountsUseCase(repository)
        Assert.assertEquals(3, getAccountsUseCase().size)
    }

    @Test
    fun `FindAccountUseCase return account`() {
        val repository: AccountRepository = InMemoryAccountRepository()
        val findAccountByIdUseCase = FindAccountByIdUseCase(repository)
        Assert.assertEquals(1, findAccountByIdUseCase(1)?.id)
    }

    @Test
    fun `Account Not Found`() {
        val repository: AccountRepository = InMemoryAccountRepository()
        val findAccountByIdUseCase = FindAccountByIdUseCase(repository)
        Assert.assertNull(findAccountByIdUseCase(10)?.id)
    }
}