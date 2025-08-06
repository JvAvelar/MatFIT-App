package engsoft.matfit.events

import engsoft.matfit.model.Employee
import engsoft.matfit.model.EmployeeUpdateDTO

interface EmployeeEvent {
    data object OnGetAllEmployee: EmployeeEvent
    data object OnBackPressed: EmployeeEvent

    data class OnAddEmployee(val employee: Employee): EmployeeEvent
    data class OnGetEmployeeByCpf(val cpf: String): EmployeeEvent
    data class OnUpdateEmployee(val cpf: String, val employee: EmployeeUpdateDTO): EmployeeEvent
    data class OnDeleteEmployee(val cpf: String): EmployeeEvent

}