package com.bashar.avalag.ph1kotlinRefresh

import org.junit.Assert
import org.junit.Test


/*
*
* Useful cases for object

An object can be appropriate for:

Stateless calculators
Mappers
Formatters
Application constants
Shared utility functions
Simple in-memory state holders
* */

object CurrencyFormatter {

    fun format(amount: Double, currency: String): String {
        return "$currency ${"%.2f".format(amount)}"
    }
}

val result = CurrencyFormatter.format(
    amount = 1250.5,
    currency = "EUR",
)

//println(result)


//Private constructors with factory functions

class PaymentRequest private constructor(
    val accountId: Int,
    val amount: Double,
) {
    companion object {

        fun create(
            accountId: Int,
            amount: Double,
        ): PaymentRequest {
            require(accountId > 0) {
                "Account ID must be positive"
            }

            require(amount > 0) {
                "Amount must be greater than zero"
            }

            return PaymentRequest(
                accountId = accountId,
                amount = amount,
            )
        }
    }
}

/*
*
* Named companion objects

A companion object may have a name:

class Customer(
    val id: Int,
) {
    companion object Factory {
        fun create(id: Int): Customer {
            return Customer(id)
        }
    }
}

Both forms work:

val first = Customer.create(101)
val second = Customer.Factory.create(102)
* */

//Lesson 9 Exercise


enum class TransferType {
    INTERNAL,
    EXTERNAL,
    INTERNATIONAL
}

object TransferFeeCalculator{
    fun calculateFee(amount: Double, type: TransferType): Double {
        if (amount <= 0) throw IllegalArgumentException("Amount Must Be Greater Than 0!!")
        return when (type) {
            TransferType.INTERNAL -> 0.0
            TransferType.EXTERNAL -> amount * (1 / 100)
            TransferType.INTERNATIONAL -> amount * (2.5 / 100)
        }
    }
}


data class TransferRequest(
    val senderAccountId: Int,
    val receiverAccountId: Int,
    val amount: Double,
    val type: TransferType
) {
    companion object {
        const val MINIMUM_TRANSFER_AMOUNT = 1.0

        fun internal(
            senderAccountId: Int,
            receiverAccountId: Int,
            amount: Double,
        ): TransferRequest {

            require(senderAccountId>0 && receiverAccountId > 0){
                "Account IDs must be positive."
            }
            require(senderAccountId != receiverAccountId){
                "Sender and receiver cannot be identical."
            }
            require( amount>= MINIMUM_TRANSFER_AMOUNT){
                "Amount must be at least MINIMUM_TRANSFER_AMOUNT."
            }

        return TransferRequest(
            senderAccountId = senderAccountId,
            receiverAccountId = receiverAccountId,
            amount = amount,
            type = TransferType.INTERNAL
        )}

        fun international(
            senderAccountId: Int,
            receiverAccountId: Int,
            amount: Double,
        ): TransferRequest {

            require(senderAccountId>0 && receiverAccountId > 0){
                "Account IDs must be positive."
            }
            require(senderAccountId != receiverAccountId){
                "Sender and receiver cannot be identical."
            }
            require( amount>= MINIMUM_TRANSFER_AMOUNT){
                "Amount must be at least MINIMUM_TRANSFER_AMOUNT."
            }

            return TransferRequest(
                senderAccountId = senderAccountId,
                receiverAccountId = receiverAccountId,
                amount = amount,
                type = TransferType.INTERNATIONAL
            )
        }
    }
}


class ObjectCompanionObjectTest {

    private val delta = 0.0001

    @Test
    fun `internal transfer has no fee`() {
        val fee = TransferFeeCalculator.calculateFee(
            amount = 1000.0,
            type = TransferType.INTERNAL,
        )

        Assert.assertEquals(
            0.0,
            fee,
            delta,
        )
    }

    @Test
    fun `external transfer calculates one percent fee`() {
        val fee = TransferFeeCalculator.calculateFee(
            amount = 1000.0,
            type = TransferType.EXTERNAL,
        )

        Assert.assertEquals(
            10.0,
            fee,
            delta,
        )
    }

    @Test
    fun `international transfer calculates two and half percent fee`() {
        val fee = TransferFeeCalculator.calculateFee(
            amount = 1000.0,
            type = TransferType.INTERNATIONAL,
        )

        Assert.assertEquals(
            25.0,
            fee,
            delta,
        )
    }

    @Test
    fun `fee calculator rejects negative amount`() {
        val exception = Assert.assertThrows(
            IllegalArgumentException::class.java,
        ) {
            TransferFeeCalculator.calculateFee(
                amount = -100.0,
                type = TransferType.EXTERNAL,
            )
        }

        Assert.assertEquals(
            "Amount must be greater than zero",
            exception.message,
        )
    }

    @Test
    fun `internal factory creates internal transfer`() {
        val request = TransferRequest.internal(
            senderAccountId = 101,
            receiverAccountId = 202,
            amount = 500.0,
        )

        Assert.assertEquals(101, request.senderAccountId)
        Assert.assertEquals(202, request.receiverAccountId)
        Assert.assertEquals(500.0, request.amount, delta)
        Assert.assertEquals(TransferType.INTERNAL, request.type)
    }

    @Test
    fun `international factory creates international transfer`() {
        val request = TransferRequest.international(
            senderAccountId = 101,
            receiverAccountId = 303,
            amount = 1000.0,
        )

        Assert.assertEquals(TransferType.INTERNATIONAL, request.type)
        Assert.assertEquals(1000.0, request.amount, delta)
    }

    @Test
    fun `factory rejects identical accounts`() {
        val exception = Assert.assertThrows(
            IllegalArgumentException::class.java,
        ) {
            TransferRequest.internal(
                senderAccountId = 101,
                receiverAccountId = 101,
                amount = 100.0,
            )
        }

        Assert.assertEquals(
            "Sender and receiver cannot be identical",
            exception.message,
        )
    }

    @Test
    fun `factory rejects amount below minimum`() {
        val exception = Assert.assertThrows(
            IllegalArgumentException::class.java,
        ) {
            TransferRequest.international(
                senderAccountId = 101,
                receiverAccountId = 202,
                amount = 0.5,
            )
        }

        Assert.assertEquals(
            "Amount must be at least 1.0",
            exception.message,
        )
    }
}