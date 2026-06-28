package engsoft.matfit.service

import engsoft.matfit.model.Student
import engsoft.matfit.model.StudentRequestDTO
import engsoft.matfit.model.StudentResponseDTO
import engsoft.matfit.model.StudentUpdateDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StudentService {

    @GET("/aluno")
    @Headers("Content-Type: application/json")
    suspend fun getAllStudents(): Response<List<Student>>

    @POST("/aluno")
    @Headers("Content-Type: application/json")
    suspend fun registerStudent(
        @Body aluno: StudentRequestDTO
    ): Response<StudentResponseDTO>

    @HTTP(method = "DELETE", path = "/aluno/{cpf}", hasBody = true)
    @Headers("Content-Type: application/json")
    suspend fun removeStudent(
        @Path("cpf") cpf: String
    ): Response<Boolean>

    @PUT("/aluno/{cpf}")
    @Headers("Content-Type: application/json")
    suspend fun updateStudent(
        @Path("cpf") cpf: String,
        @Body aluno: StudentUpdateDTO
    ): Response<StudentResponseDTO>

    @GET("/aluno/{cpf}")
    @Headers("Content-Type: application/json")
    suspend fun getStudent(
        @Path("cpf") cpf: String
    ): Response<StudentResponseDTO>


    @PATCH("/aluno/pagar/{cpf}")
    @Headers("Content-Type: application/json")
    suspend fun makePayment(
        @Path("cpf") cpf: String
    ): Response<Boolean>

    @PATCH("/aluno/pagamento/{cpf}")
    @Headers("Content-Type: application/json")
    suspend fun verifyPayment(
        @Path("cpf") cpf: String
    ): Response<StudentResponseDTO>

}