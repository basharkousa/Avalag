package com.bashar.avalag.src.core.data.remote.api

import retrofit2.Response
import retrofit2.http.GET

interface AppApi {

    /**
     * Keep this interface as the "root" API.
     * Later you can:
     *  - put all endpoints here
     *  - OR split into multiple interfaces (AuthApi, ProductApi, OrderApi...)
     *    and provide them via Retrofit as well.
     */

    // Example safe endpoint (not required to exist; keep as template):
    // @GET("health")
    // suspend fun health(): Response<Unit>

    // For now, leave empty until you start integrating features.
}

/*
interface LastFmApi{

    //@Query("page") int page
//    @QueryMap var params:Map<String, String> = mapOf("" to "")

    @GET(Constants.projects)
    suspend fun getProjects(
//        @Query("apiKey") apiKey: String = BuildConfig.VERSION_NAME

    ):Response<ProjectsResponse>

    */
/*    @GET("?method=library.getartists")
        suspend fun getArtistsRequest(@Query("user") artist: String): Response<ArtistsResponse>

        @GET("?method=artist.search")
        suspend fun getSearchArtistsRequest(@Query("artist") artist: String): Response<ArtistSearchResponse>

        @GET("?method=artist.gettopalbums")
        suspend fun getAlbumsArtistRequest(
            @Query("artist") artist: String,
            @Query("page") page: Int, @Query("limit") limit: Int
        ): Response<AlbumsArtistResponse>


        @GET("?method=album.getinfo")
        suspend fun getAlbumDetailsRequest(
            @Query("mbid") album: String
        ): Response<AlbumDetailsResponse>*//*

}
*/
