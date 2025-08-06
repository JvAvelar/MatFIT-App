package engsoft.matfit.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import engsoft.matfit.model.Employee
import engsoft.matfit.model.EmployeeUpdateDTO
import engsoft.matfit.service.EmployeeService
import engsoft.matfit.service.RetrofitService
import engsoft.matfit.service.repository.FuncionarioRepository
import engsoft.matfit.util.RequestState
import kotlinx.coroutines.launch

class FuncionarioViewModel : ViewModel() {

    private val repository =
        FuncionarioRepository(RetrofitService.getService(EmployeeService::class.java))

    private val _cadastro = MutableLiveData<Boolean>()
    val cadastroFuncionario: LiveData<Boolean> = _cadastro

    private val _deletar = MutableLiveData<Boolean?>()
    val deletarFuncionario: LiveData<Boolean?> = _deletar

    private val _buscar = MutableLiveData<Employee?>()
    val buscarFuncionario: LiveData<Employee?> = _buscar

    private val _atualizar = MutableLiveData<Employee?>()
    val atualizarFuncionario: LiveData<Employee?> = _atualizar

    private val _estadoRequisicao = MutableLiveData<RequestState<List<Employee>>>()
    val estadoRequisicao: LiveData<RequestState<List<Employee>>> = _estadoRequisicao


    fun listarFuncionarios() {
        _estadoRequisicao.postValue(RequestState.Carregando())
        viewModelScope.launch {
            try {
                val response = repository.listarFuncionarios()
                if (response.isNotEmpty())
                    _estadoRequisicao.postValue(RequestState.Sucesso(response))
                else
                    _estadoRequisicao.postValue(RequestState.Sucesso(emptyList()))
                Log.i("info_listarFuncionarios", "Sucesso! -> $response")

            } catch (e: Exception) {
                Log.i("info_listarFuncionarios", "Erro! -> ${e.message}")
                _estadoRequisicao.postValue(RequestState.Erro("Lista vazia!"))
            }
        }
    }

    fun cadastrarFuncionario(funcionario: Employee) {
        viewModelScope.launch {
            try {
                _cadastro.postValue(repository.cadastrarFuncionario(funcionario))
            } catch (e: Exception) {
                e.printStackTrace()
                _cadastro.postValue(false)
            }
        }
    }

    fun buscarFuncionario(cpf: String) {
        viewModelScope.launch {
            try {
                Log.i("info_buscarFuncionarios", "Sucesso ao buscar funcionario! ")
                _buscar.postValue(repository.buscarFuncionario(cpf))
            } catch (e: Exception) {
                Log.i("info_buscarFuncionarios", "Erro na busca de funcionario-> ${e.message}")
                _buscar.postValue(null)
                e.printStackTrace()
            }
        }
    }

    fun atualizarFuncionario(cpf: String, funcionario: EmployeeUpdateDTO) {
        viewModelScope.launch {
            try {
                Log.i("info_atualizarFuncionarios", "Sucesso ao atualizar funcionario! ")
                _atualizar.postValue(repository.atualizarFuncionario(cpf, funcionario))
            } catch (e: Exception) {
                Log.i(
                    "info_atualizarFuncionarios",
                    "Erro ao atualizar funcionario! -> ${e.message}"
                )
                _atualizar.postValue(null)
                e.printStackTrace()
            }
        }
    }

    fun deletarFuncionario(cpf: String) {
        viewModelScope.launch {
            try {
                _deletar.postValue(repository.deletarFuncionario(cpf))
                listarFuncionarios()
            } catch (e: Exception) {
                _deletar.postValue(null)
                e.printStackTrace()
            }
        }
    }
}