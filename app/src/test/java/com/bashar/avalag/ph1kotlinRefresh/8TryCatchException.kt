package com.bashar.avalag.ph1kotlinRefresh

import org.junit.Assert
import org.junit.Test

class TryCatchException {

    val remainingBalance = withdraw(1000.0, 200.0)

    //Important: try/catch is an expression in Kotlin, so it can return a value.
    val result = try {
        withdraw(1000.0, 200.0)
    } catch (error: Exception) {
        0.0
    }

    @Test
    fun `payment succeeds with valid account and amount`() {
        val paymentAccount =
            PaymentAccount(id = 414543, balance = 1000.00, status = PaymentAccountStatus.ACTIVE)

        Assert.assertEquals(
            PaymentResult.Success(paymentAccount.balance - 100.0),
            safeExecutePayment(paymentAccount, 100.0)
        )
    }

    @Test
    fun `payment fails when amount is negative`() {

        val paymentAccount =
            PaymentAccount(id = 414543, balance = 1000.00, status = PaymentAccountStatus.ACTIVE)

        Assert.assertEquals(
            PaymentResult.Failure("Amount must be greater than zero"),
            safeExecutePayment(paymentAccount, -100.0)
        )

    }

    @Test
    fun `payment fails when account is blocked`() {
        val paymentAccount =
            PaymentAccount(id = 414543, balance = 1000.00, status = PaymentAccountStatus.BLOCKED)

        Assert.assertEquals(
            PaymentResult.Failure("Account is Blocked"),
            safeExecutePayment(paymentAccount, 100.0)
        )
    }

    @Test
    fun `payment fails when balance is insufficient`() {
        val paymentAccount =
            PaymentAccount(id = 414543, balance = 1000.00, status = PaymentAccountStatus.ACTIVE)

        Assert.assertEquals(
            PaymentResult.Failure("Insufficient Balance!!"),
            safeExecutePayment(paymentAccount, 1500.0)
        )
    }

    @Test
    fun `payment fails when account is closed`() {
        val paymentAccount =
            PaymentAccount(id = 414543, balance = 1000.00, status = PaymentAccountStatus.CLOSED)

        Assert.assertEquals(
            PaymentResult.Failure("Account Closed"),
            safeExecutePayment(paymentAccount, 100.0)
        )
    }

}

fun withdraw(balance: Double, amount: Double): Double {
    if (amount <= 0) {
        throw IllegalArgumentException("Amount must be greater than zero")
    }
    if (amount > balance) throw IllegalStateException("Insufficient Balance!!")

    return balance - amount
}

fun safeWithdraw(balance: Double, amount: Double): String {
    return try {
        val remainingBalance = withdraw(100.0, amount = 300.0)
        "Withdrawal successful. Remaining balance: $remainingBalance"
    } catch (e: IllegalStateException) {
        e.message ?: "Invalid amount"
    } catch (e: IllegalStateException) {
        e.message ?: "Withdrawal failed"
    } catch (error: Exception) {
        "Unexpected error: ${error.message}"
    } finally {
        println("Withdrawal operation finished")
    }
}

//Custom Exception

class InsufficientBalanceException(
    val availableBalance: Double,
    val requestedAmount: Double,
) : Exception(
    "Insufficient balance. Available: $availableBalance, requested: $requestedAmount"
)

class AccountBlockedException(
    val accountId: Int,
) : Exception("Account $accountId is blocked")

fun transferMoney(
    account: BankAccount,
    amount: Double,
): Double {
    if (amount <= 0) {
        throw IllegalArgumentException("Amount must be greater than zero")
    }

    if (account.status == AccountStatus.BLOCKED) {
        throw AccountBlockedException(account.id)
    }

    if (amount > account.balance) {
        throw InsufficientBalanceException(
            availableBalance = account.balance,
            requestedAmount = amount,
        )
    }

    return account.balance - amount
}

sealed interface TransferResult {
    data class Success(
        val remainingBalance: Double,
    ) : TransferResult

    data class Failure(
        val message: String,
    ) : TransferResult
}

fun safeTransferMoney(
    account: BankAccount,
    amount: Double,
): TransferResult {
    return try {
        val remainingBalance = transferMoney(account, amount)

        TransferResult.Success(
            remainingBalance = remainingBalance,
        )
    } catch (error: AccountBlockedException) {
        TransferResult.Failure(
            message = "This account is blocked",
        )
    } catch (error: InsufficientBalanceException) {
        TransferResult.Failure(
            message = "Available balance is ${error.availableBalance}",
        )
    } catch (error: IllegalArgumentException) {
        TransferResult.Failure(
            message = error.message ?: "Invalid transfer amount",
        )
    } catch (error: Exception) {
        TransferResult.Failure(
            message = "Unexpected transfer error",
        )
    }
}

//7. try/catch versus runCatching

fun safeLoginTRYCATCH(
    username: String,
    password: String,
): LoginCheckResult {
    return try {
        val token = fetchLogin(username, password)
        LoginCheckResult.Success(token)
    } catch (error: Exception) {
        LoginCheckResult.Failure(
            error.message ?: "Unexpected login error"
        )
    }
}

fun safeLoginRUNCATCHING(
    username: String,
    password: String,
): LoginCheckResult {
    return runCatching {
        fetchLogin(username, password)
    }.fold(
        onSuccess = { token ->
            LoginCheckResult.Success(token)
        },
        onFailure = { error ->
            LoginCheckResult.Failure(
                error.message ?: "Unexpected login error"
            )
        },
    )
}

/*
*
Use try/catch when:

Different exception types require different handling.
You need a finally block.
The logic is easier to read imperatively.

Use runCatching when:

You want a Result.
Success and failure mapping are simple.
You want to chain operations such as map, recover, or fold.
*
* */

///Exercise

enum class PaymentAccountStatus {
    ACTIVE,
    BLOCKED,
    CLOSED
}

data class PaymentAccount(
    val id: Int,
    val balance: Double,
    val status: PaymentAccountStatus
)

sealed interface PaymentResult {
    data class Success(
        val remainingBalance: Double,
    ) : PaymentResult

    data class Failure(
        val message: String,
    ) : PaymentResult
}

class PaymentAccountBlockedException() : Exception("Account is Blocked")
class PaymentServiceUnavailableException(serviceId: Int) :
    Exception("Service $serviceId is Unavailable")

fun executePayment(amount: Double, account: PaymentAccount): Double {
    if (amount <= 0) throw IllegalArgumentException("Amount must be greater than zero")
    if (amount > account.balance) throw IllegalStateException("Insufficient Balance!!")
    if (account.status == PaymentAccountStatus.BLOCKED) throw PaymentAccountBlockedException()
    if (account.status == PaymentAccountStatus.CLOSED) throw IllegalStateException("Account Closed")
    return account.balance - amount
}

fun safeExecutePayment(
    account: PaymentAccount,
    amount: Double,
): PaymentResult {
    return try {
        val remainingBalance = executePayment(amount, account)
        PaymentResult.Success(remainingBalance)
    } catch (error: IllegalArgumentException) {
        PaymentResult.Failure(error.message ?: "unknown error")
    } catch (error: IllegalStateException) {
        PaymentResult.Failure(error.message ?: "unknown error")
    } catch (error: PaymentAccountBlockedException) {
        PaymentResult.Failure(error.message ?: "unknown error")
    } catch (error: PaymentAccountBlockedException) {
        PaymentResult.Failure(error.message ?: "unknown error")
    }
}
