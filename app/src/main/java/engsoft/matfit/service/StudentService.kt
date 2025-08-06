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
    suspend fun listarAlunos(): Response<List<Student>>

    @POST("/aluno")
    @Headers("Content-Type: application/json")
    suspend fun cadastrarAluno(
        @Body aluno: StudentRequestDTO
    ): Response<StudentResponseDTO>

    @HTTP(method = "DELETE", path = "/aluno/{cpf}", hasBody = true)
    @Headers("Content-Type: application/json")
    suspend fun deletarAluno(
        @Path("cpf") cpf: String
    ): Response<Boolean>

    @PUT("/aluno/{cpf}")
    @Headers("Content-Type: application/json")
    suspend fun atualizarAluno(
        @Path("cpf") cpf: String,
        @Body aluno: StudentUpdateDTO
    ): Response<StudentResponseDTO>

    @GET("/aluno/{cpf}")
    @Headers("Content-Type: application/json")
    suspend fun buscarAluno(
        @Path("cpf") cpf: String
    ): Response<StudentResponseDTO>


    @PATCH("/aluno/pagar/{cpf}")
    @Headers("Content-Type: application/json")
    suspend fun realizarPagamento(
        @Path("cpf") cpf: String
    ): Response<Boolean>

    @PATCH("/aluno/pagamento/{cpf}")
    @Headers("Content-Type: application/json")
    suspend fun verificarPagamento(
        @Path("cpf") cpf: String
    ): Response<StudentResponseDTO>

}