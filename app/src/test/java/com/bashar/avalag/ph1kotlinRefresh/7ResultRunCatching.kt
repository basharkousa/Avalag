package com.bashar.avalag.ph1kotlinRefresh

import org.junit.Assert
import org.junit.Test


//1. Normal try/catch

fun divide(a: Double, b: Double): Double = try {
    a / b
} catch (e: Exception) {
    0.0
}


class ResultRunCatching {

    val result = runCatching {
        10 / 2
    }

    fun main() {
        println(result.getOrNull()) // null

        //Run it safely
        val result = runCatching {
            login(username = "bashar", password = "wrong")
        }

//        if (result.isSuccess) {
//            println("Login success: ${result.getOrNull()}")
//        } else {
//            println("Login failed: ${result.exceptionOrNull()?.message}")
//        }


        //Clear Version
        val message = result.fold(onSuccess = { token ->
            "Login success: $token"
        }, onFailure = { error ->
            "Login failed: ${error.message}"
        })

        println(message)
    }

    @Test
    fun testLoginScenario() {

        Assert.assertEquals(LoginCheckResult.Success("TOKEN_123456"),
            safeLogin("bashar", "secret123"))

        Assert.assertEquals(LoginCheckResult.Failure("Username is required"), safeLogin("", "secret123"))

        Assert.assertEquals(LoginCheckResult.Failure("Wrong password"),
            safeLogin("bashar", "wrong"))



    }

}

fun login(username: String, password: String): String {
    if (username.isBlank()) {
        throw IllegalArgumentException("Username is required")
    }

    if (password != "secret123") {
        throw IllegalStateException("Wrong password")
    }

    return "TOKEN_123456"
}

sealed interface LoginCheckResult {
    data class Success(val token: String) : LoginCheckResult
    data class Failure(val message: String) : LoginCheckResult
}

fun fetchLogin(userName: String, password: String): String {
    if (userName.isBlank()) throw IllegalArgumentException("Username is required")
    if (password.isBlank()) throw IllegalArgumentException("Password is Required")
    if (password != "secret123") throw IllegalStateException("Wrong password")
    return "TOKEN_123456"
}

fun safeLogin(username: String, password: String): LoginCheckResult {
    return runCatching {
        fetchLogin(username, password)
    }.fold(onSuccess = { token ->
        LoginCheckResult.Success(token)
    }, onFailure = { error ->
        LoginCheckResult.Failure(error.message ?: "Unknown error")
    })
}