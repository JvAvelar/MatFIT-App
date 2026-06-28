package engsoft.matfit.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import engsoft.matfit.model.Equipment
import engsoft.matfit.service.EquipamentService
import engsoft.matfit.service.RetrofitService
import engsoft.matfit.repository.EquipmentRepository
import engsoft.matfit.util.RequestState
import kotlinx.coroutines.launch

class EquipamentoViewModel : ViewModel() {

    private val repository =
        EquipmentRepository(RetrofitService.getService(EquipamentService::class.java))

    private val _buscarEquipamento = MutableLiveData<Equipment?>()
    val buscarEquipamento: LiveData<Equipment?> = _buscarEquipamento

    private val _cadastrar = MutableLiveData<Boolean>()
    val cadastrar: LiveData<Boolean> = _cadastrar

    private val _deletar = MutableLiveData<Boolean?>()
    val deletar: LiveData<Boolean?> = _deletar

    private val _atualizar = MutableLiveData<Equipment?>()
    val atualizar: LiveData<Equipment?> = _atualizar

    private val _estadoRequisicao = MutableLiveData<RequestState<List<Equipment>>>()
    val estadoRequisicao: LiveData<RequestState<List<Equipment>>> = _estadoRequisicao

    fun listarEquipamentos() {
        _estadoRequisicao.postValue(RequestState.Carregando())
        viewModelScope.launch {
            try {
                val response = repository.getAllEquipments()

                if (response.isNotEmpty())
                    _estadoRequisicao.postValue(RequestState.Sucesso(response))
                else
                    _estadoRequisicao.postValue(RequestState.Sucesso(emptyList()))
                Log.i("info_listarEquipamento", "Sucesso! -> $response")

            } catch (e: Exception) {
                Log.i("info_listarEquipamento", "Erro! -> ${e.message}")
                _estadoRequisicao.postValue(RequestState.Erro("Lista vazia!"))
            }
        }
    }

    fun cadastrarEquipamento(equipamentoDTO: Equipment) {
        viewModelScope.launch {
            try {
                _cadastrar.postValue(repository.registerEquipment(equipamentoDTO))
            } catch (e: Exception) {
                _cadastrar.postValue(false)
                e.printStackTrace()
            }
        }
    }

    fun atualizarEquipamento(id: Int, equipamentoDTO: Equipment) {
        viewModelScope.launch {
            try {
                _atualizar.postValue(repository.updateEquipment(id, equipamentoDTO))
                Log.i("info_atualizarEquipamento", "Sucesso ao atualizar equipamento! -> id = $id")
            } catch (e: Exception) {
                Log.i("info_atualizarEquipamento", "ERRO ao atualizar equipamento! ${e.message}")
                _atualizar.postValue(null)
                e.printStackTrace()
            }
        }
    }

    fun deletarEquipamento(id: Int) {
        viewModelScope.launch {
            try {
                _deletar.postValue(repository.removeEquipment(id))
                listarEquipamentos()
            } catch (e: Exception) {
                _deletar.postValue(null)
                e.printStackTrace()
            }
        }
    }

    fun buscarEquipamento(id: Int) {
        viewModelScope.launch {
            try {
                _buscarEquipamento.postValue(repository.getEquipment(id))
            } catch (e: Exception) {
                _buscarEquipamento.postValue(null)
                e.printStackTrace()
            }
        }
    }
}