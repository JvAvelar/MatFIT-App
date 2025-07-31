package engsoft.matfit.service.repository

import android.util.Log
import engsoft.matfit.model.Equipament
import engsoft.matfit.service.EquipamentoService

class EquipamentoRepository(private val remote: EquipamentoService) {

    suspend fun cadastrarEquipamento(equipamento: Equipament): Boolean {
        try {
            val retorno = remote.cadastrarEquipamento(equipamento)
            if (retorno.isSuccessful) {
                retorno.body()?.let {
                    if (equipamento.quantidade > 1) {
                        Log.i("info_cadastrarEquipamento", "Operação bem-sucedida: $it")
                        return true
                    } else {
                        Log.i(
                            "info_cadastrarEquipamento",
                            "Falha! A quantidade não pode ser menor que 1. -> quantidade: ${it.quantidade}"
                        )
                        return false
                    }
                }
            } else {
                Log.i(
                    "info_cadastrarEquipamento",
                    "Erro na operação: = ${retorno.code()} - ${retorno.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i("info_cadastrarEquipamento", "Falhou -> ${e.message}")
            e.printStackTrace()
        }
        return false
    }

    suspend fun buscarEquipamento(id: Int): Equipament? {
        try {
            val retorno = remote.buscarEquipamento(id)
            if (retorno.isSuccessful) {
                retorno.body()?.let {
                    if (it.id == id) {
                        Log.i("info_buscarEquipamento", "Operação bem-sucedida: $it")
                        return it
                    } else {
                        Log.i("info_buscarEquipamento", "O id recebido não corresponde com o solicitado")
                    }
                }
            } else {
                Log.i(
                    "info_buscarEquipamento",
                    "Erro na operação: = ${retorno.code()} - ${retorno.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i("info_buscarEquipamento", "Falhou -> ${e.message}")
            e.printStackTrace()
        }
        return null
    }

    suspend fun atualizarEquipamento(id: Int, equipamento: Equipament): Equipament? {
        try {
            val retorno = remote.atualizarEquipamento(id, equipamento)
            if (retorno.isSuccessful) {
                retorno.body()?.let {
                    if (id == equipamento.id && equipamento.quantidade > 1) {
                        Log.i("info_atualizarEquipamento", "Operação bem-sucedida: $it")
                        return it
                    } else {
                        Log.i("info_buscarEquipamento", "O id recebido não corresponde com o solicitado")
                    }
                }
            } else {
                Log.i(
                    "info_atualizarEquipamento",
                    "Erro na operação: = ${retorno.code()} - ${retorno.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i("info_atualizarEquipamento", "Falhou -> ${e.message}")
            e.printStackTrace()
        }
        return null
    }

    suspend fun deletarEquipamento(id: Int): Boolean {
        try {
            val retorno = remote.deletarEquipamento(id)
            if (retorno.isSuccessful) {
                retorno.body()?.let {
                    Log.i("info_deletarEquipamento", "Operação bem-sucedida: $it")
                    return it
                }
            } else {
                Log.i(
                    "info_deletarEquipamento",
                    "Erro na operação: = ${retorno.code()} - ${retorno.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i("info_deletarEquipamento", "Falhou -> ${e.message}")
            e.printStackTrace()
        }
        return false
    }

    suspend fun listarEquipamentos(): List<Equipament> {
        try {
            val retorno = remote.listarEquipamentos()
            if (retorno.isSuccessful) {
                retorno.body()?.let { list ->
                    Log.i("info_listarEquipamentos", "Operação bem-sucedida: $list")
                    return list
                }
            } else {
                Log.i(
                    "info_listarEquipamentos",
                    "Erro na operação -> ${retorno.code()} - ${retorno.message()}"
                )
            }
        } catch (e: Exception) {
            Log.i("info_listarEquipamentos", "Falhou -> ${e.message}")
            e.printStackTrace()
        }
        return emptyList()
    }
}