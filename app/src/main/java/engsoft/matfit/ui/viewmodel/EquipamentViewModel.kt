package engsoft.matfit.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import engsoft.matfit.events.EquipmentEvent
import engsoft.matfit.model.Equipment
import engsoft.matfit.service.EquipamentService
import engsoft.matfit.service.RetrofitService
import engsoft.matfit.repository.EquipmentRepository
import engsoft.matfit.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EquipamentViewModel : ViewModel() {

    private val repository =
        EquipmentRepository(RetrofitService.getService(EquipamentService::class.java))

    private val _update = MutableStateFlow<EquipamentState>(EquipamentState())
    val updateState: Flow<EquipamentState> = _update

    data class EquipamentState(
        val register: Boolean = false,
        val delete: Boolean = false,
        val update: Equipment? = Equipment(),
        val getEquipment: Equipment? = Equipment(),
        val requestState: RequestState<List<Equipment>> = RequestState.Carregando()
    )

    fun onEvent(event: EquipmentEvent){
        when(event){
            EquipmentEvent.OnGetAllEquipment -> getAllEquipament()
            EquipmentEvent.OnBackPressed -> onBackPressed()
            is EquipmentEvent.OnAddEquipment -> registerEquipament(event.equipment)
            is EquipmentEvent.OnGetEquipmentById -> getEquipament(event.id)
            is EquipmentEvent.OnUpdateEquipment -> updateEquipament(event.id, event.equipment)
            is EquipmentEvent.OnDeleteEquipment -> deleteEquipament(event.id)
        }
    }

    private fun deleteEquipament(id: Int){
        viewModelScope.launch {
            val result = repository.removeEquipment(id)
            updateUiState { it.copy(delete = result) }
        }
    }

    private fun updateEquipament(id: Int, equipment: Equipment){
        viewModelScope.launch {
            val result = repository.updateEquipment(id, equipment)
            updateUiState { it.copy(update = result) }
        }
    }

    private fun getEquipament(id: Int){
        viewModelScope.launch {
            val result = repository.getEquipment(id)
            updateUiState { it.copy(getEquipment = result) }
        }
    }

    private fun registerEquipament(equipment: Equipment){
        viewModelScope.launch {
            val result = repository.registerEquipment(equipment)
            updateUiState { it.copy(register = result) }
        }
    }

    private fun getAllEquipament(){
        updateUiState { it.copy(requestState = RequestState.Carregando()) }
        viewModelScope.launch {
            try {
                val response = repository.getAllEquipments()
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