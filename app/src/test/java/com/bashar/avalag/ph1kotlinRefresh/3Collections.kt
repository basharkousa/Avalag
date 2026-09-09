package com.bashar.avalag.ph1kotlinRefresh

import org.junit.Test


/*
* for (i in 1..10)          // 1 to 10
for (i in 1 until 10)     // 1 to 9
for (i in 1..<10)         // 1 to 9
for (i in 10 downTo 1)    // 10 to 1
for (i in 0..10 step 2)   // 0, 2, 4, 6...
for (item in list)         // values
for (i in list.indices)    // indices
for ((i, item) in list.withIndex()) // index + value
* */

class Collections {
    @Test
    fun main() {
        val services = listOf(
            Service(id = 1, title = "Transfer", isEnabled = true, isFavorite = true),
            Service(id = 2, title = "Loan Request", isEnabled = true, isFavorite = false),
            Service(
                id = 3, title = "Account Statement", isEnabled = false, isFavorite = false
            ),
            Service(id = 4, title = "QR Payment", isEnabled = true, isFavorite = true)
        )

        val enabledServices = services.filter { service -> service.isEnabled }.map { it.title }


        /* 4. firstOrNull

         Use firstOrNull when you want to find one item.

         Example:
 */
        val transferService = services.firstOrNull { service ->
            service.title == "Transfer"
        }

        print(enabledServices)

        /*  5. any

          Use any to check if at least one item matches.

          Example:*/

        val hasFavorites = services.any { it.isFavorite }


        /*  6. none

          Use none to check if no item matches.*/

        val noDisabledServices = services.none { !it.isEnabled }

        /* This means:

         Are there no disabled services?*/

        /*  7. count

          Use count to count matching items.
  */
        val favoriteCount = services.count { it.isFavorite }


        /*  8. sortedBy

          Use sortedBy to sort items.*/

        val sortedServices = services.sortedBy { it.title }

//        This sorts alphabetically by title.

        val apiResponse =
            services.filter { it.isEnabled }.sortedByDescending { it.isFavorite }.map { it.title }
    }
}

data class Service(val id: Int, val title: String, val isEnabled: Boolean, val isFavorite: Boolean)

fun main() {
    val accounts = listOf(
        Account(1, "Main Account", 1500.0, "EUR", true),
        Account(2, "Savings", 3000.0, "EUR", true),
        Account(3, "Old Account", 0.0, "EUR", false),
        Account(4, "USD Account", 500.0, "USD", true),
        Account(5, "Blocked Account", 100.0, "EUR", false)
    )

    fun getActiveAccounts(accounts: List<Account>): List<Account> {
        return accounts.filter { it.isActive }
    }

    fun getEuroAccount(accounts: List<Account>): List<Account> =
        accounts.filter { it.currency == "EUR" }

    fun getAccountsNames(accounts: List<Account>): List<String> = accounts.map { it.name }

    fun getAccountById(accounts:List<Account>,id:Int): Account?{
        return accounts.find { account-> account.id == id }
    }
    fun hasBlockedAccounts(accounts: List<Account>): Boolean = accounts.any { !it.isActive }
}

////***** Practice



