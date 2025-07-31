package engsoft.matfit.events

import engsoft.matfit.model.StudentRequestDTO
import engsoft.matfit.model.StudentUpdateDTO

interface StudentEvent {

    data object getAllStudents: StudentEvent

    data class addStudents(val student: StudentRequestDTO): StudentEvent
    data class deleteStudents(val cpf: String): StudentEvent
    data class getStudentByCpf(val cpf: String): StudentEvent
    data class updateStudents(val cpf: String, val studant: StudentUpdateDTO): StudentEvent
    data class doPayment(val cpf: String): StudentEvent
    data class verifyPayment(val cpf: String): StudentEvent

}