package com.bashar.avalag.ph2androidArchitectureKotlin

import org.junit.Assert
import org.junit.Test

data class BankAccount(
    val id: Int,
    val ownerName: String,
    val balance: Double
)

interface AccountRepository {

    fun getAccounts(): List<BankAccount>

    fun findAccountById(id: Int): BankAccount?

}

class InMemoryAccountRepository : AccountRepository {

    private val accounts = listOf(
        BankAccount(
            1,
            "Bashar",
            100.0
        ),
        BankAccount(
            2,
            "Ali",
            200.0
        ),
        BankAccount(
            3,
            "Hamada",
            1000.0
        ),)

    override fun getAccounts(): List<BankAccount> {
        return accounts
    }
    override fun findAccountById(id: Int): BankAccount? {
        return accounts.find { id == it.id }
     }
}


class RepositoryPattern {

    @Test
    fun `getAccounts returns all accounts`() {
        val repository = InMemoryAccountRepository()

        val result = repository.getAccounts()

        Assert.assertEquals(3, result.size)
    }

    @Test
    fun `findAccountById returns account when id exists`() {
        val repository = InMemoryAccountRepository()

        val result = repository.findAccountById(2)

        Assert.assertEquals("Ali", result?.ownerName)
    }

    @Test
    fun `findAccountById returns null when id does not exist`() {
        val repository = InMemoryAccountRepository()

        val result = repository.findAccountById(999)

        Assert.assertNull(result)
    }
}