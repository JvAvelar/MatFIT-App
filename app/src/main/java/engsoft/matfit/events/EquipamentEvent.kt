package engsoft.matfit.events

import engsoft.matfit.model.Equipament

interface EquipamentEvent {
    data object OnGetAllEquipament: EquipamentEvent
    data object OnBackPressed: EmployeeEvent

    data class OnAddEquipament(val equipament: Equipament): EquipamentEvent
    data class OnGetEquipamentById(val id: Int): EquipamentEvent
    data class OnUpdateEquipament(val id: Int, val equipament: Equipament): EquipamentEvent
    data class OnDeleteEquipament(val id: Int): EquipamentEvent
    
}