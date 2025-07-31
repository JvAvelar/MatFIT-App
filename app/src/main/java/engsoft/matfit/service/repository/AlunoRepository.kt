package engsoft.matfit.service.repository

import android.util.Log
import engsoft.matfit.model.Student
import engsoft.matfit.model.StudentRequestDTO
import engsoft.matfit.model.StudentResponseDTO
import engsoft.matfit.model.StudentUpdateDTO
import engsoft.matfit.service.AlunoService

class AlunoRepository(private val remote: AlunoService) {

    suspend fun listarAlunos(): List<Student> {
        try {
            val retorno = remote.listarAlunos()
            if (retorno.isSuccessful) {
                retorno.body()?.let { list ->
                    Log.i("info_listarAlunos", "Operação bem-sucedida = $list")
                    return list
                }
            }
        } catch (e: Exception) {
            Log.i("info_listarAlunos", "Erro durante a execução: ${e.message}")
            e.printStackTrace()
        }
        return emptyList()
    }

    suspend fun cadastrarAluno(aluno: StudentRequestDTO): Boolean {
        try {
            val retorno = remote.cadastrarAluno(aluno)
            if (retorno.isSuccessful) {
                retorno.body()?.let {
                    Log.i("info_cadastrarAluno", "Operação bem-sucedida = $it")
                    return !it.pagamentoAtrasado
                }
            } else {
                Log.i(
                    "info_cadastrarAluno",
                    "Erro na operação: = ${retorno.code()} - ${retorno.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i("info_cadastrarAluno", "Erro durante a execusão: ${e.message}")
            e.printStackTrace()
        }
        return false
    }

    suspend fun deletarAluno(cpf: String): Boolean {
        try {
            val retorno = remote.deletarAluno(cpf)
            if (retorno.isSuccessful) {
                retorno.body()?.let {
                    Log.i("info_deletarAluno", "Operação bem-sucedida = $it")
                    listarAlunos()
                    return it
                }
            } else {
                Log.i(
                    "info_deletarAluno",
                    "Erro na operação: = ${retorno.code()} - ${retorno.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i("info_deletarAluno", "Erro durante a exclusão: ${e.message}")
            e.printStackTrace()
        }
        return false
    }

    suspend fun buscarAluno(cpf: String): StudentResponseDTO? {
        return try {
            val retorno = remote.buscarAluno(cpf)
            if (retorno.isSuccessful) {
                retorno.body()?.let {
                    if (it.cpf == cpf) {
                        Log.i("info_buscarAluno", "Operação bem-sucedida = $it")
                        return it
                    } else {
                        Log.i("info_buscarAluno", "CPF retornado não corresponde ao solicitado")
                    }
                }
            } else {
                Log.i(
                    "info_buscarAluno",
                    "Erro na operação: = ${retorno.code()} - ${retorno.message()}"
                )
            }
            null
        } catch (e: Exception) {
            Log.i("info_buscarAluno", "Erro durante a execução: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    suspend fun atualizarAluno(cpf: String, aluno: StudentUpdateDTO): StudentResponseDTO? {
        try {
            val retorno = remote.atualizarAluno(cpf, aluno)
            if (retorno.isSuccessful) {
                retorno.body()?.let {
                    Log.i("info_atualizarAluno", "Operação bem-sucedida: $it")
                    return it
                }
            } else {
                Log.i(
                    "info_atualizarAluno",
                    "Erro na Operação -> ${retorno.code()} - ${retorno.message()}"
                )
            }

        } catch (e: Exception) {
            Log.i("info_atualizarAluno", "Erro durante a execução ${e.message}")
            e.printStackTrace()
        }
        return null
    }

    suspend fun realizarPagamento(cpf: String): Boolean {
        try {
            val retorno = remote.realizarPagamento(cpf)
            if (retorno.isSuccessful) {
                retorno.body()?.let {
                    Log.i("info_realizarPagamento", "Operação bem-sucedida: $it")
                    return it
                }
            } else {
                Log.i(
                    "info_realizarPagamento",
                    "Erro na Operação -> ${retorno.code()} - ${retorno.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i("info_realizarPagamento", "Erro durante a execução ${e.message}")
            e.printStackTrace()
        }
        return false
    }

    suspend fun verificarPagamento(cpf: String): StudentResponseDTO? {
        try {
            val retorno = remote.verificarPagamento(cpf)
            if (retorno.isSuccessful) {
                retorno.body()?.let {
                    Log.i("info_verificarPagamento", "Operação bem-sucedida: $it")
                    return it
                }
            } else {
                Log.i(
                    "info_verificarPagamento",
                    "Erro na Operação -> ${retorno.code()} - ${retorno.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i("info_verificarPagamento", "Erro durante a execução ${e.message}")
            e.printStackTrace()
        }
        return null
    }
}