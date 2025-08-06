package engsoft.matfit.events

import engsoft.matfit.model.StudentRequestDTO
import engsoft.matfit.model.StudentUpdateDTO

interface StudentEvent {
    data object OnGetAllStudents: StudentEvent
    data object OnBackPressed: StudentEvent

    data class OnAddStudent(val student: StudentRequestDTO): StudentEvent
    data class OnDeleteStudent(val cpf: String): StudentEvent
    data class OnGetStudentByCpf(val cpf: String): StudentEvent
    data class OnUpdateStudent(val cpf: String, val student: StudentUpdateDTO): StudentEvent
    data class OnDoPayment(val cpf: String): StudentEvent
    data class OnVerifyPayment(val cpf: String): StudentEvent

}