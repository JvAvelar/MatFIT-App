package engsoft.matfit.repository

import android.util.Log
import engsoft.matfit.model.Employee
import engsoft.matfit.model.EmployeeUpdateDTO
import engsoft.matfit.service.EmployeeService

class EmployeeRepository(private val remote: EmployeeService) {
    private val TAG = "EMPLOYEE_REPOSITORY"

    suspend fun getAllEmployees(): List<Employee> {
        try {
            val result = remote.getAllEmployees()
            if (result.isSuccessful) {
                result.body()?.let { list ->
                    Log.i(TAG, "info_listarFuncionario: Operação bem-sucedida = $list")
                    return list
                }
            }
        } catch (e: Exception) {
            Log.i(TAG, "info_listarFuncionario: Falhou -> ${e.message}")
            e.printStackTrace()
        }
        return emptyList()
    }

    suspend fun registerEmployee(funcionario: Employee): Boolean {
        try {
            val result = remote.registerEmployee(funcionario)
            if (result.isSuccessful) {
                result.body()?.let {
                    Log.i(TAG, "info_cadastrarFuncionario: Operação bem-sucedida = $it")
                    return true
                }
            } else {
                Log.i(TAG,
                    "info_cadastrarAluno: Erro na operação: = ${result.code()} - ${result.message()}"
                )
            }

        } catch (e: Exception) {
            Log.i(TAG, "info_cadastrarFuncionario: Falhou -> ${e.message}")
            e.printStackTrace()
        }
        return false
    }

    suspend fun getEmployee(cpf: String): Employee? {
        try {
            val result = remote.getEmployee(cpf)
            if (result.isSuccessful) {
                result.body()?.let {
                    if (it.cpf == cpf) {
                        return it
                        Log.i(TAG, "info_cadastrarFuncionario: Operação bem-sucedida = $it")
                    } else
                        Log.i(TAG,
                        "info_cadastrarFuncionario: CPF retornado não corresponde ao solicitado"
                    )
                }
            } else
                Log.i(TAG,
                    "info_buscarFuncionario: Erro na operação: = ${result.code()} - ${result.message()}"
                )

        } catch (e: Exception) {
            Log.i(TAG, "info_buscarFuncionario: ${e.message}")
            e.printStackTrace()
        }
        return null
    }

    suspend fun updateEmployee(cpf: String, employee: EmployeeUpdateDTO): Employee? {
        try {
            val result = remote.updateEmployee(cpf, employee)
            if (result.isSuccessful) {
                result.body()?.let {
                    Log.i(TAG, "info_atualizarFuncionario: Operação bem-sucedida = $it")
                    return it
                }
            } else {
                Log.i(TAG,
                    "info_atualizarFuncionario: Erro na operação: = ${result.code()} - ${result.message()}"
                )
            }

        } catch (e: Exception) {
            Log.i("info_atualizarFuncionario", "${e.message}")
            e.printStackTrace()
        }
        return null
    }

    suspend fun removeEmployee(cpf: String): Boolean {
        try {
            val result = remote.removeEmployee(cpf)
            if (result.isSuccessful) {
                result.body()?.let {
                    Log.i(TAG, "info_deletarFuncionario: Operação bem-sucedida = $it")
                    return it
                }
            } else {
                Log.i(TAG,
                    "info_deletarFuncionario: Erro na operação: = ${result.code()} - ${result.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i(TAG, "info_deletarFuncionario: ${e.message}")
            e.printStackTrace()
        }
        return false
    }
}