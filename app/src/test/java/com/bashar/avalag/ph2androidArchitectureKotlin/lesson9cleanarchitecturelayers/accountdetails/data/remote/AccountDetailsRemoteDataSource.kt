package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.data.remote

interface AccountDetailsRemoteDataSource {
    fun getAccountDetails(id: Int): AccountDetailsDto?
}