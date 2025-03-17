package engsoft.matfit.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import engsoft.matfit.service.repository.FuncionarioRepository
import engsoft.matfit.util.RegraTestCustomizada
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

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
    fun listarFuncionarios() = runTest {
    }

    @Test
    fun cadastrarFuncionario() = runTest{
    }

    @Test
    fun buscarFuncionario() = runTest {
    }

    @Test
    fun atualizarFuncionario() = runTest {
    }

    @Test
    fun deletarFuncionario() = runTest {
    }
}