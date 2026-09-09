package com.bashar.avalag.ph2androidArchitectureKotlin

import com.bashar.avalag.ph2androidArchitectureKotlin.BankAccount
import com.bashar.avalag.ph1kotlinRefresh.Currency
import org.junit.Assert
import org.junit.Test


/*
{
  "account_id": 123,
  "owner_name": "Bashar",
  "balance": 1500.50,
  "currency": "EUR"
}
*/


//Your Lesson 5 exercise

data class BankAccountDto(
    val accountId: Int,
    val accountHolder: String,
    val currentBalance: Double
)

fun BankAccountDto.toDomain(): BankAccount {
    return BankAccount(
        id = accountId,
        ownerName = accountHolder,
        balance = currentBalance
    )
}

fun List<BankAccountDto>.toDomain(): List<BankAccount> = map { it.toDomain() }.toList()


class Lesson5DtoDomainModelsMappers {
    @Test
    fun `one DTO to correct BankAccount`() {
        val bankDto = BankAccountDto(1, "Bashar", 100.0)
        val bankAccount = bankDto.toDomain()

        Assert.assertEquals(
            BankAccount(
                id = 1,
                ownerName = "Bashar",
                balance = 100.0
            ),
            bankAccount
        )
    }

    @Test
    fun `list of DTO to correct list BankAccount`() {
        val apiResponseAccounts = listOf(
            BankAccountDto(1, "Bashar", 100.0),
            BankAccountDto(2, "Ali", 200.0),
        )
        val bankAccountList = apiResponseAccounts.toDomain()

        Assert.assertEquals(
            listOf(
                BankAccount(1, "Bashar", 100.0),
                BankAccount(2, "Ali", 200.0)
            ),
            bankAccountList
        )
    }
}