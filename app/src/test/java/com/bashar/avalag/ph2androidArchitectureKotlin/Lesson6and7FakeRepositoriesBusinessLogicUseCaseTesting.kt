package com.bashar.avalag.ph2androidArchitectureKotlin

import org.junit.Assert
import org.junit.Test

sealed interface FindAccountResult {
    data class Success(val bankAccount: BankAccount) : FindAccountResult
    data object NotFound : FindAccountResult
    data object ZeroBalance : FindAccountResult
}

// Business logic in the use case
class FindAccountByIdWithResultUseCase(
    private val repository: AccountRepository
) {

    operator fun invoke(id: Int): FindAccountResult {
        val account = repository.findAccountById(id)
            ?: return FindAccountResult.NotFound

        return if (account.balance > 0) {
            FindAccountResult.Success(account)
        } else {
            FindAccountResult.ZeroBalance
        }
    }
}

class FakeAccountRepository : AccountRepository {

    var accountsToReturn: List<BankAccount> = emptyList()

    override fun getAccounts(): List<BankAccount> {
        return accountsToReturn
    }

    override fun findAccountById(id: Int): BankAccount? = accountsToReturn.find { it.id == id }

}

class AccountViewModel2(
    private val getAccountsUseCase: GetAccountsUseCase,
    private val findAccountByIdUseCase: FindAccountByIdWithResultUseCase
) {
    var state: AccountUiState = AccountUiState()
        private set

    private fun getAccounts() {
        state = state.copy(accounts = getAccountsUseCase())
    }

   private fun selectAccount(id: Int) {
        val result = findAccountByIdUseCase(id)
        state = when (result) {
            FindAccountResult.NotFound -> state.copy(
                selectedAccount = null,
                errorMessage = "Account not found"
            )

            is FindAccountResult.Success -> state.copy(
                selectedAccount = result.bankAccount,
                errorMessage = null
            )

            FindAccountResult.ZeroBalance -> state.copy(
                selectedAccount = null,
                errorMessage = "Zero Balance"
            )
        }
    }

    fun onEvent(event: AccountUiEvent) {
        when (event) {
            is AccountUiEvent.AccountSelected -> selectAccount(event.id)
            AccountUiEvent.LoadAccounts -> getAccounts()
        }
    }

    class Lesson6FakeRepositoriesTesting {

        @Test
         fun `repository maps remote dto to domain account`() {

            val repository = FakeAccountRepository()
            repository.accountsToReturn = listOf(
                BankAccount(1, "Bashar", 100.0),
                BankAccount(2, "Ali", 100.0),
            )
            val getAccountsUseCase = GetAccountsUseCase(repository)
            val findAccountByIdUseCase = FindAccountByIdWithResultUseCase(repository)
            val viewModel = AccountViewModel2(getAccountsUseCase, findAccountByIdUseCase)
            viewModel.onEvent(AccountUiEvent.LoadAccounts)
            Assert.assertEquals(2, viewModel.state.accounts.size)

        }

        @Test
        fun `Account is exist`() {
            val repository = FakeAccountRepository()
            repository.accountsToReturn = listOf(
                BankAccount(1, "Bashar", 100.0),
                BankAccount(2, "Ali", 100.0),
            )
            val getAccountsUseCase = GetAccountsUseCase(repository)
            val findAccountByIdUseCase = FindAccountByIdWithResultUseCase(repository)
            val viewModel = AccountViewModel2(getAccountsUseCase, findAccountByIdUseCase)
            viewModel.onEvent(AccountUiEvent.AccountSelected(1))
            Assert.assertNotNull(viewModel.state.selectedAccount)
        }

        @Test
        fun `Account not found`() {
            val repository = FakeAccountRepository()
            repository.accountsToReturn = listOf(
                BankAccount(1, "Bashar", 100.0),
                BankAccount(2, "Ali", 100.0),
            )
            val getAccountsUseCase = GetAccountsUseCase(repository)
            val findAccountByIdUseCase = FindAccountByIdWithResultUseCase(repository)
            val viewModel = AccountViewModel2(getAccountsUseCase, findAccountByIdUseCase)
            viewModel.onEvent(AccountUiEvent.AccountSelected(11))
            Assert.assertNull(viewModel.state.selectedAccount)
        }


    }
}