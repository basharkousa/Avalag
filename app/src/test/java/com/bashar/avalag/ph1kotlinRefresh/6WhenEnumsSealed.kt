package com.bashar.avalag.ph1kotlinRefresh

import org.junit.Assert
import org.junit.Test


/* When to use enum vs sealed
*
* | Use                                           | Best choice        |
| --------------------------------------------- | ------------------ |
| Simple fixed list                             | `enum class`       |
| Fixed cases with different data               | `sealed interface` |
| API status like ACTIVE/BLOCKED/CLOSED         | `enum`             |
| Login result Success/Error/MustChangePassword | `sealed interface` |
| Form validation Valid/Invalid(message)        | `sealed interface` |
| Currency EUR/USD/SYP                          | `enum`             |

* */


class ABNAmroBank {

    @Test
    fun `Transfer Actions Test`() {
        val myAccount = BankAccount(
            id = 1012210,
            name = "Current Account",
//            currency = Currency.EUR,
            status = AccountStatus.ACTIVE,
            balance = 1000.0,
        )

        val myOldAccount = BankAccount(
            id = 1012210,
            name = "Current Account",
            status = AccountStatus.BLOCKED,
            balance = 0.0,
        )

        printTransferResult(checkTransfer(myAccount,
            200.0))
        printTransferResult(checkTransfer(myAccount,
            1500.0))

        Assert.assertEquals(TransferCheckResult.Allowed, checkTransfer(myAccount,
            200.0))


        Assert.assertEquals(
            TransferCheckResult.NotAllowed("Insufficient balance"),
            checkTransfer(myAccount, 1500.0)
        )
        Assert.assertEquals(AccountStatus.BLOCKED,myOldAccount.status)

        Assert.assertEquals(TransferCheckResult.NotAllowed("Account is blocked"),checkTransfer(myOldAccount, amount = 100.0))


    }

}

enum class AccountStatus {
    ACTIVE, BLOCKED, CLOSED
}

enum class Currency(val symbol: String) {
    EUR("€"),
    USD("$"),
    SYP("SYP")
}

data class Accountt(
    val id: Int,
    val name: String,
    val status: AccountStatus,
    val currency: Currency,
    val balance: Double
)

fun Currency.showSymbol(): String = symbol

sealed interface LoginResults {
    data object Success : LoginResults
    data class Error(val message: String) : LoginResults
    data object MustChangePassword : LoginResults
}

fun handelLoginResult(loginResults: LoginResults): String {
    return when (loginResults) {
        LoginResults.Success -> "Success"
        LoginResults.MustChangePassword -> "Must Change Password"
        is LoginResults.Error -> "Error : ${loginResults.message}"
    }
}

///Practice

data class BankAccount(
    val id: Int,
    val name: String,
    val balance: Double,
    val status: AccountStatus
)

sealed interface TransferCheckResult {
    data object Allowed : TransferCheckResult
    data class NotAllowed(val reason: String) : TransferCheckResult
}

fun checkTransfer(account: BankAccount, amount: Double): TransferCheckResult {
    if (account.status == AccountStatus.BLOCKED) {
        return TransferCheckResult.NotAllowed("Account is blocked")
    }
    if (account.status == AccountStatus.CLOSED) {
        return TransferCheckResult.NotAllowed("Account is closed")
    }
    if (amount <= 0) {
        return TransferCheckResult.NotAllowed("Amount must be greater than zero")
    }
    if (account.balance < amount) {
        return TransferCheckResult.NotAllowed("Insufficient balance")
    }
    return TransferCheckResult.Allowed
}
fun printTransferResult(result: TransferCheckResult) {
    when (result) {
        TransferCheckResult.Allowed -> {
            println("Transfer allowed")
        }

        is TransferCheckResult.NotAllowed -> {
            println("Transfer not allowed: ${result.reason}")
        }
    }
}