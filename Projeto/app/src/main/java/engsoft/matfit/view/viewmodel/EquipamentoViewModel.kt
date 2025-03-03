package engsoft.matfit.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import engsoft.matfit.model.EquipamentoDTO
import engsoft.matfit.service.repository.EquipamentoRepository
import engsoft.matfit.util.EstadoRequisicao
import kotlinx.coroutines.launch

class EquipamentoViewModel : ViewModel() {

    private val repository = EquipamentoRepository()

    private val _buscarEquipamento = MutableLiveData<EquipamentoDTO>()
    val buscarEquipamento: LiveData<EquipamentoDTO> = _buscarEquipamento

    private val _cadastrar = MutableLiveData<Boolean>()
    val cadastrar: LiveData<Boolean> = _cadastrar

    private val _deletar = MutableLiveData<Boolean?>()
    val deletar: LiveData<Boolean?> = _deletar

    private val _atualizar = MutableLiveData<EquipamentoDTO?>()
    val atualizar: LiveData<EquipamentoDTO?> = _atualizar

    private val _estadoRequisicao = MutableLiveData<EstadoRequisicao<List<EquipamentoDTO>>>()
    val estadoRequisicao: LiveData<EstadoRequisicao<List<EquipamentoDTO>>> = _estadoRequisicao

    fun listarEquipamentos() {
        _estadoRequisicao.postValue(EstadoRequisicao.Carregando())

        try {
            viewModelScope.launch {
                val response = repository.listarEquipamentos()

                if (response.isNotEmpty())
                    _estadoRequisicao.postValue(EstadoRequisicao.Sucesso(response))
                else
                    _estadoRequisicao.postValue(EstadoRequisicao.Sucesso(emptyList()))
                Log.i("info_listarEquipamento", "Sucesso! -> $response")
            }
        } catch (e: Exception) {
            Log.i("info_listarEquipamento", "Erro! -> ${e.message}")
            _estadoRequisicao.postValue(EstadoRequisicao.Erro("Erro ao buscar equipamentos!"))
        }
    }

    fun cadastrarEquipamento(equipamentoDTO: EquipamentoDTO) {
        viewModelScope.launch {
            _cadastrar.postValue(repository.cadastrarEquipamento(equipamentoDTO))
        }
    }

    fun atualizarEquipamento(id: Int, equipamentoDTO: EquipamentoDTO) {
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
            _deletar.postValue(repository.deletarEquipamento(id))
            listarEquipamentos()
        }
    }

    fun reseteDeletar() {
        _deletar.postValue(null)
    }

    fun buscarEquipamento(id: Int) {
        viewModelScope.launch {
            _buscarEquipamento.postValue(repository.buscarEquipamento(id))
        }
    }
}