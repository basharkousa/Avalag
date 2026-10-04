package com.bashar.avalag.ph3Coroutins

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import kotlin.time.measureTime

data class AccountDashboard(
    val account: Account,
    val balance: Double
)

class L2LaunchAsyncAwait {

    //Both child coroutines can progress concurrently
    fun mainLaunch() = runBlocking {
        launch {
            delay(1000)
            println("Launch1")
        }
        launch {
            delay(2000)
            println("Launch2")
        }
    }

    //Functions run sequntilly

    fun mainAsyncAwait() = runBlocking {
        val accountDeferred = async { fetchAccount(1) }
        val balanceDeferred = async { fetchBalance(1) }
        val account = accountDeferred.await()
        val balance = balanceDeferred.await()

        println("Account: ${account.name}")
        println("Balance: $balance")
    }


    //Exercise 3.2A

    suspend fun loadAccountDashboard(id: Int): AccountDashboard = coroutineScope {
        println("Loading dashboard...")

        val accountDeferred = async { fetchAccount(id) }
        val balanceDeferred = async { fetchBalance(id) }
        val account = accountDeferred.await()
        val balance = balanceDeferred.await()


        println("Account:${account}")
        println("Balance: $balance")
        println("Dashboard loaded!")
        AccountDashboard(account, balance)
    }

    @Test
    fun `Load Account Dashboard`() = runTest {
        val startTime = testScheduler.currentTime
        loadAccountDashboard(1)
        val elapsedTime = testScheduler.currentTime - startTime

        Assert.assertEquals(1000, elapsedTime)
    }

}
