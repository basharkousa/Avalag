package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.domain.usecase

import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.domain.AccountRepository
import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.domain.model.AccountDetails

class GetAccountDetailsUseCase(private val repository: AccountRepository){
    operator fun invoke(id: Int): AccountResult {
        val account = repository.getAccountDetails(id)?: return AccountResult.NotFound
        return if(account.isActive){
            AccountResult.Success(account)
        }else{
            AccountResult.InactiveAccount
        }
    }
}

sealed interface AccountResult {
    data class Success(val accountDetails: AccountDetails) : AccountResult
    data object NotFound : AccountResult
    data object InactiveAccount : AccountResult
}