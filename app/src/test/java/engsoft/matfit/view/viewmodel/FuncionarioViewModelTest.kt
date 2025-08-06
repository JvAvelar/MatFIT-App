package engsoft.matfit.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import engsoft.matfit.model.Employee
import engsoft.matfit.model.EmployeeUpdateDTO
import engsoft.matfit.service.repository.FuncionarioRepository
import engsoft.matfit.util.RequestState
import engsoft.matfit.util.RegraTestCustomizada
import engsoft.matfit.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class FuncionarioViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val regra = RegraTestCustomizada()

    @Mock
    private lateinit var mockRepository: FuncionarioRepository

    private lateinit var viewModel: FuncionarioViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = FuncionarioViewModel()
        // usando reflection
        val repositoryField = FuncionarioViewModel::class.java.getDeclaredField("repository")
        repositoryField.isAccessible = true
        repositoryField.set(viewModel, mockRepository)
    }


    @Test
    fun listarFuncionarios_sucesso_retornaListaFuncionario() = runTest {
        // DADO -> sucesso
        val listaEsperada = listOf(
            Employee("592.829.630-47", "Zyon Avelar", "Eletricista", 20),
            Employee("120.454.000-42", "Luana Duarte", "Personal", 20),
            Employee("837.709.180-10", "Luiz alves", "Segurança", 20),
        )
        whenever(mockRepository.listarFuncionarios())
            .thenReturn(listaEsperada)

        // QUANDO
        viewModel.listarFuncionarios()
        advanceUntilIdle()

        // ENTÃO
        val estado = viewModel.estadoRequisicao.getOrAwaitValue()
        assertThat(estado).isNotNull()
        assertThat(estado).isInstanceOf(RequestState.Sucesso::class.java)
        assertThat((estado as RequestState.Sucesso).data).isEqualTo(listaEsperada)
    }

    @Test
    fun listarFuncionario_falha_retornaListaVazia() = runTest {
        // DADO -> falha
        whenever(mockRepository.listarFuncionarios())
            .thenThrow(RuntimeException("Erro! Lista Vazia!!"))

        // QUANDO
        viewModel.listarFuncionarios()
        advanceUntilIdle()

        // ENTÃO
        val estado = viewModel.estadoRequisicao.getOrAwaitValue()
        assertThat(estado).isNotNull()
        assertThat(estado).isInstanceOf(RequestState.Erro::class.java)
        assertThat((estado as RequestState.Erro).mensagem).isEqualTo("Lista vazia!")
    }

    @Test
    fun cadastrarFuncionario_sucesso_retornaTrue() = runTest {
        // DADO -> sucesso
        val cpf = "592.829.630-47"
        val funcionario = Employee(cpf, "Joao Vitor", "Personal", 20)
        whenever(mockRepository.cadastrarFuncionario(funcionario))
            .thenReturn(true)

        // QUANDO
        viewModel.cadastrarFuncionario(funcionario)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.cadastroFuncionario.getOrAwaitValue()
        assertThat(resultado).isNotNull()
        assertThat(resultado).isTrue()
    }

    @Test
    fun cadastrarFuncionario_falha_retornaFalse() = runTest {
        // DADO -> falha
        val cpf = "123.456.789-10"
        val funcionario = Employee(cpf, "Joao Vitor", "Personal", 20)
        whenever(mockRepository.cadastrarFuncionario(funcionario))
            .thenThrow(RuntimeException("CPF inválido!"))

        // QUANDO
        viewModel.cadastrarFuncionario(funcionario)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.cadastroFuncionario.getOrAwaitValue()
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun buscarFuncionario_sucesso_retornaFuncionario() = runTest {
        // DADO -> sucesso
        val cpf = "592.829.630-47"
        val funcionarioEsperado = Employee(
            "592.829.630-47", "Zyon Avelar",
            "Eletricista", 20
        )
        whenever(mockRepository.buscarFuncionario(cpf))
            .thenReturn(funcionarioEsperado)

        // QUANDO
        viewModel.buscarFuncionario(cpf)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.buscarFuncionario.getOrAwaitValue()
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(funcionarioEsperado)
    }

    @Test
    fun buscarFuncionario_falaha_retornaNull() = runTest {
        // DADO -> falha
        val cpf = "123.456.789-10"
        whenever(mockRepository.buscarFuncionario(cpf))
            .thenThrow(RuntimeException("CPF inválido!"))

        // QUANDO
        viewModel.buscarFuncionario(cpf)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.buscarFuncionario.getOrAwaitValue()
        assertThat(resultado).isNull()
    }

    @Test
    fun atualizarFuncionario_sucesso_retornaFuncionario() = runTest {
        // DADO -> sucesso
        val cpf = "592.829.630-47"
        val funcionarioEsperado =  Employee(
            "592.829.630-47", "Zyon Avelar",
            "Segurança", 20
        )
        val dadosAtualizados = EmployeeUpdateDTO("", "Segurança", 20)
        whenever(mockRepository.atualizarFuncionario(cpf, dadosAtualizados))
            .thenReturn(funcionarioEsperado)

        // QUANDO
        viewModel.atualizarFuncionario(cpf, dadosAtualizados)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.atualizarFuncionario.getOrAwaitValue()
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(funcionarioEsperado)
    }

    @Test
    fun atualizarFuncionario_falha_retornaNull() = runTest {
        // DADO -> falha
        val cpf = "592.829.630-47"
        val dadosAtualizados = EmployeeUpdateDTO("", "Segurança", 0)
        whenever(mockRepository.atualizarFuncionario(cpf, dadosAtualizados))
            .thenThrow(RuntimeException("Carga horária invalida!"))

        // QUANDO
        viewModel.atualizarFuncionario(cpf, dadosAtualizados)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.atualizarFuncionario.getOrAwaitValue()
        assertThat(resultado).isNull()
    }

    @Test
    fun deletarFuncionario_sucesso_retornaTrue() = runTest {
        // DADO -> sucesso
        val cpf = "592.829.630-47"
        whenever(mockRepository.deletarFuncionario(cpf))
            .thenReturn(true)

        // QUANDO
        viewModel.deletarFuncionario(cpf)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.deletarFuncionario.getOrAwaitValue()
        assertThat(resultado).isNotNull()
        assertThat(resultado).isTrue()
    }

    @Test
    fun deletarFuncionario_falha_retornaNull() = runTest {
        // DADO -> falha
        val cpf = "123.456.789-10"
        whenever(mockRepository.deletarFuncionario(cpf))
            .thenThrow(RuntimeException("CPF inválido!!"))

        // QUANDO
        viewModel.deletarFuncionario(cpf)
        advanceUntilIdle()

        // ENTÃO
        val resultado = viewModel.deletarFuncionario.getOrAwaitValue()
        assertThat(resultado).isNull()
    }
}