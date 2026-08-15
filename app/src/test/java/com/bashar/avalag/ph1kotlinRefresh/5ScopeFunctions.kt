package com.bashar.avalag.ph1kotlinRefresh

import org.junit.Test

/*
*
* let
  also
  apply
  run
  with
*
* They help you write cleaner code when you want to:
handle nullable values
configure objects
perform actions on an object
transform an object
avoid repeating variable names
*
* | Function | Use case              | Object name inside | Returns     |
| -------- | --------------------- | ------------------ | ----------- |
| `let`    | nullable / transform  | `it`               | last line   |
| `also`   | extra action/logging  | `it`               | same object |
| `apply`  | configure object      | `this`             | same object |
| `run`    | calculate from object | `this`             | last line   |
| `with`   | calculate from object | `this`             | last line   |

* */

class ScopeFunctions {

//    Nullable error message
    /*state.errorMessage?.let { message ->
        Text(text = message)
    }*/
    /*
        val result = validateRegisterForm(form).also { result ->
            println("Register validation result: $result")
        }*/

    @Test
    fun main() {

        val account = Account(
            12, "Current Account", 4000.2, "USD", true
        )
        account.iban?.let { iban ->
            println("IBAN $iban")
        }

        val accounts = listOf(
            Account(
                12, "Current Account", 4000.2, "USD", true
            ), Account(
                12, "Saving Account", 5000.2, "EUR", false
            ), Account(
                12, "Orange Account", 5000.2, "USD", true
            )
        )

        val activeAccounts = accounts.filter { it.isActive
        }.also{ println("Active Accounts Number : ${it.size}")}

         account.run { println("$name - $currency - $balance") }

        val form = ProfileForm().apply {
            fullName = "Bashar"
            phone = "0945574056"
            isEditing = true
        }

    }

}

class ProfileForm {
    var fullName: String = ""
    var phone: String = ""
    var isEditing: Boolean = false
}