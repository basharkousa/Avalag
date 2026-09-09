package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.data.remote

class FakeAccountDetailsDataSource : AccountDetailsRemoteDataSource {
    val accounts = listOf<AccountDetailsDto>(
        AccountDetailsDto(
            accountId = 1,
            customerName = "Bashar",
            availableBalance = 1000.0,
            isActive = true
        ),
        AccountDetailsDto(
            accountId = 2,
            customerName = "Ali",
            availableBalance = 1000.0,
            isActive = true
        ),
        AccountDetailsDto(
            accountId = 3,
            customerName = "Sam",
            availableBalance = 1000.0,
            isActive = false
        )
    )

    override fun getAccountDetails(id: Int): AccountDetailsDto? = accounts.find { id == it.accountId }
}