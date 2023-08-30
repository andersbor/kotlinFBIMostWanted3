package com.example.fbimostwanted.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.fbimostwanted.models.Catalog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class FBIrepository {
    private val url = "https://api.fbi.gov/wanted/v1/"
    private val fbiService: FBIservice
    val catalogLiveData: MutableLiveData<Catalog> = MutableLiveData<Catalog>()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        //val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            //.addConverterFactory(GsonConverterFactory.create()) // GSON
            //.addConverterFactory(KotlinJsonAdapterFactory)
            .addConverterFactory(MoshiConverterFactory.create()) // Moshi, added to Gradle dependencies
            .build()
        fbiService = build.create(FBIservice::class.java)
        getCatalog(1)
    }

    fun getCatalog(currentPage: Int) {
        fbiService.getCatalog(currentPage).enqueue(object : Callback<Catalog> {
            override fun onResponse(call: Call<Catalog>, response: Response<Catalog>) {
                if (response.isSuccessful) {
                    //Log.d("APPLE", response.body().toString())
                    val b: Catalog? = response.body()
                    catalogLiveData.postValue(b!!)
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<Catalog>, t: Throwable) {
                //booksLiveData.postValue(null)
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }
}