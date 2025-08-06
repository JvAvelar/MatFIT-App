package engsoft.matfit.service

import engsoft.matfit.model.Equipament
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
    suspend fun listarEquipamentos(): Response<List<Equipament>>

    @GET("/equipamento/{id}")
    @Headers("Content-Type: application/json")
    suspend fun buscarEquipamento(
        @Path("id") id: Int
    ): Response<Equipament>

    @POST("/equipamento")
    @Headers("Content-Type: application/json")
    suspend fun cadastrarEquipamento(
        @Body equipamento: Equipament
    ): Response<Equipament>

    @PUT("/equipamento/{id}")
    @Headers("Content-Type: application/json")
    suspend fun atualizarEquipamento(
        @Path("id") id: Int,
        @Body equipamento: Equipament,
    ): Response<Equipament>

    @HTTP(method = "DELETE", path = "/equipamento/{id}", hasBody = true)
    @Headers("Content-Type: application/json")
    suspend fun deletarEquipamento(
        @Path("id") id: Int
    ): Response<Boolean>

}