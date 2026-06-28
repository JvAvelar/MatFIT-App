package engsoft.matfit.service

import engsoft.matfit.model.Equipment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EquipamentService {

    @GET("/equipamento")
    @Headers("Content-Type: application/json")
    suspend fun getAllEquipments(): Response<List<Equipment>>

    @GET("/equipamento/{id}")
    @Headers("Content-Type: application/json")
    suspend fun getEquipment(
        @Path("id") id: Int
    ): Response<Equipment>

    @POST("/equipamento")
    @Headers("Content-Type: application/json")
    suspend fun registerEquipment(
        @Body equipamento: Equipment
    ): Response<Equipment>

    @PUT("/equipamento/{id}")
    @Headers("Content-Type: application/json")
    suspend fun updateEquipment(
        @Path("id") id: Int,
        @Body equipamento: Equipment,
    ): Response<Equipment>

    @HTTP(method = "DELETE", path = "/equipamento/{id}", hasBody = true)
    @Headers("Content-Type: application/json")
    suspend fun removeEquipment(
        @Path("id") id: Int
    ): Response<Boolean>

}