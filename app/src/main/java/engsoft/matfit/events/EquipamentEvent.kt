package engsoft.matfit.events

import engsoft.matfit.model.Equipament

interface EquipamentEvent {
    data object getAllEquipament: EquipamentEvent

    data class addEquipament(val equipament: Equipament): EquipamentEvent
    data class getEquipamentById(val id: Int): EquipamentEvent
    data class updateEquipament(val id: Int, val equipament: Equipament): EquipamentEvent
    data class deleteEquipament(val id: Int): EquipamentEvent
    
}