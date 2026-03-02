package com.bashar.avalag.src.core.data.remote.errors

import com.bashar.avalag.R
import com.bashar.avalag.src.core.utils.UiText
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

//todo Adding Unit tests
object NetworkErrorMapper {

    fun toUiText(t: Throwable): UiText {
        return when (t) {
            is UnknownHostException -> UiText.StringResource(R.string.error_no_internet) // add string
            is SocketTimeoutException -> UiText.StringResource(R.string.error_timeout)  // add string
            is IOException -> UiText.StringResource(R.string.error_network_generic)     // add string

            is HttpException -> {
                when (t.code()) {
                    401, 403 -> UiText.StringResource(R.string.error_unauthorized)
                    404 -> UiText.StringResource(R.string.error_not_found)
                    500, 502, 503, 504 -> UiText.StringResource(R.string.error_server)
                    else -> UiText.StringResource(R.string.error_unknown)
                }
            }

            else -> UiText.StringResource(R.string.error_unknown)
        }
    }

}
