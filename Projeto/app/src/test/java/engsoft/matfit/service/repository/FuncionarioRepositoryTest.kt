package engsoft.matfit.service.repository

import com.google.common.truth.Truth.assertThat
import engsoft.matfit.model.FuncionarioDTO
import engsoft.matfit.model.FuncionarioUpdate
import engsoft.matfit.service.FuncionarioService
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class FuncionarioRepositoryTest {

    @Mock
    private lateinit var mockRemote: FuncionarioService

    private lateinit var funcionarioRepository: FuncionarioRepository
    private lateinit var responseBodyError: ResponseBody

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        // criando uma instância real do FuncionarioRepository
        funcionarioRepository = FuncionarioRepository(mockRemote)
        responseBodyError = "Falha na API".toResponseBody("application/json".toMediaTypeOrNull())
    }

    @Test
    fun listarFuncionarios_sucesso_retornaListaFuncionario() = runTest {
        // DADO -> sucesso = lista de funcionários
        val listaFuncionarios = listOf(
            FuncionarioDTO("181.348.230-68", "Gabriel Silva", "Personal", 20),
            FuncionarioDTO("657.399.860-01", "Joao Souza", "Faxineiro", 20)
        )

        Mockito.doReturn(Response.success(listaFuncionarios)).`when`(mockRemote)
            .listarFuncionarios()

        // QUANDO
        val resultado = funcionarioRepository.listarFuncionarios()

        // ENTÃO
        assertThat(resultado).isNotEmpty()
        assertThat(resultado).hasSize(2)
    }

    @Test
    fun listarFuncionarios_listaVazia_retornaListaVazia() = runTest {
        // DADO -> falha
        val listaFuncionarios = emptyList<FuncionarioDTO>()
        Mockito.doReturn(Response.success(listaFuncionarios)).`when`(mockRemote)
            .listarFuncionarios()

        // QUANDO
        val resultado = funcionarioRepository.listarFuncionarios()

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEmpty()
    }

    @Test
    fun listarFuncionarios_falhaNaAPI_retornaListaVazia() = runTest {
        // DADO -> falha na API
        Mockito.doReturn(Response.error<List<FuncionarioDTO>>(404, responseBodyError))
            .`when`(mockRemote).listarFuncionarios()

        // QUANDO
        val resultado = funcionarioRepository.listarFuncionarios()

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEmpty()
    }

    @Test
    fun cadastrarFuncionario_sucesso_retornaTrue() = runTest {
        // DADO -> sucesso
        val funcionario = FuncionarioDTO("181.348.230-68", "Gabriel Silva", "Personal", 20)
        Mockito.doReturn(Response.success(funcionario)).`when`(mockRemote)
            .cadastrarFuncionario(funcionario)

        // QUANDO
        val resultado = funcionarioRepository.cadastrarFuncionario(funcionario)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isTrue()
    }

    @Test
    fun cadastrarFuncionario_falha_retornaFalse() = runTest {
        // DADO -> falha
        val funcionario = FuncionarioDTO("181.342.220-68", "Gabriel Silva", "Personal", 20)
        Mockito.doReturn(Response.error<FuncionarioDTO>(400, responseBodyError)).`when`(mockRemote)
            .cadastrarFuncionario(funcionario)

        // QUANDO
        val resultado = funcionarioRepository.cadastrarFuncionario(funcionario)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun cadastrarFuncionario_falhaNaAPI_retornaFalse() = runTest {
        // DADO -> falha na API
        val funcionario = FuncionarioDTO("181.342.220-68", "Gabriel Silva", "Personal", 20)
        Mockito.doReturn(Response.error<FuncionarioDTO>(404, responseBodyError)).`when`(mockRemote)
            .cadastrarFuncionario(funcionario)

        // QUANDO
        val resultado = funcionarioRepository.cadastrarFuncionario(funcionario)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun buscarFuncionario_sucesso_retornaFuncionario() = runTest {
        // DADO -> sucesso
        val cpf = "181.342.220-68"
        val funcionarioEsperado = FuncionarioDTO("181.342.220-68", "Gabriel Silva", "Personal", 20)
        Mockito.doReturn(Response.success(funcionarioEsperado)).`when`(mockRemote)
            .buscarFuncionario(cpf)

        // QUANDO
        val resultado = funcionarioRepository.buscarFuncionario(cpf)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(funcionarioEsperado)
    }

    @Test
    fun buscarFuncionario_falha_retornaNull() = runTest {
        // DADO -> falha
        val cpf = "123.456.789-10"
        Mockito.doReturn(Response.error<FuncionarioDTO>(400, responseBodyError))
            .`when`(mockRemote).buscarFuncionario(cpf)

        // QUANDO
        val resultado = funcionarioRepository.buscarFuncionario(cpf)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun buscarFuncionario_falhaNaAPI_retornaNull() = runTest {
        // DADO -> falha na API
        val cpf = "181.342.220-68"
        Mockito.doReturn(Response.error<FuncionarioDTO>(404, responseBodyError))
            .`when`(mockRemote).buscarFuncionario(cpf)

        // QUANDO
        val resultado = funcionarioRepository.buscarFuncionario(cpf)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun atualizarFuncionario_sucesso_retornaFuncionario() = runTest {
        // DADO -> sucesso
        val cpf = "181.342.220-68"
        val dadosAtualizados = FuncionarioUpdate("Gabriel Silva", "Personal", 24)
        val funcionarioEsperado = FuncionarioDTO(cpf, "Gabriel Silva", "Personal", 24)
        Mockito.doReturn(Response.success(funcionarioEsperado)).`when`(mockRemote)
            .atualizarFuncionario(cpf, dadosAtualizados)

        // QUANDO
        val resultado = funcionarioRepository.atualizarFuncionario(cpf, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(funcionarioEsperado)
    }

    @Test
    fun atualizarFuncionario_falha_retornaNull() = runTest {
        // DADO -> falha
        val cpf = "181.342.223-64"
        val dadosAtualizados = FuncionarioUpdate("Gabriel Silva", "Personal", 24)

        Mockito.doReturn(Response.error<FuncionarioDTO>(400, responseBodyError))
            .`when`(mockRemote).atualizarFuncionario(cpf, dadosAtualizados)

        // QUANDO
        val resultado = funcionarioRepository.atualizarFuncionario(cpf, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun atualizarFuncionario_falhaNaAPI_retornaNull() = runTest {
        // DADO -> falha na API
        val cpf = "181.342.220-68"
        val dadosAtualizados = FuncionarioUpdate("Gabriel Silva", "Personal", 24)

        Mockito.doReturn(Response.error<FuncionarioDTO>(404, responseBodyError))
            .`when`(mockRemote).atualizarFuncionario(cpf, dadosAtualizados)

        // QUANDO
        val resultado = funcionarioRepository.atualizarFuncionario(cpf, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun deletarFuncionario_sucesso_retornatrue() = runTest {
        // DADO -> sucesso
        val cpf =  "181.342.220-68"
        Mockito.doReturn(Response.success(true)).`when`(mockRemote).deletarFuncionario(cpf)

        // QUANDO
        val resultado = funcionarioRepository.deletarFuncionario(cpf)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isTrue()
    }

    @Test
    fun deletarFuncionario_falha_retornaFalse() = runTest {
        // DADO -> falha
        val cpf =  "181.342.221-62"
        Mockito.doReturn(Response.error<Boolean>(400, responseBodyError)).`when`(mockRemote).deletarFuncionario(cpf)

        // QUANDO
        val resultado = funcionarioRepository.deletarFuncionario(cpf)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun deletarFuncionario_falhaNaAPI_retornaFalse() = runTest {
        // DADO -> falha na API
        val cpf =  "181.342.220-68"
        Mockito.doReturn(Response.error<Boolean>(404, responseBodyError)).`when`(mockRemote).deletarFuncionario(cpf)

        // QUANDO
        val resultado = funcionarioRepository.deletarFuncionario(cpf)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }
}