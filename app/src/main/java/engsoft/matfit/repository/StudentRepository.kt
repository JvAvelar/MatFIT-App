package engsoft.matfit.repository

import android.util.Log
import engsoft.matfit.model.Student
import engsoft.matfit.model.StudentRequestDTO
import engsoft.matfit.model.StudentResponseDTO
import engsoft.matfit.model.StudentUpdateDTO
import engsoft.matfit.service.StudentService

class StudentRepository(private val remote: StudentService) {
    private val TAG = "STUDENT_REPOSITORY"
    suspend fun getAllStudents(): List<Student> {
        try {
            val result = remote.getAllStudents()
            if (result.isSuccessful) {
                result.body()?.let { list ->
                    Log.i(TAG, "info_listarAlunos: Operação bem-sucedida = $list")
                    return list
                }
            }
        } catch (e: Exception) {
            Log.i(TAG, "info_listarAlunos: Erro durante a execução: ${e.message}")
            e.printStackTrace()
        }
        return emptyList()
    }

    suspend fun registerStudent(student: StudentRequestDTO): Boolean {
        try {
            val result = remote.registerStudent(student)
            if (result.isSuccessful) {
                result.body()?.let {
                    Log.i(TAG, "info_cadastrarAluno: Operação bem-sucedida = $it")
                    return !it.latePayment
                }
            } else {
                Log.i(
                    "info_cadastrarAluno",
                    "Erro na operação: = ${result.code()} - ${result.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i(TAG, "info_cadastrarAluno: Erro durante a execusão: ${e.message}")
            e.printStackTrace()
        }
        return false
    }

    suspend fun removeStudent(cpf: String): Boolean {
        try {
            val result = remote.removeStudent(cpf)
            if (result.isSuccessful) {
                result.body()?.let {
                    Log.i(TAG, "info_deletarAluno: Operação bem-sucedida = $it")
                    getAllStudents()
                    return it
                }
            } else {
                Log.i(TAG, "info_deletarAluno: Erro na operação: = ${result.code()} - ${result.message()}")
            }
        } catch (e: Exception) {
            Log.i(TAG, "info_deletarAluno: Erro durante a exclusão: ${e.message}")
            e.printStackTrace()
        }
        return false
    }

    suspend fun getStudent(cpf: String): StudentResponseDTO? {
        return try {
            val result = remote.getStudent(cpf)
            if (result.isSuccessful) {
                result.body()?.let {
                    if (it.cpf == cpf) {
                        Log.i(TAG, "info_buscarAluno: Operação bem-sucedida = $it")
                        return it
                    } else {
                        Log.i(TAG, "info_buscarAluno: CPF retornado não corresponde ao solicitado")
                    }
                }
            } else {
                Log.i(TAG, "info_buscarAluno: Erro na operação: = ${result.code()} - ${result.message()}")
            }
            null
        } catch (e: Exception) {
            Log.i(TAG, "info_buscarAluno: Erro durante a execução: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    suspend fun updateStudent(cpf: String, student: StudentUpdateDTO): StudentResponseDTO? {
        try {
            val result = remote.updateStudent(cpf, student)
            if (result.isSuccessful) {
                result.body()?.let {
                    Log.i(TAG, "info_atualizarAluno: Operação bem-sucedida: $it")
                    return it
                }
            } else {
                Log.i(TAG, "info_atualizarAluno: Erro na Operação -> ${result.code()} - ${result.message()}")
            }

        } catch (e: Exception) {
            Log.i(TAG, "info_atualizarAluno: Erro durante a execução ${e.message}")
            e.printStackTrace()
        }
        return null
    }

    suspend fun makePayment(cpf: String): Boolean {
        try {
            val result = remote.makePayment(cpf)
            if (result.isSuccessful) {
                result.body()?.let {
                    Log.i(TAG, "info_realizarPagamento: Operação bem-sucedida: $it")
                    return it
                }
            } else {
                Log.i(TAG, "info_realizarPagamento: Erro na Operação -> ${result.code()} - ${result.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i(TAG, "info_realizarPagamento: Erro durante a execução ${e.message}")
            e.printStackTrace()
        }
        return false
    }

    suspend fun verifyPayment(cpf: String): StudentResponseDTO? {
        try {
            val result = remote.verifyPayment(cpf)
            if (result.isSuccessful) {
                result.body()?.let {
                    Log.i(TAG, "info_verificarPagamento: Operação bem-sucedida: $it")
                    return it
                }
            } else {
                Log.i(TAG, "info_verificarPagamento: Erro na Operação -> ${result.code()} - ${result.message()}")
            }
        } catch (e: Exception) {
            Log.i(TAG, "info_verificarPagamento: Erro durante a execução ${e.message}")
            e.printStackTrace()
        }
        return null
    }
}