package engsoft.matfit.repository

import android.util.Log
import engsoft.matfit.model.Equipment
import engsoft.matfit.service.EquipamentService

class EquipmentRepository(private val remote: EquipamentService) {
    private val TAG = "EQUIPMENT_REPOSITORY"

    suspend fun registerEquipment(equipment: Equipment): Boolean {
        try {
            val result = remote.registerEquipment(equipment)
            if (result.isSuccessful) {
                result.body()?.let {
                    if (equipment.quantity > 1) {
                        Log.i(TAG, "info_cadastrarEquipamento: Operação bem-sucedida: $it")
                        return true
                    } else {
                        Log.i(TAG,
                            "info_cadastrarEquipamento: Falha! A quantidade não pode ser menor que 1. -> quantidade: ${it.quantity}"
                        )
                        return false
                    }
                }
            } else {
                Log.i(TAG,
                    "info_cadastrarEquipamento: Erro na operação: = ${result.code()} - ${result.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i(TAG, "info_cadastrarEquipamento: Falhou -> ${e.message}")
            e.printStackTrace()
        }
        return false
    }

    suspend fun getEquipment(id: Int): Equipment? {
        try {
            val result = remote.getEquipment(id)
            if (result.isSuccessful) {
                result.body()?.let {
                    if (it.id == id) {
                        Log.i(TAG, "info_buscarEquipamento: Operação bem-sucedida: $it")
                        return it
                    } else {
                        Log.i(TAG, "info_buscarEquipamento: O id recebido não corresponde com o solicitado")
                    }
                }
            } else {
                Log.i(TAG,
                    "info_buscarEquipamento: Erro na operação: = ${result.code()} - ${result.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i(TAG, "info_buscarEquipamento: Falhou -> ${e.message}")
            e.printStackTrace()
        }
        return null
    }

    suspend fun updateEquipment(id: Int, equipment: Equipment): Equipment {
        try {
            val result = remote.updateEquipment(id, equipment)
            if (result.isSuccessful) {
                result.body()?.let {
                    if (id == equipment.id && equipment.quantity > 1) {
                        Log.i(TAG, "info_atualizarEquipamento: Operação bem-sucedida: $it")
                        return it
                    } else {
                        Log.i(TAG, "info_buscarEquipamento: O id recebido não corresponde com o solicitado")
                    }
                }
            } else {
                Log.i(TAG,
                    "info_atualizarEquipamento: Erro na operação: = ${result.code()} - ${result.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i(TAG, "info_atualizarEquipamento: Falhou -> ${e.message}")
            e.printStackTrace()
        }
        return Equipment()
    }

    suspend fun removeEquipment(id: Int): Boolean {
        try {
            val result = remote.removeEquipment(id)
            if (result.isSuccessful) {
                result.body()?.let {
                    Log.i(TAG, "info_deletarEquipamento: Operação bem-sucedida: $it")
                    return it
                }
            } else {
                Log.i(TAG,
                    "info_deletarEquipamento: Erro na operação: = ${result.code()} - ${result.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i(TAG, "info_deletarEquipamento: Falhou -> ${e.message}")
            e.printStackTrace()
        }
        return false
    }

    suspend fun getAllEquipments(): List<Equipment> {
        try {
            val result = remote.getAllEquipments()
            if (result.isSuccessful) {
                result.body()?.let { list ->
                    Log.i(TAG, "info_listarEquipamentos: Operação bem-sucedida: $list")
                    return list
                }
            } else {
                Log.i(TAG,
                    "info_listarEquipamentos: Erro na operação -> ${result.code()} - ${result.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i(TAG, "info_listarEquipamentos: Falhou -> ${e.message}")
            e.printStackTrace()
        }
        return emptyList()
    }
}