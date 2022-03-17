package com.wwinc.moviewall.networking

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.wwinc.moviewall.Model.OtherAppListModel
import com.wwinc.moviewall.Model.VoteUp
import com.wwinc.moviewall.util.Companion.BASEURL
import com.wwinc.moviewall.util.Companion.NETWORK_KEY
import com.wwinc.moviewall.util.Companion.PACKAGENAME

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


private const val BASE_URL =  BASEURL

//private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()


private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
//    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build();

interface NetworkAPIService {
    @GET("com.wwinc.gameWall")
    fun getLinksAsync(): Deferred<List<Wallpaper_ModelItem>>

    @GET("com.wwinc.gameWall")
    fun getTopRated(): Deferred<List<Wallpaper_ModelItem>>

    @FormUrlEncoded
    @POST("votingsys.php")
    fun voteImage(@Field("pack") pack : String, @Field("url") url : String) : Call<VoteUp>


    @GET("layouts/moreApps/getdata.php?pacakgeName=$PACKAGENAME")
    fun othersApplist2 () : Deferred<List<OtherAppListModel>>

}

object WallpaperApi{
    val retrofitService : NetworkAPIService by lazy {
        retrofit.create(NetworkAPIService::class.java)
    }
}