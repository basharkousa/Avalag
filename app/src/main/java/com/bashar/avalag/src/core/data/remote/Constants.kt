package com.bashar.avalag.src.core.data.remote

object Constants {

    /**
     * Base URL MUST end with '/' for Retrofit.
     * Since your API is under /api/, we include it here so endpoints become:
     *   @GET("auth/login")
     *   @GET("products")
     */
    const val BASE_URL: String = "https://adverwize.smarttarget.qa/api/"

}