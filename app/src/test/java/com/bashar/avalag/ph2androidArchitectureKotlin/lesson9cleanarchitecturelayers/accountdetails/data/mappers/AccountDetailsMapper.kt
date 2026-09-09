package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.data.mappers

import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.data.remote.AccountDetailsDto
import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.domain.model.AccountDetails

fun AccountDetailsDto.toDomain(): AccountDetails {
    return AccountDetails(
        id = accountId,
        ownerName = customerName,
        balance = availableBalance,
        isActive = isActive
    )
}