package com.bashar.avalag.ph1kotlinRefresh.bankingTransferEngin.exceptions

class AccountNotFoundException(
    accountId: Int
) : Exception("Account $accountId Not Found!!")

class AccountBlockedException(
    val accountId: Int,
) : Exception("Account $accountId is Blocked!!")

class AccountClosedException(
    val accountId: Int,
) : Exception("Account $accountId is Closed!!")

class InsufficientBalanceException(
    val availableBalance: Double,
    val requiredAmount: Double,
) : Exception("Insufficient Balance!! Your Balance is $availableBalance and Required $requiredAmount")
