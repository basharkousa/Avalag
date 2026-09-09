package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accounts.domain.usecase

import com.bashar.avalag.ph2androidArchitectureKotlin.AccountRepository
import com.bashar.avalag.ph2androidArchitectureKotlin.BankAccount

class GetAccountsUseCase(private val repository: AccountRepository) {
    operator fun invoke(): List<BankAccount> = repository.getAccounts()
}
