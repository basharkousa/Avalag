package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.domain

import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.domain.model.AccountDetails

interface AccountRepository {
    fun getAccountDetails(id:Int): AccountDetails?
}