package com.bashar.avalag

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


sealed interface ValidationResults {
    data object Valid : ValidationResults
    data class Invalid(val message: String) : ValidationResults
}

fun validatePassword(password: String): ValidationResults {
    if (password.length <= 8) {
        return ValidationResults.Invalid("Password Must Be More more or equal 8 chars")
    }
//    if(password.contains(""))
    return ValidationResults.Valid
}

class ExampleUnitTest {

    @Test
    fun passwordIsValid() {
        val result = validatePassword("Bashar88888")
        when (result) {
            ValidationResults.Valid -> {
                print("Valid")
            }

            is ValidationResults.Invalid -> {
                print("Password Is invalid ${result.message}")
            }
        }
        assertEquals(ValidationResults.Valid, result)
    }


    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}