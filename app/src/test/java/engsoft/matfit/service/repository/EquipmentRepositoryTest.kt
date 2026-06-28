package engsoft.matfit.service.repository

import com.google.common.truth.Truth.assertThat
import engsoft.matfit.model.Equipment
import engsoft.matfit.repository.EquipmentRepository
import engsoft.matfit.service.EquipamentService
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class EquipmentRepositoryTest {

    @Mock
    private lateinit var mockRemote: EquipamentService

    private lateinit var equipmentRepository: EquipmentRepository
    private lateinit var responseBodyError: ResponseBody

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        // criando instância real do EquipamentoRepository
        equipmentRepository = EquipmentRepository(mockRemote)
        responseBodyError = "Falha na API".toResponseBody("application/json".toMediaTypeOrNull())
    }

    @Test
    fun listarEquipamentos_sucesso_retornaListaEquipamentos() = runTest {
        // DADO -> Sucesso
        val listaEquipamentos = listOf(
            Equipment(1, "pesos", 2),
            Equipment(2, "barra", 3),
            Equipment(3, "halteres", 10),
        )
        whenever(mockRemote.getAllEquipments())
            .thenReturn(Response.success(listaEquipamentos))

        // QUANDO
        val resultado = equipmentRepository.listarEquipamentos()

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isNotEmpty()
        assertThat(resultado).hasSize(3)
    }

    @Test
    fun listarEquipamentos_listaVazia_retornaListaVazia() = runTest {
        // DADO -> falha
        val listaEquipamentos = emptyList<Equipment>()
        whenever(mockRemote.getAllEquipments())
            .thenReturn(Response.success(listaEquipamentos))

        // QUANDO
        val resultado = equipmentRepository.listarEquipamentos()

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEmpty()
    }

    @Test
    fun listarEquipamentos_falhaNaAPI_retornaListaVazia() = runTest {
        // DADO -> falha na API
        whenever(mockRemote.getAllEquipments())
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = equipmentRepository.listarEquipamentos()

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEmpty()
    }

    @Test
    fun cadastrarEquipamento_sucesso_retornaEquipamento() = runTest {
        // DADO -> sucesso
        val equipamento = Equipment(name = "halteres", quantity = 1)
        whenever(mockRemote.cadastrarEquipamento(equipamento))
            .thenReturn(Response.success(equipamento))

        // QUANDO
        val resultado = equipmentRepository.registerEquipment(equipamento)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(resultado)
    }

    @Test
    fun cadastrarEquipamento_falha_retornaFalse() = runTest {
        // DADO -> falha por quantidade ser menor que 1
        val equipamento = Equipment(name = "halteres", quantity = 0)
        whenever(mockRemote.cadastrarEquipamento(equipamento))
            .thenReturn(Response.error(400, responseBodyError))

        // QUANDO
        val resultado = equipmentRepository.registerEquipment(equipamento)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun cadastrarEquipamento_falhaNaAPI_retornaFalse() = runTest {
        // DADO -> falha na API
        val equipamento = Equipment(name = "pesos", quantity = 2)
        whenever(mockRemote.cadastrarEquipamento(equipamento))
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = equipmentRepository.registerEquipment(equipamento)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun getEquipment() = runTest {
        // DADO
        val id = 1
        val equipamentoEsperado = Equipment(id, name = "halteres", quantity = 2)
        whenever(mockRemote.getEquipment(id))
            .thenReturn(Response.success(equipamentoEsperado))

        // QUANDO
        val resultado = equipmentRepository.getEquipment(id)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(equipamentoEsperado)
    }

    @Test
    fun getEquipment_falha_retornaNull() = runTest {
        // DADO
        val id = -1
        whenever(mockRemote.getEquipment(id))
            .thenReturn(Response.error(400, responseBodyError))

        // QUANDO
        val resultado = equipmentRepository.getEquipment(id)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun getEquipment_falhaNaAPI_retornaNull() = runTest {
        // DADO
        val id = 3
        whenever(mockRemote.getEquipment(id))
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = equipmentRepository.getEquipment(id)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun atualizarEquipamento_sucesso_retornaEquipamento() = runTest {
        // DADO -> sucesso = equipamento atualizado
        val id = 1
        val dadosAtualizados = Equipment(id, name = "", quantity = 3)
        val equipamentoEsperado = Equipment(id, "barra", 3)

        whenever(mockRemote.atualizarEquipamento(id, dadosAtualizados))
            .thenReturn(Response.success(equipamentoEsperado))

        // QUANDO
        val resultado = equipmentRepository.updateEquipment(id, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(equipamentoEsperado)
    }

    @Test
    fun atualizarEquipamento_falha_retornaNull() = runTest {
        // DADO -> falha
        val id = 1
        val dadosAtualizados = Equipment(id, name = "", quantity = 0)

        whenever(mockRemote.atualizarEquipamento(id, dadosAtualizados))
            .thenReturn(Response.error(400, responseBodyError))

        // QUANDO
        val resultado = equipmentRepository.updateEquipment(id, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun atualizarEquipamento_falhaNaAPI_retornaNull() = runTest {
        // DADO -> falha na API
        val id = 1
        val dadosAtualizados = Equipment(id, name = "", quantity = 3)

        whenever(mockRemote.atualizarEquipamento(id, dadosAtualizados))
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = equipmentRepository.updateEquipment(id, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun removeEquipment_sucesso_retornaTrue() = runTest {
        // DADO
        val id = 2
        whenever(mockRemote.removeEquipment(id))
            .thenReturn(Response.success(true))

        // QUANDO
        val resultado = equipmentRepository.removeEquipment(id)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isTrue()
    }

    @Test
    fun removeEquipment_falha_retornaFalse() = runTest {
        // DADO
        val id = 2
        whenever(mockRemote.removeEquipment(id))
            .thenReturn(Response.error(400, responseBodyError))

        // QUANDO
        val resultado = equipmentRepository.removeEquipment(id)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun removeEquipment_falhaNaAPI_retornaFalse() = runTest {
        // DADO
        val id = 2
        whenever(mockRemote.removeEquipment(id))
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = equipmentRepository.removeEquipment(id)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }
}