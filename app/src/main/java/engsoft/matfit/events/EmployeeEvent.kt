package engsoft.matfit.events

import engsoft.matfit.model.Employee
import engsoft.matfit.model.EmployeeUpdateDTO

interface EmployeeEvent {

    data object getAllEmployee: EmployeeEvent

    data class addEmployee(val employee: Employee): EmployeeEvent
    data class getEmployeeByCpf(val cpf: String): EmployeeEvent
    data class updateEmployee(val cpf: String, val employee: EmployeeUpdateDTO): EmployeeEvent
    data class deletarEmployee(val cpf: String): EmployeeEvent

}