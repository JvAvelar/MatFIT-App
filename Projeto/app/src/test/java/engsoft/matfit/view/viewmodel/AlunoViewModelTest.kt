package engsoft.matfit.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import engsoft.matfit.model.AlunoDTO
import engsoft.matfit.model.AlunoRequest
import engsoft.matfit.model.AlunoResponse
import engsoft.matfit.model.AlunoUpdate
import engsoft.matfit.service.repository.AlunoRepository
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
class AlunoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val regra = RegraTestCustomizada()

    @Mock
    private lateinit var mockRepository: AlunoRepository

    private lateinit var viewModel: AlunoViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = AlunoViewModel()
        // usando reflection
        val repositoryField = AlunoViewModel::class.java.getDeclaredField("repository")
        repositoryField.isAccessible = true
        repositoryField.set(viewModel, mockRepository)
    }


    @Test
    fun listarAlunos_sucesso_retornaListaAlunos() = runTest {
        // DADO -> sucesso
        val listaEsperada = listOf(
            AlunoDTO("129.078.459-12", "Joaquim Abreu", "Musculação"),
            AlunoDTO("038.295.820-99", "Cosmos Dantas", "Crossfit")
        )
        whenever(mockRepository.listarAlunos()).thenReturn(listaEsperada)

        // QUANDO
        viewModel.listarAlunos()

        // Espera a execução assincrona terminar
        advanceUntilIdle()

        // ENTÃO
        val estado = viewModel.estadoRequisicao.getOrAwaitValue()
        assertThat(estado).isNotNull()
        assertThat(estado).isInstanceOf(EstadoRequisicao.Sucesso::class.java)
        assertThat((estado as EstadoRequisicao.Sucesso).data).isEqualTo(listaEsperada)
    }

    @Test
    fun listarAlunos_falha_retornaListaVazia() = runTest {
        // DADO -> falha
        whenever(mockRepository.listarAlunos()).thenReturn(null)

        // QUANDO
        viewModel.listarAlunos()
        advanceUntilIdle()

        // ENTÃO
        val estado = viewModel.estadoRequisicao.getOrAwaitValue()
        assertThat(estado).isNotNull()
        assertThat(estado).isInstanceOf(EstadoRequisicao.Erro::class.java)
        assertThat((estado as EstadoRequisicao.Erro).mensagem).isEqualTo("Lista vazia!")
    }

    @Test
    fun cadastrarAlunos_sucesso_retornaTrue() = runTest {
        // DADO -> sucesso
        val aluno = AlunoRequest("038.295.820-99", "Cosmos Dantas", "Crossfit")
        whenever(mockRepository.cadastrarAluno(aluno)).thenReturn(true)

        // QUANDO
        viewModel.cadastrarAluno(aluno)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.cadastro.getOrAwaitValue()
        assertThat(resultado).isNotNull()
        assertThat(resultado).isTrue()
    }

    @Test
    fun cadastrarAlunos_falha_retornaFalse() = runTest {
        // DADO -> falha
        val aluno = AlunoRequest("123.456.789-10", "Joaquim Abreu", "Musculação")
        whenever(mockRepository.cadastrarAluno(aluno)).thenReturn(false)

        // QUANDO
        viewModel.cadastrarAluno(aluno)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.cadastro.getOrAwaitValue()
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun deletarAluno_sucesso_retornaTrue() = runTest {
        // DADO -> sucesso
        val cpf = "038.295.820-99"
        whenever(mockRepository.deletarAluno(cpf)).thenReturn(true)

        // QUANDO
        viewModel.deletarAluno(cpf)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.deletar.getOrAwaitValue()
        assertThat(resultado).isNotNull()
        assertThat(resultado).isTrue()
    }

    @Test
    fun deletarAluno_falha_retornaFalse() = runTest {
        // DADO -> falha
        val cpf = "123.456.789-10"
        whenever(mockRepository.deletarAluno(cpf)).thenReturn(false)

        // QUANDO
        viewModel.deletarAluno(cpf)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.deletar.getOrAwaitValue()
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun buscarAluno_sucesso_retonaAluno() = runTest {
        // DADO -> sucesso
        val cpf = "038.295.820-99"
        val alunoEsperado = AlunoResponse(cpf, "Joao", "Jiu jitsu", "01/04/2025", false)
        whenever(mockRepository.buscarAluno(cpf)).thenReturn(alunoEsperado)

        // QUANDO
        viewModel.buscarAluno(cpf)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.buscarAluno.getOrAwaitValue()
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(alunoEsperado)
    }

    @Test
    fun buscarAluno_falha_retonaNull() = runTest {
        // DADO -> falha
        val cpf = "123.456.789-10"
        whenever(mockRepository.buscarAluno(cpf)).thenThrow(RuntimeException("Erro ao buscar aluno"))

        // QUANDO
        viewModel.buscarAluno(cpf)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.buscarAluno.getOrAwaitValue()
        assertThat(resultado).isNull()
    }

    @Test
    fun atualizarAluno_sucesso_retornaAluno() = runTest {
        // DADO -> sucesso
        val cpf = "038.295.820-99"
        val dadosAtualizados = AlunoUpdate("Joao Vitor", "Musculação")
        val alunoEsperado = AlunoResponse(
            cpf, "Joao Vitor", "Musculação",
            "01/04/2025", false
        )
        whenever(mockRepository.atualizarAluno(cpf, dadosAtualizados)).thenReturn(alunoEsperado)

        // QUANDO
        viewModel.atualizarAluno(cpf, dadosAtualizados)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.atualizarAluno.getOrAwaitValue()
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(alunoEsperado)
    }

    @Test
    fun atualizarAluno_falha_retornaNull() = runTest {
        // DADO -> falha
        val cpf = "123.456.789-10"
        val dadosAtualizados = AlunoUpdate("Joao Vitor", "Musculação")
        whenever(
            mockRepository.atualizarAluno(
                cpf,
                dadosAtualizados
            )
        ).thenThrow(RuntimeException("Erro na atualização"))

        // QUANDO
        viewModel.atualizarAluno(cpf, dadosAtualizados)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.atualizarAluno.getOrAwaitValue()
        assertThat(resultado).isNull()
    }

}