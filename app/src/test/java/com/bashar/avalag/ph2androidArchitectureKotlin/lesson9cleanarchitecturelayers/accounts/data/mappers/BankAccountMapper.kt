package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accounts.data.mappers

import com.bashar.avalag.ph2androidArchitectureKotlin.BankAccount
import com.bashar.avalag.ph2androidArchitectureKotlin.BankAccountDto

fun BankAccountDto.toDomain(): BankAccount {
    return BankAccount(
        id = accountId,
        ownerName = accountHolder,
        balance = currentBalance
    )
}
