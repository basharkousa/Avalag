package com.bashar.avalag.ph1kotlinRefresh

import org.junit.Test


fun isValidPhone(phone: String): Boolean = phone.length >= 8

fun String.isValid(): Boolean = this.isNotBlank() && this.length >= 8

fun Double.format(): String = "%,.2f".format(this)


fun Account.displayTitle(): String = "$name - $currency"

fun Account.canMakeTransfer(): Boolean = isActive && balance > 0

data class AccountDto(
    val id: Int,
    val account_name: String,
    val balance: Double,
    val currency: String,
    val active: Boolean
)

fun AccountDto.toDomain(): Account =
    Account(id = id, name = account_name, balance = balance, currency = currency, isActive = active)

class ExtensionFunction {
    @Test
    fun main() {
        val phoneNum = "094558"
        println(phoneNum.isValid())

        val balance = 2498.50
        println("Balance: ${balance.format()}")
    }
}