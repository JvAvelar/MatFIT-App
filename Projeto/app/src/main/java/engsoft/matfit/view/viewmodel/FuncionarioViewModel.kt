package engsoft.matfit.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import engsoft.matfit.model.FuncionarioDTO
import engsoft.matfit.model.FuncionarioUpdate
import engsoft.matfit.service.repository.FuncionarioRepository
import engsoft.matfit.util.EstadoRequisicao
import kotlinx.coroutines.launch

class FuncionarioViewModel : ViewModel() {

    private val repository = FuncionarioRepository()

    private val _cadastro = MutableLiveData<Boolean>()
    val cadastroFuncionario: LiveData<Boolean> = _cadastro

    private val _deletar = MutableLiveData<Boolean?>()
    val deletarFuncionario: LiveData<Boolean?> = _deletar

    private val _buscar = MutableLiveData<FuncionarioDTO?>()
    val buscarFuncionario: LiveData<FuncionarioDTO?> = _buscar

    private val _atualizar = MutableLiveData<FuncionarioDTO?>()
    val atualizarFuncionario: LiveData<FuncionarioDTO?> = _atualizar

    private val _estadoRequisicao = MutableLiveData<EstadoRequisicao<List<FuncionarioDTO>>>()
    val estadoRequisicao: LiveData<EstadoRequisicao<List<FuncionarioDTO>>> = _estadoRequisicao


    fun listarFuncionarios() {
        _estadoRequisicao.postValue(EstadoRequisicao.Carregando())

        try {
            viewModelScope.launch {
                val response = repository.listarFuncionarios()
                if (response.isNotEmpty())
                    _estadoRequisicao.postValue(EstadoRequisicao.Sucesso(response))
                else
                    _estadoRequisicao.postValue(EstadoRequisicao.Sucesso(emptyList()))
                Log.i("info_listarFuncionarios", "Sucesso! -> $response")
            }
        } catch (e: Exception) {
            Log.i("info_listarFuncionarios", "Erro! -> ${e.message}")
            _estadoRequisicao.postValue(EstadoRequisicao.Erro("Erro ao buscar funcionários!"))
        }
    }

    fun cadastrarFuncionario(funcionario: FuncionarioDTO) {
        viewModelScope.launch {
            _cadastro.postValue(repository.cadastrarFuncionario(funcionario))
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

    fun atualizarFuncionario(cpf: String, funcionario: FuncionarioUpdate) {
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
            _deletar.postValue(repository.deletarFuncionario(cpf))
            listarFuncionarios()
        }
    }

    fun reseteDeletar() {
        _deletar.postValue(null)
    }
}