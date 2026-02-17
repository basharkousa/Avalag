package com.bashar.avalag.src.features.appversion.data

import com.bashar.avalag.src.core.data.remote.model.ApiEnvelope
import com.bashar.avalag.src.features.appversion.data.remote.AppVersionApi
import com.bashar.avalag.src.features.appversion.data.remote.dto.AppVersionDto
import com.bashar.avalag.src.features.appversion.domain.model.UpdateStatus
import kotlinx.coroutines.runBlocking
import okhttp3.RequestBody
import okio.Buffer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AppVersionRepoTest {


    private class FakeAppVersionApi(
        private val response: ApiEnvelope<AppVersionDto>
    ) : AppVersionApi {

        private fun RequestBody.asUtf8(): String {
            val buffer = Buffer()
            writeTo(buffer)
            return buffer.readUtf8()
        }

        var lastPlatform: String? = null
        var lastVersion: String? = null

        override suspend fun checkAppVersion(
            platform: RequestBody,
            version: RequestBody
        ): ApiEnvelope<AppVersionDto> {
            lastPlatform = platform.asUtf8()
            lastVersion = version.asUtf8()

            return response
        }
    }

    @Test
    fun `repo maps mandatory response and passes platform-version`() = runBlocking {
        val api = FakeAppVersionApi(
            ApiEnvelope(
                status = "success",
                message = "Version app",
                data = AppVersionDto(updateStatus = "mandatory", link = "https://www.apple.com/")
            )
        )
        val repo = AppVersionRepo(api)

        val info = repo.getAppVersionInfo(platform = "android", version = "1.1.0")

        assertEquals("android", api.lastPlatform)
        assertEquals("1.1.0", api.lastVersion)

        assertEquals(UpdateStatus.MANDATORY, info.updateStatus)
        assertEquals("https://www.apple.com/", info.link)
    }

    @Test
    fun `repo throws when data missing`() = runBlocking {
        val api = FakeAppVersionApi(
            ApiEnvelope(
                status = "success",
                message = "Version app",
                data = null
            )
        )
        val repo = AppVersionRepo(api)

        val ex = runCatching {
            repo.getAppVersionInfo(platform = "android", version = "1.1.0")
        }.exceptionOrNull()

        assertNotNull(ex)
        assertTrue(ex!!.message?.contains("Missing data") == true)
    }

    @Test
    fun `repo throws when update_status unknown`() = runBlocking {
        val api = FakeAppVersionApi(
            ApiEnvelope(
                status = "success",
                message = "Version app",
                data = AppVersionDto(updateStatus = "optional", link = "https://example.com")
            )
        )
        val repo = AppVersionRepo(api)

        val ex = runCatching {
            repo.getAppVersionInfo(platform = "android", version = "1.1.0")
        }.exceptionOrNull()

        assertNotNull(ex)
        assertTrue(ex!!.message?.contains("Unknown update_status") == true)
    }

}
