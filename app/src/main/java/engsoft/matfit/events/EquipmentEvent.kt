package engsoft.matfit.events

import engsoft.matfit.model.Equipment

interface EquipmentEvent {
    data object OnGetAllEquipment: EquipmentEvent
    data object OnBackPressed: EmployeeEvent

    data class OnAddEquipment(val equipment: Equipment): EquipmentEvent
    data class OnGetEquipmentById(val id: Int): EquipmentEvent
    data class OnUpdateEquipment(val id: Int, val equipment: Equipment): EquipmentEvent
    data class OnDeleteEquipment(val id: Int): EquipmentEvent
    
}