package engsoft.matfit.service.repository

import com.google.common.truth.Truth.assertThat
import engsoft.matfit.model.Equipament
import engsoft.matfit.service.EquipamentoService
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
class EquipamentoRepositoryTest {

    @Mock
    private lateinit var mockRemote: EquipamentoService

    private lateinit var equipamentoRepository: EquipamentoRepository
    private lateinit var responseBodyError: ResponseBody

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        // criando instância real do EquipamentoRepository
        equipamentoRepository = EquipamentoRepository(mockRemote)
        responseBodyError = "Falha na API".toResponseBody("application/json".toMediaTypeOrNull())
    }

    @Test
    fun listarEquipamentos_sucesso_retornaListaEquipamentos() = runTest {
        // DADO -> Sucesso
        val listaEquipamentos = listOf(
            Equipament(1, "pesos", 2),
            Equipament(2, "barra", 3),
            Equipament(3, "halteres", 10),
        )
        whenever(mockRemote.listarEquipamentos())
            .thenReturn(Response.success(listaEquipamentos))

        // QUANDO
        val resultado = equipamentoRepository.listarEquipamentos()

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isNotEmpty()
        assertThat(resultado).hasSize(3)
    }

    @Test
    fun listarEquipamentos_listaVazia_retornaListaVazia() = runTest {
        // DADO -> falha
        val listaEquipamentos = emptyList<Equipament>()
        whenever(mockRemote.listarEquipamentos())
            .thenReturn(Response.success(listaEquipamentos))

        // QUANDO
        val resultado = equipamentoRepository.listarEquipamentos()

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEmpty()
    }

    @Test
    fun listarEquipamentos_falhaNaAPI_retornaListaVazia() = runTest {
        // DADO -> falha na API
        whenever(mockRemote.listarEquipamentos())
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = equipamentoRepository.listarEquipamentos()

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEmpty()
    }

    @Test
    fun cadastrarEquipamento_sucesso_retornaEquipamento() = runTest {
        // DADO -> sucesso
        val equipamento = Equipament(nome = "halteres", quantidade = 1)
        whenever(mockRemote.cadastrarEquipamento(equipamento))
            .thenReturn(Response.success(equipamento))

        // QUANDO
        val resultado = equipamentoRepository.cadastrarEquipamento(equipamento)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(resultado)
    }

    @Test
    fun cadastrarEquipamento_falha_retornaFalse() = runTest {
        // DADO -> falha por quantidade ser menor que 1
        val equipamento = Equipament(nome = "halteres", quantidade = 0)
        whenever(mockRemote.cadastrarEquipamento(equipamento))
            .thenReturn(Response.error(400, responseBodyError))

        // QUANDO
        val resultado = equipamentoRepository.cadastrarEquipamento(equipamento)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun cadastrarEquipamento_falhaNaAPI_retornaFalse() = runTest {
        // DADO -> falha na API
        val equipamento = Equipament(nome = "pesos", quantidade = 2)
        whenever(mockRemote.cadastrarEquipamento(equipamento))
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = equipamentoRepository.cadastrarEquipamento(equipamento)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun buscarEquipamento_sucesso_retornaEquipamento() = runTest {
        // DADO
        val id = 1
        val equipamentoEsperado = Equipament(id, nome = "halteres", quantidade = 2)
        whenever(mockRemote.buscarEquipamento(id))
            .thenReturn(Response.success(equipamentoEsperado))

        // QUANDO
        val resultado = equipamentoRepository.buscarEquipamento(id)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(equipamentoEsperado)
    }

    @Test
    fun buscarEquipamento_falha_retornaNull() = runTest {
        // DADO
        val id = -1
        whenever(mockRemote.buscarEquipamento(id))
            .thenReturn(Response.error(400, responseBodyError))

        // QUANDO
        val resultado = equipamentoRepository.buscarEquipamento(id)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun buscarEquipamento_falhaNaAPI_retornaNull() = runTest {
        // DADO
        val id = 3
        whenever(mockRemote.buscarEquipamento(id))
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = equipamentoRepository.buscarEquipamento(id)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun atualizarEquipamento_sucesso_retornaEquipamento() = runTest {
        // DADO -> sucesso = equipamento atualizado
        val id = 1
        val dadosAtualizados = Equipament(id, nome = "", quantidade = 3)
        val equipamentoEsperado = Equipament(id, "barra", 3)

        whenever(mockRemote.atualizarEquipamento(id, dadosAtualizados))
            .thenReturn(Response.success(equipamentoEsperado))

        // QUANDO
        val resultado = equipamentoRepository.atualizarEquipamento(id, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(equipamentoEsperado)
    }

    @Test
    fun atualizarEquipamento_falha_retornaNull() = runTest {
        // DADO -> falha
        val id = 1
        val dadosAtualizados = Equipament(id, nome = "", quantidade = 0)

        whenever(mockRemote.atualizarEquipamento(id, dadosAtualizados))
            .thenReturn(Response.error(400, responseBodyError))

        // QUANDO
        val resultado = equipamentoRepository.atualizarEquipamento(id, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun atualizarEquipamento_falhaNaAPI_retornaNull() = runTest {
        // DADO -> falha na API
        val id = 1
        val dadosAtualizados = Equipament(id, nome = "", quantidade = 3)

        whenever(mockRemote.atualizarEquipamento(id, dadosAtualizados))
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = equipamentoRepository.atualizarEquipamento(id, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun deletarEquipamento_sucesso_retornaTrue() = runTest {
        // DADO
        val id = 2
        whenever(mockRemote.deletarEquipamento(id))
            .thenReturn(Response.success(true))

        // QUANDO
        val resultado = equipamentoRepository.deletarEquipamento(id)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isTrue()
    }

    @Test
    fun deletarEquipamento_falha_retornaFalse() = runTest {
        // DADO
        val id = 2
        whenever(mockRemote.deletarEquipamento(id))
            .thenReturn(Response.error(400, responseBodyError))

        // QUANDO
        val resultado = equipamentoRepository.deletarEquipamento(id)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun deletarEquipamento_falhaNaAPI_retornaFalse() = runTest {
        // DADO
        val id = 2
        whenever(mockRemote.deletarEquipamento(id))
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = equipamentoRepository.deletarEquipamento(id)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }
}