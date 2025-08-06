package engsoft.matfit.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import engsoft.matfit.events.EquipamentEvent
import engsoft.matfit.model.Equipament
import engsoft.matfit.service.EquipamentService
import engsoft.matfit.service.RetrofitService
import engsoft.matfit.service.repository.EquipamentoRepository
import engsoft.matfit.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EquipamentViewModel : ViewModel() {

    private val repository =
        EquipamentoRepository(RetrofitService.getService(EquipamentService::class.java))

    private val _update = MutableStateFlow<EquipamentState>(EquipamentState())
    val updateState: Flow<EquipamentState> = _update

    data class EquipamentState(
        val register: Boolean = false,
        val delete: Boolean = false,
        val update: Equipament? = Equipament(),
        val getEquipament: Equipament? = Equipament(),
        val requestState: RequestState<List<Equipament>> = RequestState.Carregando()
    )

    fun onEvent(event: EquipamentEvent){
        when(event){
            EquipamentEvent.OnGetAllEquipament -> getAllEquipament()
            EquipamentEvent.OnBackPressed -> onBackPressed()
            is EquipamentEvent.OnAddEquipament -> registerEquipament(event.equipament)
            is EquipamentEvent.OnGetEquipamentById -> getEquipament(event.id)
            is EquipamentEvent.OnUpdateEquipament -> updateEquipament(event.id, event.equipament)
            is EquipamentEvent.OnDeleteEquipament -> deleteEquipament(event.id)
        }
    }

    private fun deleteEquipament(id: Int){
        viewModelScope.launch {
            val result = repository.deletarEquipamento(id)
            updateUiState { it.copy(delete = result) }
        }
    }

    private fun updateEquipament(id: Int, equipament: Equipament){
        viewModelScope.launch {
            val result = repository.atualizarEquipamento(id, equipament)
            updateUiState { it.copy(update = result) }
        }
    }

    private fun getEquipament(id: Int){
        viewModelScope.launch {
            val result = repository.buscarEquipamento(id)
            updateUiState { it.copy(getEquipament = result) }
        }
    }

    private fun registerEquipament(equipament: Equipament){
        viewModelScope.launch {
            val result = repository.cadastrarEquipamento(equipament)
            updateUiState { it.copy(register = result) }
        }
    }

    private fun getAllEquipament(){
        updateUiState { it.copy(requestState = RequestState.Carregando()) }
        viewModelScope.launch {
            try {
                val response = repository.listarEquipamentos()
                if (response.isNotEmpty())
                    updateUiState { it.copy(requestState = RequestState.Sucesso(response)) }
                else
                    updateUiState { it.copy(requestState = RequestState.Sucesso(emptyList())) }
            } catch (e: Exception) {
                updateUiState { it.copy(requestState = RequestState.Erro("List is empty!")) }
            }
        }
    }

    private fun onBackPressed(){
        EquipamentState()
    }

    private fun updateUiState(update: (EquipamentState) -> EquipamentState) {
        _update.update(update)
    }
}