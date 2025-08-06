package engsoft.matfit.service

import engsoft.matfit.model.Employee
import engsoft.matfit.model.EmployeeUpdateDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EmployeeService {

    @GET("/funcionario/{cpf}")
    @Headers("Content-Type: application/json")
    suspend fun buscarFuncionario(
        @Path(value = "cpf") cpf: String
    ) : Response<Employee>

    @GET("/funcionario")
    @Headers("Content-Type: application/json")
    suspend fun listarFuncionarios() : Response<List<Employee>>

    @POST("/funcionario")
    @Headers("Content-Type: application/json")
    suspend fun cadastrarFuncionario(
        @Body funcionario: Employee
    ) : Response<Employee>

    @PUT("/funcionario/{cpf}")
    @Headers("Content-Type: application/json")
    suspend fun atualizarFuncionario(
        @Path(value = "cpf") cpf: String,
        @Body funcionario: EmployeeUpdateDTO
    ) : Response<Employee>

    @HTTP(method = "DELETE", path = "/funcionario/{cpf}", hasBody = true)
    @Headers("Content-Type: application/json")
    suspend fun deletarFuncionario(
        @Path("cpf") cpf: String
    ) : Response<Boolean>

}