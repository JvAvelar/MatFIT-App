package engsoft.matfit.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    private const val URL = "https://matfit-api.onrender.com/"

    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> getService(serviceClass: Class<T>): T {
        return getInstance().create(serviceClass)
    }
}