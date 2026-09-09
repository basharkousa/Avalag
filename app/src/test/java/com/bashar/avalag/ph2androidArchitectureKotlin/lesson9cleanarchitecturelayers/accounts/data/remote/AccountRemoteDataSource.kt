package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accounts.data.remote

import com.bashar.avalag.ph2androidArchitectureKotlin.BankAccountDto

interface AccountRemoteDataSource{
    fun getAccounts():List<BankAccountDto>
}