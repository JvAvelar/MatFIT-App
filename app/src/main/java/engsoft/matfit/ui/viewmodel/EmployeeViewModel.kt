package engsoft.matfit.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import engsoft.matfit.events.EmployeeEvent
import engsoft.matfit.model.Employee
import engsoft.matfit.model.EmployeeUpdateDTO
import engsoft.matfit.service.EmployeeService
import engsoft.matfit.service.RetrofitService
import engsoft.matfit.service.repository.FuncionarioRepository
import engsoft.matfit.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmployeeViewModel : ViewModel() {

    private val repository =
        FuncionarioRepository(RetrofitService.getService(EmployeeService::class.java))

    private val _update = MutableStateFlow<EmployeeState>(EmployeeState())
    val updateState: Flow<EmployeeState> = _update

    data class EmployeeState(
        val register: Boolean = false,
        val delete: Boolean = false,
        val update: Employee? = Employee(),
        val getEmployee: Employee? = Employee(),
        val requestState: RequestState<List<Employee>> = RequestState.Carregando()
    )

    fun onEvent(event: EmployeeEvent) {
        when (event) {
            EmployeeEvent.OnGetAllEmployee -> getAllEmployee()
            EmployeeEvent.OnBackPressed -> onBackPressed()
            is EmployeeEvent.OnAddEmployee -> registerEmployee(event.employee)
            is EmployeeEvent.OnGetEmployeeByCpf -> getEmployee(event.cpf)
            is EmployeeEvent.OnUpdateEmployee -> updateEmployee(event.cpf, event.employee)
            is EmployeeEvent.OnDeleteEmployee -> deleteEmployee(event.cpf)
        }
    }

    private fun deleteEmployee(cpf: String){
        viewModelScope.launch {
            val result = repository.deletarFuncionario(cpf)
            updateUiState { it.copy(delete = result) }
        }
    }

    private fun updateEmployee(cpf: String, employee: EmployeeUpdateDTO){
        viewModelScope.launch {
            val result = repository.atualizarFuncionario(cpf, employee)
            updateUiState { it.copy(update = result) }
        }
    }

    private fun getEmployee(cpf: String){
        viewModelScope.launch {
            val result = repository.buscarFuncionario(cpf)
            updateUiState { it.copy(getEmployee = result) }
        }
    }

    private fun registerEmployee(employee: Employee) {
        viewModelScope.launch {
            val result = repository.cadastrarFuncionario(employee)
            updateUiState { it.copy(register = result) }
        }
    }

    private fun getAllEmployee() {
        updateUiState { it.copy(requestState = RequestState.Carregando()) }

        viewModelScope.launch {
            try {
                val response = repository.listarFuncionarios()
                if (response.isNotEmpty())
                    updateUiState { it.copy(requestState = RequestState.Sucesso(response)) }
                else
                    updateUiState { it.copy(requestState = RequestState.Sucesso(emptyList())) }
            } catch (e: Exception) {
                updateUiState { it.copy(requestState = RequestState.Erro("List is empty!")) }
            }
        }
    }

    private fun onBackPressed() {
        EmployeeState()
    }

    private fun updateUiState(update: (EmployeeState) -> EmployeeState) {
        _update.update(update)
    }
}