package engsoft.matfit.service.repository

import android.util.Log
import engsoft.matfit.model.FuncionarioDTO
import engsoft.matfit.model.FuncionarioUpdate
import engsoft.matfit.service.FuncionarioService
import engsoft.matfit.service.RetrofitService

class FuncionarioRepository {

    private val remote = RetrofitService.getService(FuncionarioService::class.java)

    suspend fun listarFuncionarios(): List<FuncionarioDTO> {
        try {
            val retorno = remote.listarFuncionarios()
            if (retorno.isSuccessful) {
                retorno.body()?.let { list ->
                    Log.i("info_listarFuncionario", "Operação bem-sucedida = $list")
                    return list
                }
            }
        } catch (e: Exception) {
            Log.i("info_listarFuncionario", "Falhou -> ${e.message}")
            e.printStackTrace()
        }
        return emptyList()
    }

    suspend fun cadastrarFuncionario(funcionario: FuncionarioDTO): Boolean {
        try {
            val retorno = remote.cadastrarFuncionario(funcionario)
            if (retorno.isSuccessful) {
                retorno.body()?.let {
                    Log.i("info_cadastrarFuncionario", "Operação bem-sucedida = $it")
                    return true
                }
            } else {
                Log.i(
                    "info_cadastrarAluno",
                    "Erro na operação: = ${retorno.code()} - ${retorno.message()}"
                )
            }

        } catch (e: Exception) {
            Log.i("info_cadastrarFuncionario", "Falhou -> ${e.message}")
            e.printStackTrace()
        }
        return false
    }

    suspend fun buscarFuncionario(cpf: String): FuncionarioDTO? {
        return try {
            val retorno = remote.buscarFuncionario(cpf)
            if (retorno.isSuccessful) {
                retorno.body()?.let {
                    if (it.cpf == cpf) {
                        it
                        Log.i("info_cadastrarFuncionario", "Operação bem-sucedida = $it")
                    } else Log.i(
                        "info_cadastrarFuncionario",
                        "CPF retornado não corresponde ao solicitado"
                    )
                }
            } else {
                Log.i(
                    "info_buscarFuncionario",
                    "Erro na operação: = ${retorno.code()} - ${retorno.message()}"
                )
            }
            null
        } catch (e: Exception) {
            Log.i("info_buscarFuncionario", "${e.message}")
            e.printStackTrace()
            null
        }
    }

    suspend fun atualizarFuncionario(cpf: String, funcionario: FuncionarioUpdate): FuncionarioDTO? {
        try {
            val retorno = remote.atualizarFuncionario(cpf, funcionario)
            if (retorno.isSuccessful) {
                retorno.body()?.let {
                    Log.i("info_atualizarFuncionario", "Operação bem-sucedida = $it")
                    return it
                }
            } else {
                Log.i(
                    "info_atualizarFuncionario",
                    "Erro na operação: = ${retorno.code()} - ${retorno.message()}"
                )
            }

        } catch (e: Exception) {
            Log.i("info_atualizarFuncionario", "${e.message}")
            e.printStackTrace()
        }
        return null
    }

    suspend fun deletarFuncionario(cpf: String): Boolean {
        try {
            val retorno = remote.deletarFuncionario(cpf)
            if (retorno.isSuccessful) {
                retorno.body()?.let {
                    Log.i("info_deletarFuncionario", "Operação bem-sucedida = $it")
                    return it
                }
            } else {
                Log.i(
                    "info_deletarFuncionario",
                    "Erro na operação: = ${retorno.code()} - ${retorno.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i("info_deletarFuncionario", "${e.message}")
            e.printStackTrace()
        }
        return false
    }
}