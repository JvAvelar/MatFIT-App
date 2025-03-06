package engsoft.matfit.service.repository

import com.google.common.truth.Truth.assertThat
import engsoft.matfit.model.EquipamentoDTO
import engsoft.matfit.service.EquipamentoService
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody

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
            EquipamentoDTO(1, "pesos", 2),
            EquipamentoDTO(2, "barra", 3),
            EquipamentoDTO(3, "halteres", 10),
        )
        Mockito.doReturn(Response.success(listaEquipamentos)).`when`(mockRemote)
            .listarEquipamentos()

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
        val listaEquipamentos = emptyList<EquipamentoDTO>()
        Mockito.doReturn(Response.success(listaEquipamentos)).`when`(mockRemote)
            .listarEquipamentos()

        // QUANDO
        val resultado = equipamentoRepository.listarEquipamentos()

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEmpty()
    }

    @Test
    fun listarEquipamentos_falhaNaAPI_retornaListaVazia() = runTest {
        // DADO -> falha na API
        Mockito.doReturn(Response.error<List<EquipamentoDTO>>(404,responseBodyError))
            .`when`(mockRemote).listarEquipamentos()

        // QUANDO
        val resultado = equipamentoRepository.listarEquipamentos()

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEmpty()
    }

    @Test
    fun cadastrarEquipamento_sucesso_retornaEquipamento() = runTest {
        // DADO -> sucesso
        val equipamento = EquipamentoDTO(nome = "halteres", quantidade = 1)
        Mockito.doReturn(Response.success(equipamento)).`when`(mockRemote)
            .cadastrarEquipamento(equipamento)

        // QUANDO
        val resultado = equipamentoRepository.cadastrarEquipamento(equipamento)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(resultado)
    }

    @Test
    fun cadastrarEquipamento_falha_retornaFalse() = runTest {
        // DADO -> falha por quantidade ser menor que 1
        val equipamento = EquipamentoDTO(nome = "halteres", quantidade = 0)
        Mockito.doReturn(Response.error<EquipamentoDTO>(400, responseBodyError)).`when`(mockRemote)
            .cadastrarEquipamento(equipamento)

        // QUANDO
        val resultado = equipamentoRepository.cadastrarEquipamento(equipamento)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun cadastrarEquipamento_falhaNaAPI_retornaFalse() = runTest {
        // DADO -> falha na API
        val equipamento = EquipamentoDTO(nome = "pesos", quantidade = 2)
        Mockito.doReturn(Response.error<EquipamentoDTO>(404, responseBodyError))
            .`when`(mockRemote).cadastrarEquipamento(equipamento)

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
        val equipamentoEsperado = EquipamentoDTO(id, nome = "halteres", quantidade = 2)
        Mockito.doReturn(Response.success(equipamentoEsperado)).`when`(mockRemote)
            .buscarEquipamento(id)

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
        Mockito.doReturn(Response.error<EquipamentoDTO>(400, responseBodyError)).`when`(mockRemote)
            .buscarEquipamento(id)

        // QUANDO
        val resultado = equipamentoRepository.buscarEquipamento(id)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun buscarEquipamento_falhaNaAPI_retornaNull() = runTest {
        // DADO
        val id = 3
        Mockito.doReturn(Response.error<EquipamentoDTO>(404, responseBodyError))
            .`when`(mockRemote).buscarEquipamento(id)

        // QUANDO
        val resultado = equipamentoRepository.buscarEquipamento(id)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun atualizarEquipamento_sucesso_retornaEquipamento() = runTest {
        // DADO -> sucesso = equipamento atualizado
        val id = 1
        val dadosAtualizados = EquipamentoDTO(id, nome = "", quantidade = 3)
        val equipamentoEsperado = EquipamentoDTO(id, "barra", 3)

        Mockito.doReturn(Response.success(equipamentoEsperado)).`when`(mockRemote)
            .atualizarEquipamento(id, dadosAtualizados)

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
        val dadosAtualizados = EquipamentoDTO(id, nome = "", quantidade = 0)

        Mockito.doReturn(Response.error<EquipamentoDTO>(400, responseBodyError)).`when`(mockRemote)
            .atualizarEquipamento(id, dadosAtualizados)

        // QUANDO
        val resultado = equipamentoRepository.atualizarEquipamento(id, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun atualizarEquipamento_falhaNaAPI_retornaNull() = runTest {
        // DADO -> falha na API
        val id = 1
        val dadosAtualizados = EquipamentoDTO(id, nome = "", quantidade = 3)
        Mockito.doReturn(
            Response.error<EquipamentoDTO>(
                404,
                responseBodyError
            )
        ).`when`(mockRemote).atualizarEquipamento(id, dadosAtualizados)

        // QUANDO
        val resultado = equipamentoRepository.atualizarEquipamento(id, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun deletarEquipamento_sucesso_retornaTrue() = runTest {
        // DADO
        val id = 2
        Mockito.doReturn(Response.success(true)).`when`(mockRemote).deletarEquipamento(id)

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
        Mockito.doReturn(Response.error<Boolean>(400, responseBodyError)).`when`(mockRemote).deletarEquipamento(id)

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
        Mockito.doReturn(Response.error<Boolean>(404, responseBodyError))
            .`when`(mockRemote).deletarEquipamento(id)

        // QUANDO
        val resultado = equipamentoRepository.deletarEquipamento(id)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }
}