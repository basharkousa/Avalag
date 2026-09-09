package com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails

import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.data.mappers.toDomain
import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.data.remote.AccountDetailsDto
import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.data.remote.FakeAccountDetailsDataSource
import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.data.repository.AccountDetailsRepositoryImpl
import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.domain.model.AccountDetails
import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.domain.usecase.AccountResult
import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.domain.usecase.GetAccountDetailsUseCase
import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.presentation.AccountDetailsUiEvent
import com.bashar.avalag.ph2androidArchitectureKotlin.lesson9cleanarchitecturelayers.accountdetails.presentation.AccountDetailsViewModel
import org.junit.Assert
import org.junit.Test

class MainTest {

    @Test
    fun `DTO maps correctly to AccountDetails`(){
        val accountDetailsDto = AccountDetailsDto(1,"Bashar",1000.0,true)
        Assert.assertEquals(
            AccountDetails(1,"Bashar",1000.0,true),
            accountDetailsDto.toDomain()
        )
    }

    @Test
    fun `Repository returns domain model, not DTO`(){
        val accountDetailsAccountRepository = AccountDetailsRepositoryImpl(
            FakeAccountDetailsDataSource()
        )
        Assert.assertEquals(
            AccountDetails(1,"Bashar",1000.0,true),
            accountDetailsAccountRepository.getAccountDetails(1)
        )
    }

    @Test
    fun `Active account → Success`(){
        val accountDetailsAccountRepository = AccountDetailsRepositoryImpl(
            FakeAccountDetailsDataSource()
        )
        val useCase = GetAccountDetailsUseCase(accountDetailsAccountRepository)
        Assert.assertTrue(useCase(2) is AccountResult.Success)
    }
    @Test
    fun `Missing account → NotFound`(){
        val accountDetailsAccountRepository = AccountDetailsRepositoryImpl(
            FakeAccountDetailsDataSource()
        )
        val useCase = GetAccountDetailsUseCase(accountDetailsAccountRepository)
        Assert.assertEquals(AccountResult.NotFound,useCase(10))
    }

    @Test
    fun `Inactive account → InactiveAccount`(){
        val accountDetailsAccountRepository = AccountDetailsRepositoryImpl(
            FakeAccountDetailsDataSource()
        )
        val useCase = GetAccountDetailsUseCase(accountDetailsAccountRepository)
        Assert.assertEquals(AccountResult.InactiveAccount,useCase(3))
    }

    @Test
    fun `ViewModel active account → state contains account and no error`(){
        val viewModel = AccountDetailsViewModel(GetAccountDetailsUseCase(AccountDetailsRepositoryImpl(
            FakeAccountDetailsDataSource()
        )))
        viewModel.onEvent(AccountDetailsUiEvent.LoadAccount(1))
        Assert.assertNotNull(viewModel.state.accountDetails)
        Assert.assertNull(viewModel.state.errorMessage)
    }

    @Test
    fun `ViewModel inactive account → correct error`(){
        val viewModel = AccountDetailsViewModel(GetAccountDetailsUseCase(AccountDetailsRepositoryImpl(
            FakeAccountDetailsDataSource()
        )))
        viewModel.onEvent(AccountDetailsUiEvent.LoadAccount(3))
        Assert.assertNull(viewModel.state.accountDetails)
        Assert.assertEquals("Inactive account!!",viewModel.state.errorMessage)
    }
}