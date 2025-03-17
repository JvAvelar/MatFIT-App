package engsoft.matfit.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import engsoft.matfit.model.EquipamentoDTO
import engsoft.matfit.service.repository.EquipamentoRepository
import engsoft.matfit.util.EstadoRequisicao
import engsoft.matfit.util.RegraTestCustomizada
import engsoft.matfit.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class EquipamentoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val regra = RegraTestCustomizada()

    @Mock
    private lateinit var mockRepository: EquipamentoRepository

    private lateinit var viewModel: EquipamentoViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = EquipamentoViewModel()
        // usando reflection
        val repositoryField = EquipamentoViewModel::class.java.getDeclaredField("repository")
        repositoryField.isAccessible = true
        repositoryField.set(viewModel, mockRepository)
    }


    @Test
    fun listarEquipamentos_sucesso_retornaListaEquipamentos() = runTest {
        // DADO -> sucesso
        val listaEsperada = listOf(
            EquipamentoDTO(nome = "barra grande", quantidade = 3),
            EquipamentoDTO(nome = "pesos", quantidade = 12)
        )
        whenever(mockRepository.listarEquipamentos())
            .thenReturn(listaEsperada)

        // QUANDO
        viewModel.listarEquipamentos()
        advanceUntilIdle()

        // ENTÃO
        val estado = viewModel.estadoRequisicao.getOrAwaitValue()
        assertThat(estado).isNotNull()
        assertThat(estado).isInstanceOf(EstadoRequisicao.Sucesso::class.java)
        assertThat((estado as EstadoRequisicao.Sucesso).data).isEqualTo(listaEsperada)
    }

    @Test
    fun listarEquipamentos_falha_retornaListaVazia() = runTest {
        // DADO -> falha
        whenever(mockRepository.listarEquipamentos())
            .thenThrow(RuntimeException("Erro Lista vazia!"))

        // QUANDO
        viewModel.listarEquipamentos()
        advanceUntilIdle()

        // ENTÃO
        val estado = viewModel.estadoRequisicao.getOrAwaitValue()
        assertThat(estado).isNotNull()
        assertThat(estado).isInstanceOf(EstadoRequisicao.Erro::class.java)
        assertThat((estado as EstadoRequisicao.Erro).mensagem).isEqualTo("Lista vazia!")
    }

    @Test
    fun cadastrarEquipamento_sucesso_retornaTrue() = runTest {
        // DADO -> sucesso
        val equipamento = EquipamentoDTO(nome = "halteres", quantidade = 3)
        whenever(mockRepository.cadastrarEquipamento(equipamento))
            .thenReturn(true)

        // QUANDO
        viewModel.cadastrarEquipamento(equipamento)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.cadastrar.getOrAwaitValue()
        assertThat(resultado).isNotNull()
        assertThat(resultado).isTrue()
    }

    @Test
    fun cadastrarEquipamento_falha_retornaFalse() = runTest {
        // DADO -> falha
        val equipamento = EquipamentoDTO(nome = "halteres", quantidade = 0)
        whenever(mockRepository.cadastrarEquipamento(equipamento))
            .thenThrow(RuntimeException("Quantidade não pode ser menor que 1!!!"))

        // QUANDO
        viewModel.cadastrarEquipamento(equipamento)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.cadastrar.getOrAwaitValue()
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun atualizarEquipamento_sucesso_retornaEquipamento() = runTest {
        // DADO -> sucesso
        val id = 1
        val equipamentoQtdAtualizada = EquipamentoDTO(id, "halteres", 3)
        val equipamentoEsperado = EquipamentoDTO(id, "halteres", 3)
        whenever(mockRepository.atualizarEquipamento(id, equipamentoQtdAtualizada))
            .thenReturn(equipamentoEsperado)

        // QUANDO
        viewModel.atualizarEquipamento(id, equipamentoQtdAtualizada)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.atualizar.getOrAwaitValue()
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(equipamentoEsperado)
    }

    @Test
    fun atualizarEquipamento_falha_retornaNull() = runTest {
        // DADO -> falha
        val id = 1
        val equipamento = EquipamentoDTO(id, "halteres", 0)
        whenever(mockRepository.atualizarEquipamento(id, equipamento))
            .thenThrow(RuntimeException("Erro! Quantidade não permitida!"))

        // QUANDO
        viewModel.atualizarEquipamento(id, equipamento)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.atualizar.getOrAwaitValue()
        assertThat(resultado).isNull()
    }

    @Test
    fun deletarEquipamento_sucesso_retornaTrue() = runTest {
        // DADO -> sucesso
        val id = 1
        whenever(mockRepository.deletarEquipamento(id))
            .thenReturn(true)

        // QUANDO
        viewModel.deletarEquipamento(id)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.deletar.getOrAwaitValue()
        assertThat(resultado).isNotNull()
        assertThat(resultado).isTrue()
    }

    @Test
    fun deletarEquipamento_falha_retornaNull() = runTest {
        // DADO -> falha
        val id = 0
        whenever(mockRepository.deletarEquipamento(id))
            .thenThrow(RuntimeException("ERRO! id inválido!"))

        // QUANDO
        viewModel.deletarEquipamento(id)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.deletar.getOrAwaitValue()
        assertThat(resultado).isNull()
    }

    @Test
    fun buscarEquipamento_sucesso_retornaEquipamento() = runTest {
        // DADO -> sucesso
        val id = 1
        val equipamentoEsperado = EquipamentoDTO(id, "halteres", 2)

        whenever(mockRepository.buscarEquipamento(id))
            .thenReturn(equipamentoEsperado)

        // QUANDO
        viewModel.buscarEquipamento(id)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.buscarEquipamento.getOrAwaitValue()
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(equipamentoEsperado)
    }

    @Test
    fun buscarEquipamento_falha_retornaNull() = runTest {
        // DADO -> falha
        val id = 0
        whenever(mockRepository.buscarEquipamento(id))
            .thenThrow(RuntimeException("ERRO! Id inválido!"))

        // QUANDO
        viewModel.buscarEquipamento(id)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.buscarEquipamento.getOrAwaitValue()
        assertThat(resultado).isNull()
    }
}