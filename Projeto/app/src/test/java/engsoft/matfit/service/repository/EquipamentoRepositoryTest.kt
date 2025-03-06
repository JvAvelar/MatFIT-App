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

@RunWith(MockitoJUnitRunner::class)
class EquipamentoRepositoryTest {

    @Mock
    private lateinit var mockEquipamentoRepository: EquipamentoRepository

    @Mock
    private lateinit var mockRemote: EquipamentoService

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        // criando instância real do AlunoRepository
        mockEquipamentoRepository = Mockito.spy(EquipamentoRepository())

        // mock do AlunoService via Reflection
        val remoteField = EquipamentoRepository::class.java.getDeclaredField("remote")
        remoteField.isAccessible = true
        remoteField.set(mockEquipamentoRepository, mockRemote)
    }

    @Test
    fun cadastrarEquipamento_sucesso_retornaTrue() = runTest {
        // DADO -> sucesso
        val equipamento = EquipamentoDTO(nome = "halteres", quantidade = 1)
        Mockito.doReturn(true).`when`(mockEquipamentoRepository).cadastrarEquipamento(equipamento)

        // QUANDO
        val resultado = mockEquipamentoRepository.cadastrarEquipamento(equipamento)

        // ENTÃO
        assertThat(resultado).isTrue()
    }

    @Test
    fun cadastrarEquipamento_falha_retornaFalse() = runTest {
        // DADO -> falha por quantidade ser menor que 1
        val equipamento = EquipamentoDTO(nome = "halteres", quantidade = 0)
        Mockito.doReturn(false).`when`(mockEquipamentoRepository).cadastrarEquipamento(equipamento)

        // QUANDO
        val resultado = mockEquipamentoRepository.cadastrarEquipamento(equipamento)

        // ENTÃO
        assertThat(resultado).isFalse()
    }

    @Test
    fun cadastrarEquipamento_falhaNaAPI_retornaFalse() = runTest {
        // DADO -> falha na API
        val equipamento = EquipamentoDTO(nome = "pesos", quantidade = 2)
        Mockito.doReturn(
            Response.error<EquipamentoDTO>(
                404,
                ResponseBody.create(null, "Falha na API")
            )
        ).`when`(mockRemote).cadastrarEquipamento(equipamento)

        // QUANDO
        val resultado = mockEquipamentoRepository.cadastrarEquipamento(equipamento)

        // ENTÃO
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
        val resultado = mockEquipamentoRepository.buscarEquipamento(id)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(equipamentoEsperado)
    }

    @Test
    fun buscarEquipamento_falha_retornaNull() = runTest {
        // DADO
        val id = 1
        val equipamentoEsperado = EquipamentoDTO(id = 3, nome = "halteres", quantidade = 2)
        Mockito.doReturn(Response.success(equipamentoEsperado)).`when`(mockRemote)
            .buscarEquipamento(id)

        // QUANDO
        val resultado = mockEquipamentoRepository.buscarEquipamento(id)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun buscarEquipamento_falhaNaAPI_retornaNull() = runTest {
        // DADO
        val id = 3
        Mockito.doReturn(
            Response.error<EquipamentoDTO>(
                404,
                ResponseBody.create(null, "Falha na API")
            )
        )
            .`when`(mockRemote).buscarEquipamento(id)

        // QUANDO
        val resultado = mockEquipamentoRepository.buscarEquipamento(id)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun atualizarEquipamento_sucesso_retornaEquipamento() = runTest {
        // DADO -> sucesso = equipamento atualizado
        val id = 1
        val exemploEquipamentoJaCadastrado =
            EquipamentoDTO(id, "barra", 1) // alterado apenas a quantidade
        val dadosAtualizados = EquipamentoDTO(id, nome = "", quantidade = 3)
        val equipamentoEsperado = EquipamentoDTO(id, "barra", 3)

        Mockito.doReturn(Response.success(equipamentoEsperado)).`when`(mockRemote)
            .atualizarEquipamento(id, dadosAtualizados)

        // QUANDO
        val resultado = mockEquipamentoRepository.atualizarEquipamento(id, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado!!.quantidade).isEqualTo(equipamentoEsperado.quantidade)
        assertThat(resultado).isEqualTo(equipamentoEsperado)

    }

    @Test
    fun atualizarEquipamento_falha_retornaNull() = runTest {
        // DADO -> falha
        val id = 1
        val exemploEquipamentoJaCadastrado = EquipamentoDTO(id, "barra", 1)
        val dadosAtualizados = EquipamentoDTO(id, nome = "", quantidade = 0)

        Mockito.doReturn(Response.success(null)).`when`(mockRemote)
            .atualizarEquipamento(id, dadosAtualizados)

        // QUANDO
        val resultado = mockEquipamentoRepository.atualizarEquipamento(id, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun atualizarEquipamento_falhaNaAPI_retornaNull() = runTest {
        // DADO -> falha na API
        val id = 1
        val dadosAtualizados = EquipamentoDTO(id, nome = "", quantidade = 3)
        val equipamentoEsperado = EquipamentoDTO(id, "barra", 3)

        Mockito.doReturn(
            Response.error<EquipamentoDTO>(
                404,
                ResponseBody.create(null, "Falha na API")
            )
        ).`when`(mockRemote).atualizarEquipamento(id, dadosAtualizados)

        // QUANDO
        val resultado = mockEquipamentoRepository.atualizarEquipamento(id, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun deletarEquipamento_sucesso_retornaTrue() = runTest {
        // DADO
        val id = 2
        Mockito.doReturn(Response.success(true)).`when`(mockRemote).deletarEquipamento(id)

        // QUANDO
        val resultado = mockEquipamentoRepository.deletarEquipamento(id)

        // ENTÃO
        assertThat(resultado).isTrue()
    }

    @Test
    fun deletarEquipamento_falha_retornaFalse() = runTest {
        // DADO
        val id = 2
        Mockito.doReturn(Response.success(false)).`when`(mockRemote).deletarEquipamento(id)

        // QUANDO
        val resultado = mockEquipamentoRepository.deletarEquipamento(id)

        // ENTÃO
        assertThat(resultado).isFalse()
    }

    @Test
    fun deletarEquipamento_falhaNaAPI_retornaFalse() = runTest {
        // DADO
        val id = 2
        Mockito.doReturn(Response.error<Boolean>(404, ResponseBody.create(null, "Falha na API")))
            .`when`(mockRemote).deletarEquipamento(id)

        // QUANDO
        val resultado = mockEquipamentoRepository.deletarEquipamento(id)

        // ENTÃO
        assertThat(resultado).isFalse()
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
        val resultado = mockEquipamentoRepository.listarEquipamentos()

        // ENTÃO
        assertThat(resultado).isNotEmpty()
        assertThat(resultado).hasSize(3)
    }

    @Test
    fun listarEquipamentos_falha_retornaListaVazia() = runTest {
        // DADO -> falha
        val listaEquipamentos = emptyList<EquipamentoDTO>()

        Mockito.doReturn(Response.success(listaEquipamentos)).`when`(mockRemote)
            .listarEquipamentos()

        // QUANDO
        val resultado = mockEquipamentoRepository.listarEquipamentos()

        // ENTÃO
        assertThat(resultado).isEmpty()
        assertThat(resultado).isNotNull()
    }

    @Test
    fun listarEquipamentos_falhaNaAPI_retornaListaVazia() = runTest {
        // DADO -> falha na API
        val listaEquipamentos = listOf(
            EquipamentoDTO(1, "pesos", 2),
            EquipamentoDTO(2, "barra", 3),
            EquipamentoDTO(3, "halteres", 10),
        )

        Mockito.doReturn(Response.error<List<EquipamentoDTO>>(404, ResponseBody.create(null, "Falha na API")))
            .`when`(mockRemote).listarEquipamentos()

        // QUANDO
        val resultado = mockEquipamentoRepository.listarEquipamentos()

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEmpty()
    }
}