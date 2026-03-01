package com.bashar.avalag.src.features.basics.domain.usecases

import com.bashar.avalag.src.features.basics.domain.model.AppEnums
import com.bashar.avalag.src.features.basics.domain.repositories.IBasicsRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GetEnumsUseCaseTest {

    private class FakeBasicsRepo : IBasicsRepo {
        var getEnumsCalls = 0

        var enumsToReturn: AppEnums = AppEnums(
            categories = mapOf(
                "payment_methods" to mapOf("cash" to "Cash")
            )
        )

        var throwOnGetEnums: Throwable? = null

        override suspend fun getEnums(): AppEnums {
            getEnumsCalls++
            throwOnGetEnums?.let { throw it }
            return enumsToReturn
        }

        override suspend fun getBasicsInfo() = error("Not needed in this test")
    }

    @Test
    fun `usecase returns enums from repo`() = runBlocking {
        val repo = FakeBasicsRepo()
        val useCase = GetEnumsUseCase(repo)

        val result = useCase()

        assertEquals(1, repo.getEnumsCalls)
        assertNotNull(result.categories["payment_methods"])
        assertEquals("Cash", result.categories["payment_methods"]?.get("cash"))
    }

    @Test
    fun `usecase propagates repo exception`() = runBlocking {
        val repo = FakeBasicsRepo().apply {
            throwOnGetEnums = IllegalStateException("boom")
        }
        val useCase = GetEnumsUseCase(repo)

        val ex = runCatching { useCase() }.exceptionOrNull()

        assertNotNull(ex)
        assertTrue(ex is IllegalStateException)
        assertEquals("boom", ex?.message)
        assertEquals(1, repo.getEnumsCalls)
    }
}