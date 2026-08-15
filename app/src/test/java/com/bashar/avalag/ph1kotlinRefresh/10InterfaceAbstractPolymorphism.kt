package com.bashar.avalag.ph1kotlinRefresh

import org.junit.Assert
import org.junit.Test

interface PaymentService {
    fun pay(amount: Double): String
}

class BankPaymentService : PaymentService {
    override fun pay(amount: Double): String {
        return "Paid $amount Through Bank"
    }
}

class GooglePayService : PaymentService {
    override fun pay(amount: Double): String {
        return "Paid $amount Through Google Pay"
    }
}

class PaypalService : PaymentService {
    override fun pay(amount: Double): String {
        return "Paid $amount Through Paypal"
    }
}

class CheckoutManager(val paymentService: PaymentService) {
    fun checkout(amount: Double) {
        paymentService.pay(amount)
    }
}


class InterfaceAbstractPolymorphismTest {

    fun main() {

        val bankService: PaymentService = BankPaymentService()
        val googlePayService: PaymentService = GooglePayService()
        val paypalService: PaymentService = PaypalService()

        var checkoutManager = CheckoutManager(bankService)
        checkoutManager.checkout(1000.0)
        checkoutManager = CheckoutManager(googlePayService)
        checkoutManager.checkout(500.0)
    }

    @Test
    fun `email service sends valid email`() {
        val notificationSender = NotificationSender(
            notificationService = EmailNotificationService(),
        )

        val result = notificationSender.sendNotification(
            destination = "asef@gmail.com",
            message = "Welcome to our application",
        )

        Assert.assertEquals(
            NotificationResult.Success(
                "Email sent to asef@gmail.com"
            ),
            result,
        )
    }

    @Test
    fun `email service rejects invalid destination`() {
        val notificationSender = NotificationSender(
            notificationService = EmailNotificationService(),
        )

        val result = notificationSender.sendNotification(
            destination = "asefgmail.com",
            message = "Welcome",
        )

        Assert.assertEquals(
            NotificationResult.Failure(
                "Destination must be an email"
            ),
            result,
        )
    }

    @Test
    fun `email service rejects blank message`() {
        val notificationSender = NotificationSender(
            notificationService = EmailNotificationService(),
        )

        val result = notificationSender.sendNotification(
            destination = "asef@gmail.com",
            message = "",
        )

        Assert.assertEquals(
            NotificationResult.Failure(
                "Message can't be empty"
            ),
            result,
        )
    }

    @Test
    fun `sms service sends valid sms`() {
        val notificationSender = NotificationSender(
            notificationService = SmsNotificationService(),
        )

        val result = notificationSender.sendNotification(
            destination = "0612345678",
            message = "Your verification code is 1234",
        )

        Assert.assertEquals(
            NotificationResult.Success(
                "SMS sent to 0612345678"
            ),
            result,
        )
    }

    @Test
    fun `sms service rejects destination containing letters`() {
        val notificationSender = NotificationSender(
            notificationService = SmsNotificationService(),
        )

        val result = notificationSender.sendNotification(
            destination = "06ABC45678",
            message = "Test message",
        )

        Assert.assertEquals(
            NotificationResult.Failure(
                "Destination must contain only digits"
            ),
            result,
        )
    }

    @Test
    fun `sms service rejects short phone number`() {
        val notificationSender = NotificationSender(
            notificationService = SmsNotificationService(),
        )

        val result = notificationSender.sendNotification(
            destination = "0945",
            message = "Test message",
        )

        Assert.assertEquals(
            NotificationResult.Failure(
                "Phone number must contain at least 8 digits"
            ),
            result,
        )
    }

    @Test
    fun `notification sender works with email implementation`() {
        val service: NotificationService =
            EmailNotificationService()

        val notificationSender = NotificationSender(service)

        val result = notificationSender.sendNotification(
            destination = "asef@gmail.com",
            message = "Hello by email",
        )

        Assert.assertEquals(
            NotificationResult.Success(
                "Email sent to asef@gmail.com"
            ),
            result,
        )
    }

    @Test
    fun `notification sender works with sms implementation`() {
        val service: NotificationService =
            SmsNotificationService()

        val notificationSender = NotificationSender(service)

        val result = notificationSender.sendNotification(
            destination = "0612345678",
            message = "Hello by SMS",
        )

        Assert.assertEquals(
            NotificationResult.Success(
                "SMS sent to 0612345678"
            ),
            result,
        )
    }
}


/*
*
* 6. Interfaces can contain properties
interface TransferProvider {

    val providerName: String

    fun transfer(amount: Double): String
}

Implementation:

class InternalTransferProvider : TransferProvider {

    override val providerName: String = "Internal Bank"

    override fun transfer(amount: Double): String {
        return "Transferred $amount through $providerName"
    }
}

The implementing class must provide both:

providerName
transfer()
*
*
* Usage:

val logger = ConsoleLogger()

logger.log("Application started")
logger.logError("Network request failed")

logError() uses the default implementation from the interface.
*
* */

/*
* Interfaces VS Abstract classes
*
* Simple comparison:

Interface
→ Defines what an object can do
→ Supports multiple implementations
→ A class can implement multiple interfaces

Abstract class
→ Defines what an object is
→ Can hold constructor state
→ Can provide shared implementation
→ A class can extend only one class
*
* */

/*Lesson 10 Exercise*/

sealed interface NotificationResult {

    data class Success(
        val message: String,
    ) : NotificationResult

    data class Failure(
        val reason: String,
    ) : NotificationResult
}

interface NotificationService {

    val serviceName: String

    fun send(
        destination: String,
        message: String,
    ): NotificationResult
}

class EmailNotificationService : NotificationService {

    override val serviceName: String = "Email"

    override fun send(
        destination: String,
        message: String,
    ): NotificationResult {
        if (!destination.contains("@")) {
            return NotificationResult.Failure(
                reason = "Destination must be an email",
            )
        }

        if (message.isBlank()) {
            return NotificationResult.Failure(
                reason = "Message can't be empty",
            )
        }

        return NotificationResult.Success(
            message = "Email sent to $destination",
        )
    }
}

class SmsNotificationService : NotificationService {

    override val serviceName: String = "SMS"

    override fun send(
        destination: String,
        message: String,
    ): NotificationResult {
        if (!destination.all(Char::isDigit)) {
            return NotificationResult.Failure(
                reason = "Destination must contain only digits",
            )
        }

        if (destination.length < 8) {
            return NotificationResult.Failure(
                reason = "Phone number must contain at least 8 digits",
            )
        }

        if (message.isBlank()) {
            return NotificationResult.Failure(
                reason = "Message can't be empty",
            )
        }

        return NotificationResult.Success(
            message = "SMS sent to $destination",
        )
    }
}

class NotificationSender(
    private val notificationService: NotificationService,
) {
    fun sendNotification(
        destination: String,
        message: String,
    ): NotificationResult {
        return notificationService.send(
            destination = destination,
            message = message,
        )
    }
}