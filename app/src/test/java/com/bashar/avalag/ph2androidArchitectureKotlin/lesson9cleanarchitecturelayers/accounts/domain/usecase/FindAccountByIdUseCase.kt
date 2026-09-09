package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accounts.domain.usecase

import com.bashar.avalag.ph2androidArchitectureKotlin.AccountRepository
import com.bashar.avalag.ph2androidArchitectureKotlin.BankAccount

class FindAccountByIdUseCase(private val repository: AccountRepository) {
    operator fun invoke(id: Int): BankAccount? = repository.findAccountById(id)
}
