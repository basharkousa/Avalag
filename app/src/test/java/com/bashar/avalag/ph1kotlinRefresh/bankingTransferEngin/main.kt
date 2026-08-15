package com.bashar.avalag.ph1kotlinRefresh.bankingTransferEngin

import com.bashar.avalag.ph1kotlinRefresh.bankingTransferEngin.exceptions.AccountBlockedException
import com.bashar.avalag.ph1kotlinRefresh.bankingTransferEngin.exceptions.AccountClosedException
import com.bashar.avalag.ph1kotlinRefresh.bankingTransferEngin.exceptions.AccountNotFoundException
import com.bashar.avalag.ph1kotlinRefresh.bankingTransferEngin.exceptions.InsufficientBalanceException
import org.junit.Assert
import org.junit.Test
import kotlin.compareTo

enum class AccountStatus {
    ACTIVE, BLOCKED, CLOSED
}

data class BankAccount(
    val id: Int,
    val ownerName: String,
    val balance: Double,
    val accountStatus: AccountStatus,

    )

fun BankAccount.canMakeTransfer(amount: Double): Boolean {
    return balance > 0 && accountStatus == AccountStatus.ACTIVE && amount <= balance
}


enum class TransferType {
    INTERNAL, EXTERNAL, INTERNATIONAL
}

object TransferFeeCalculator {
    fun calculate(
        amount: Double, type: TransferType
    ): Double {
        require(amount > 0) {
            "Money amount should be more than 0"
        }
        return when (type) {
            TransferType.INTERNAL -> 0.0
            TransferType.EXTERNAL -> amount * 0.01
            TransferType.INTERNATIONAL -> amount * 0.025
        }
    }

}

//Make copy() use the same visibility as my constructor.
@ConsistentCopyVisibility
data class TransferRequest private constructor(
    val accountSenderID: Int,
    val accountReceiverId: Int,
    val amount: Double,
    val transferType: TransferType
) {
    //You can add this if u want to remove @ConsistentCopyVisibility
  /*  init {
        require(accountSenderID > 0)
        require(accountReceiverId > 0)
        require(accountSenderID != accountReceiverId)
        require(amount >= 1.0)
    }*/
    companion object {
        const val MINIMUM_TRANSFER_AMOUNT = 1.0

        fun create(
            senderAccountId: Int,
            receiverAccountId: Int,
            amount: Double,
            type: TransferType,
        ): TransferRequest {
            require(senderAccountId > 0 && receiverAccountId > 0) { "ids must be greater than 0!!" }
            require(receiverAccountId != senderAccountId) { "Can't transfer to your account!!" }
            require(amount >= MINIMUM_TRANSFER_AMOUNT) { "Amount must be at least \$MINIMUM_TRANSFER_AMOUNT!!" }

            return TransferRequest(
                accountSenderID = senderAccountId,
                accountReceiverId = receiverAccountId,
                amount = amount,
                transferType = type
            )
        }
    }
}


interface AccountRepository {
    fun findAccount(accountId: Int): BankAccount?
    fun updateAccount(account: BankAccount)
}

class InMemoryAccountRepository(
    private val accounts: MutableList<BankAccount>
) : AccountRepository {

    override fun findAccount(accountId: Int): BankAccount? {
        return accounts.find { accountId == it.id }
    }

    override fun updateAccount(account: BankAccount) {
        val index = accounts.indexOfFirst { it.id == account.id }

        if (index == -1) {
            throw AccountNotFoundException(account.id)
        }

        accounts[index] = account
    }
}

sealed interface TransferResult {

    data class Success(
        val transferredAmount: Double,
        val fee: Double,
        val totalCharged: Double,
        val remainingBalance: Double,
    ) : TransferResult

    data class Failure(val message: String) : TransferResult

}

interface TransferService {
    fun transfer(transferRequest: TransferRequest): TransferResult
}

class BankTransferService(private val accountRepository: AccountRepository) : TransferService {
    override fun transfer(transferRequest: TransferRequest): TransferResult {
        return try {
            executeTransfer(transferRequest)
        } catch (e: Exception) {
            when (e) {
                is AccountNotFoundException -> TransferResult.Failure("Account does not exist")
                is AccountBlockedException -> TransferResult.Failure("Account is blocked")
                is AccountClosedException -> TransferResult.Failure("Account is closed")
                is InsufficientBalanceException -> TransferResult.Failure("Insufficient balance")
                else -> TransferResult.Failure("Unexpected transfer error")
            }

        }
    }

    private fun executeTransfer(request: TransferRequest): TransferResult.Success {
        val accountSender =
            accountRepository.findAccount(request.accountSenderID)
                ?: throw AccountNotFoundException(request.accountSenderID)
        val accountReceiver = accountRepository.findAccount(request.accountReceiverId)
            ?: throw AccountNotFoundException(request.accountReceiverId)
//        if (accountSender?.id == accountReceiver?.id) throw Exception("Can't transfer to your account!!")

        if (accountSender.accountStatus == AccountStatus.BLOCKED) throw AccountBlockedException(
            request.accountSenderID
        )
        if (accountReceiver.accountStatus == AccountStatus.BLOCKED) throw AccountBlockedException(
            request.accountReceiverId
        )

        if (accountSender.accountStatus == AccountStatus.CLOSED) throw AccountClosedException(
            request.accountSenderID
        )
        if (accountReceiver.accountStatus == AccountStatus.CLOSED) throw AccountClosedException(
            request.accountReceiverId
        )

        val fee = TransferFeeCalculator.calculate(request.amount, request.transferType)
        val totalCharged = request.amount + fee
        if (totalCharged > accountSender.balance) throw InsufficientBalanceException(
            accountSender.balance, totalCharged
        )

        val updatedSender =
            accountSender.copy(balance = accountSender.balance - totalCharged)
        val updatedReceiver =
            accountReceiver.copy(balance = accountReceiver.balance + request.amount)

        accountRepository.updateAccount(updatedSender)
        accountRepository.updateAccount(updatedReceiver)

        return TransferResult.Success(
            request.amount, fee, totalCharged, updatedSender.balance
        )
    }
}


//    Part 10 — Tests
class BankFunctionalityTests {
    @Test
    fun `internal transfer has zero fee`() {

        Assert.assertEquals(
            0.0, TransferFeeCalculator.calculate(
                100.0,
                TransferType.INTERNAL,
            ), 0.001
        )

    }

    @Test
    fun `external transfer has one percent fee`() {
        val fee = TransferFeeCalculator.calculate(100.0, TransferType.EXTERNAL)

        Assert.assertEquals(1.0, fee, 0.001)

        val fee2 = TransferFeeCalculator.calculate(220.0, TransferType.EXTERNAL)
        Assert.assertEquals(2.20, fee2, 0.001)

    }

    @Test
    fun `international transfer has two and half percent fee`() {
        val fee = TransferFeeCalculator.calculate(100.0, TransferType.INTERNATIONAL)
        Assert.assertEquals(2.5, fee, 0.001)
    }


    @Test
    fun `transfer request rejects identical accounts`() {
        val exception = Assert.assertThrows(
            IllegalArgumentException::class.java
        ) {
            TransferRequest.create(
                senderAccountId = 1,
                receiverAccountId = 1,
                amount = 100.0,
                type = TransferType.EXTERNAL,
            )
        }

        Assert.assertEquals(
            "Can't transfer to your account!!",
            exception.message,
        )

    }

    @Test
    fun `transfer request rejects invalid amount`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            TransferRequest.create(1, 2, 0.5, TransferType.EXTERNAL)
        }
    }

    @Test
    fun `internal transfer succeeds`() {
        val sender = BankAccount(1, "Bashar", 1000.0, AccountStatus.ACTIVE)
        val receiver = BankAccount(2, "Ali", 500.0, AccountStatus.ACTIVE)
        val accountRepository = InMemoryAccountRepository(mutableListOf(sender, receiver))
        val transferRequest = TransferRequest.create(
            sender.id, receiver.id, 200.0, TransferType.INTERNAL
        )
        val transferService = BankTransferService(accountRepository)

        Assert.assertEquals(
            TransferResult.Success(
                200.0, 0.0, 200.0, 800.0
            ), transferService.transfer(transferRequest)
        )

        Assert.assertEquals(800.0, accountRepository.findAccount(sender.id)?.balance ?: 0.0, 0.001)
        Assert.assertEquals(
            700.0, accountRepository.findAccount(receiver.id)?.balance ?: 0.0, 0.001
        )
    }

    @Test
    fun `external transfer deducts amount plus fee`() {
        val sender = BankAccount(1, "Bashar", 1000.0, AccountStatus.ACTIVE)
        val receiver = BankAccount(2, "Ali", 500.0, AccountStatus.ACTIVE)
        val accountRepository = InMemoryAccountRepository(mutableListOf(sender, receiver))
        val transferRequest = TransferRequest.create(
            sender.id, receiver.id, 200.0, TransferType.EXTERNAL
        )
        val transferService = BankTransferService(accountRepository)

        Assert.assertEquals(
            TransferResult.Success(
                200.0, 2.0, 202.0, 798.0
            ), transferService.transfer(transferRequest)
        )

        Assert.assertEquals(798.0, accountRepository.findAccount(sender.id)?.balance ?: 0.0, 0.001)
        Assert.assertEquals(
            700.0, accountRepository.findAccount(receiver.id)?.balance ?: 0.0, 0.001
        )
    }

    @Test
    fun `international transfer deducts correct fee`() {
        val sender = BankAccount(1, "Bashar", 1000.0, AccountStatus.ACTIVE)
        val receiver = BankAccount(2, "Ali", 500.0, AccountStatus.ACTIVE)
        val accountRepository = InMemoryAccountRepository(mutableListOf(sender, receiver))
        val transferRequest = TransferRequest.create(
            sender.id, receiver.id, 200.0, TransferType.INTERNATIONAL
        )
        val transferService = BankTransferService(accountRepository)

        Assert.assertEquals(
            TransferResult.Success(
                200.0, 5.0, 205.0, 795.0
            ), transferService.transfer(transferRequest)
        )

        Assert.assertEquals(795.0, accountRepository.findAccount(sender.id)?.balance ?: 0.0, 0.001)
        Assert.assertEquals(
            700.0, accountRepository.findAccount(receiver.id)?.balance ?: 0.0, 0.001
        )
    }

    @Test
    fun `transfer fails when sender is blocked`() {
        val sender = BankAccount(1, "Bashar", 1000.0, AccountStatus.BLOCKED)
        val receiver = BankAccount(2, "Ali", 500.0, AccountStatus.ACTIVE)
        val accountRepository = InMemoryAccountRepository(mutableListOf(sender, receiver))
        val transferRequest = TransferRequest.create(
            sender.id, receiver.id, 200.0, TransferType.INTERNATIONAL
        )
        val transferService = BankTransferService(accountRepository)

        Assert.assertEquals(
            TransferResult.Failure(
                "Account is blocked"
            ), transferService.transfer(transferRequest)
        )

    }

    @Test
    fun `transfer fails when sender is closed`() {
        val sender = BankAccount(1, "Bashar", 1000.0, AccountStatus.CLOSED)
        val receiver = BankAccount(2, "Ali", 500.0, AccountStatus.ACTIVE)
        val accountRepository = InMemoryAccountRepository(mutableListOf(sender, receiver))
        val transferRequest = TransferRequest.create(
            sender.id, receiver.id, 200.0, TransferType.INTERNATIONAL
        )
        val transferService = BankTransferService(accountRepository)

        Assert.assertEquals(
            TransferResult.Failure(
                "Account is closed"
            ), transferService.transfer(transferRequest)
        )

    }

    @Test
    fun `transfer fails when sender does not exist`() {
        val sender = BankAccount(1, "Bashar", 1000.0, AccountStatus.CLOSED)
        val receiver = BankAccount(2, "Ali", 500.0, AccountStatus.ACTIVE)
        val accountRepository = InMemoryAccountRepository(mutableListOf(receiver))
        val transferRequest = TransferRequest.create(
            sender.id, receiver.id, 200.0, TransferType.INTERNATIONAL
        )
        val transferService = BankTransferService(accountRepository)

        Assert.assertEquals(
            TransferResult.Failure(
                "Account does not exist"
            ), transferService.transfer(transferRequest)
        )

    }

    @Test
    fun `transfer fails when receiver does not exist`() {
        val sender = BankAccount(1, "Bashar", 1000.0, AccountStatus.CLOSED)
        val receiver = BankAccount(2, "Ali", 500.0, AccountStatus.ACTIVE)
        val accountRepository = InMemoryAccountRepository(mutableListOf(sender))
        val transferRequest = TransferRequest.create(
            sender.id, receiver.id, 200.0, TransferType.INTERNATIONAL
        )
        val transferService = BankTransferService(accountRepository)

        Assert.assertEquals(
            TransferResult.Failure(
                "Account does not exist"
            ), transferService.transfer(transferRequest)
        )

    }

    @Test
    fun `transfer fails when balance cannot cover amount and fee`() {
        val sender = BankAccount(1, "Bashar", 1000.0, AccountStatus.ACTIVE)
        val receiver = BankAccount(2, "Ali", 500.0, AccountStatus.ACTIVE)
        val accountRepository = InMemoryAccountRepository(mutableListOf(sender, receiver))
        val transferRequest = TransferRequest.create(
            sender.id, receiver.id, 976.0, TransferType.INTERNATIONAL
        )
        val transferService = BankTransferService(accountRepository)

        Assert.assertEquals(
            TransferResult.Failure(
                "Insufficient balance"
            ), transferService.transfer(transferRequest)
        )

    }

    @Test
    fun `transfer service works through interface types`() {

        val sender = BankAccount(
            id = 1,
            ownerName = "Bashar",
            balance = 1000.0,
            accountStatus = AccountStatus.ACTIVE,
        )

        val receiver = BankAccount(
            id = 2,
            ownerName = "Ali",
            balance = 500.0,
            accountStatus = AccountStatus.ACTIVE,
        )

        val repository: AccountRepository =
            InMemoryAccountRepository(
                mutableListOf(sender, receiver)
            )

        val transferService: TransferService =
            BankTransferService(repository)

        val request = TransferRequest.create(
            senderAccountId = 1,
            receiverAccountId = 2,
            amount = 100.0,
            type = TransferType.INTERNAL,
        )

        Assert.assertTrue(
            transferService.transfer(request)
                    is TransferResult.Success
        )
    }

}