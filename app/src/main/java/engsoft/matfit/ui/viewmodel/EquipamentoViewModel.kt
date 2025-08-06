package engsoft.matfit.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import engsoft.matfit.model.Equipament
import engsoft.matfit.service.EquipamentService
import engsoft.matfit.service.RetrofitService
import engsoft.matfit.service.repository.EquipamentoRepository
import engsoft.matfit.util.RequestState
import kotlinx.coroutines.launch

class EquipamentoViewModel : ViewModel() {

    private val repository =
        EquipamentoRepository(RetrofitService.getService(EquipamentService::class.java))

    private val _buscarEquipamento = MutableLiveData<Equipament?>()
    val buscarEquipamento: LiveData<Equipament?> = _buscarEquipamento

    private val _cadastrar = MutableLiveData<Boolean>()
    val cadastrar: LiveData<Boolean> = _cadastrar

    private val _deletar = MutableLiveData<Boolean?>()
    val deletar: LiveData<Boolean?> = _deletar

    private val _atualizar = MutableLiveData<Equipament?>()
    val atualizar: LiveData<Equipament?> = _atualizar

    private val _estadoRequisicao = MutableLiveData<RequestState<List<Equipament>>>()
    val estadoRequisicao: LiveData<RequestState<List<Equipament>>> = _estadoRequisicao

    fun listarEquipamentos() {
        _estadoRequisicao.postValue(RequestState.Carregando())
        viewModelScope.launch {
            try {
                val response = repository.listarEquipamentos()

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

    fun cadastrarEquipamento(equipamentoDTO: Equipament) {
        viewModelScope.launch {
            try {
                _cadastrar.postValue(repository.cadastrarEquipamento(equipamentoDTO))
            } catch (e: Exception) {
                _cadastrar.postValue(false)
                e.printStackTrace()
            }
        }
    }

    fun atualizarEquipamento(id: Int, equipamentoDTO: Equipament) {
        viewModelScope.launch {
            try {
                _atualizar.postValue(repository.atualizarEquipamento(id, equipamentoDTO))
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
                _deletar.postValue(repository.deletarEquipamento(id))
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
                _buscarEquipamento.postValue(repository.buscarEquipamento(id))
            } catch (e: Exception) {
                _buscarEquipamento.postValue(null)
                e.printStackTrace()
            }
        }
    }
}