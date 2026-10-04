package com.bashar.avalag.ph3Coroutins

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test


fun getAccount(): String {
    Thread.sleep(2000)
    return "CurrentAccount"
}

suspend fun getAccount2(): String {
    delay(2000)
    return ""
}

/*Your first coroutine

For simple JVM exercises we can use:
*/


fun main() = runBlocking {

    println("Loading account...")

    delay(2000)

    println("Account loaded")
}

/*
* Exercise 3.1 A , B
* */
data class Account(val id: Int, val name: String, val balance: Double)

suspend fun fetchAccount(id:Int): Account{
    delay(1000)
    return Account(id = id, name = "Current Account", balance = 1500.00)
}

suspend fun fetchBalance(id:Int): Double{
    delay(500)
    return 2500.0
}

suspend fun printAccountInformation(id: Int){
    val name = fetchAccount(id).name
    val balance = fetchBalance(id)
    println("Account Name: $name")
    println("The balance: $balance")
}


/*
* Exercise 3.1 C
* 3 seconds cuz Kotlin is executing sequentially so first fun will take 1sec and the other 2sec
* */

class L1CoroutinesFundamentals {

     fun main() = runBlocking{
//        fetchAccount(1)
         printAccountInformation(1)
    }

    @Test
    fun `account is returned correctly`() = runTest {

        // Arrange
        val accountId = 101

        // Act
        val account = fetchAccount(accountId)
        val balance = fetchBalance(accountId)

        // Assert
        Assert.assertEquals(101, account.id)

        Assert.assertEquals(
            "Current Account",
            account.name
        )

        Assert.assertEquals(
            1500.0,
            account.balance,
            0.0
        )

        Assert.assertEquals(
            2500.0,
            balance,
            0.0
        )
    }
}