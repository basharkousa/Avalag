package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.data.repository

import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.data.mappers.toDomain
import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.data.remote.AccountDetailsRemoteDataSource
import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.data.remote.FakeAccountDetailsDataSource
import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.domain.AccountRepository
import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.domain.model.AccountDetails


class AccountDetailsRepositoryImpl(private val remoteDataSource: AccountDetailsRemoteDataSource):
    AccountRepository {

    override fun getAccountDetails(id: Int): AccountDetails? {
        return remoteDataSource.getAccountDetails(id)?.toDomain()
    }
}