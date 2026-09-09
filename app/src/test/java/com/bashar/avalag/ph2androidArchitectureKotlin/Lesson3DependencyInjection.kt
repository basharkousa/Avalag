package com.bashar.avalag.ph2androidArchitectureKotlin

import org.junit.Assert
import org.junit.Test

data class AccountUiState(
    val accounts: List<BankAccount> = emptyList(),
    val selectedAccount: BankAccount? = null,
    val errorMessage: String? = null
)

sealed interface AccountUiEvent {
    data object LoadAccounts : AccountUiEvent
    data class AccountSelected(val id: Int) : AccountUiEvent
}

class AccountViewModel(private val repository: AccountRepository) {
    var state = AccountUiState()
        private set


    fun onEvent(event: AccountUiEvent){
        when(event){
            is AccountUiEvent.AccountSelected -> getAccount(event.id)
            AccountUiEvent.LoadAccounts -> loadAccounts()
        }
    }

    private fun loadAccounts() {
        state = state.copy(accounts = repository.getAccounts())
    }

    private fun getAccount(id: Int){
        val account = repository.findAccountById(id)
        state = if (account != null) {
            state.copy(selectedAccount = account, errorMessage = null)
        } else {
            state.copy(selectedAccount = null, errorMessage = "Account not found")
        }
    }
}

class Lesson3DependencyInjection {

    @Test
    fun `Accounts Load Correctly`(){
        val repository: AccountRepository = InMemoryAccountRepository()
        val viewModel = AccountViewModel(repository)

        viewModel.onEvent(AccountUiEvent.LoadAccounts)
        Assert.assertEquals(3,viewModel.state.accounts.size)
    }

    @Test
    fun `Account Loads Correctly`(){
        val repository: AccountRepository = InMemoryAccountRepository()
        val viewModel = AccountViewModel(repository)

        viewModel.onEvent(AccountUiEvent.AccountSelected(1))
        Assert.assertEquals(1,viewModel.state.selectedAccount?.id)
        Assert.assertNull(viewModel.state.errorMessage)
    }

    @Test
    fun `Invalid Account Shows Error`() {
        val repository: AccountRepository =
            InMemoryAccountRepository()

        val viewModel = AccountViewModel(repository)

        viewModel.onEvent(
            AccountUiEvent.AccountSelected(999)
        )

        Assert.assertNull(
            viewModel.state.selectedAccount
        )

        Assert.assertEquals(
            "Account not found",
            viewModel.state.errorMessage
        )
    }
}