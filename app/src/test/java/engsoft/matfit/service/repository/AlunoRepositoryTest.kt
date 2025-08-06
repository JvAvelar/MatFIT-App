package engsoft.matfit.service.repository

import com.google.common.truth.Truth.assertThat
import engsoft.matfit.model.Student
import engsoft.matfit.model.StudentRequestDTO
import engsoft.matfit.model.StudentResponseDTO
import engsoft.matfit.model.StudentUpdateDTO
import engsoft.matfit.service.StudentService
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
class AlunoRepositoryTest {

    @Mock
    private lateinit var mockRemote: StudentService

    private lateinit var alunoRepository: AlunoRepository
    private lateinit var responseBodyError: ResponseBody

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        // criando instância real do AlunoRepository
        alunoRepository = AlunoRepository(mockRemote)
        responseBodyError = "Falha na API".toResponseBody("application/json".toMediaTypeOrNull())
    }

    @Test
    fun listarAlunos_sucesso_retornaListaAlunos() = runTest {
        // DADO -> sucesso
        val listaAlunos = listOf(
            Student("129.078.459-12", "Joaquim Abreu", "Musculação"),
            Student("038.295.820-99", "Cosmos Dantas", "Crossfit")
        )
        // moca os dados e retorna o comportamento esperado
        whenever(mockRemote.listarAlunos())
            .thenReturn(Response.success(listaAlunos))

        // QUANDO
        val resultado = alunoRepository.listarAlunos()

        // ENTÂO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isNotEmpty()
        assertThat(resultado).hasSize(2)
    }

    @Test
    fun listarAlunos_listaVazia_retornaListaVazia() = runTest {
        // DADO -> falha = lista vazia
        whenever(mockRemote.listarAlunos())
            .thenReturn(Response.error(400, responseBodyError))

        // QUANDO
        val resultado = alunoRepository.listarAlunos()

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEmpty()
    }

    @Test
    fun listarAlunos_falhaNaAPI_retornaListaVazia() = runTest {
        // DADO -> falha na API
        whenever(mockRemote.listarAlunos())
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = alunoRepository.listarAlunos()

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEmpty()
    }

    @Test
    fun cadastrarAluno_sucesso_retornaTrue() = runTest {
        // DADO -> sucesso
        val alunoEsperado = StudentResponseDTO(
            "038.295.820-99", "Cosmos Dantas",
            "Crossfit", "06/03/2025", false
        )
        val aluno = StudentRequestDTO("038.295.820-99", "Cosmos Dantas", "Crossfit")
        whenever(mockRemote.cadastrarAluno(aluno))
            .thenReturn(Response.success(alunoEsperado))

        // QUANDO
        val resultado = alunoRepository.cadastrarAluno(aluno)

        //ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isTrue()
    }

    @Test
    fun cadastrarAluno_falha_retornaFalse() = runTest {
        // DADO -> falha -> cpf invalido
        val aluno = StudentRequestDTO("123.456.789-10", "Joaquim Abreu", "Musculação")
        whenever(mockRemote.cadastrarAluno(aluno))
            .thenReturn(Response.error(400, responseBodyError))

        // QUANDO
        val resultado = alunoRepository.cadastrarAluno(aluno)

        //ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun cadastrarAluno_falhaNaAPI_retornaFalse() = runTest {
        // DADO -> falha na API
        val aluno = StudentRequestDTO("038.295.820-99", "Joaquim Abreu", "Musculação")
        whenever(mockRemote.cadastrarAluno(aluno))
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = alunoRepository.cadastrarAluno(aluno)

        //ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun deletarAluno_sucesso_retornaTrue() = runTest {
        // DADO -> sucesso
        val cpf = "426.411.790-91"
        whenever(mockRemote.deletarAluno(cpf))
            .thenReturn(Response.success(true))

        // QUANDO
        val resultado = alunoRepository.deletarAluno(cpf)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isTrue()
    }

    @Test
    fun deletarAluno_falha_retornaFalse() = runTest {
        // DADO -> falha
        val cpf = "129.078.459-12"
        whenever(mockRemote.deletarAluno(cpf))
            .thenReturn(Response.error(400, responseBodyError))

        // QUANDO
        val resultado = alunoRepository.deletarAluno(cpf)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun deletarAluno_falhaNaAPI_retornaFalse() = runTest {
        // DADO -> falha na API
        val cpf = "129.078.459-12"
        whenever(mockRemote.deletarAluno(cpf))
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = alunoRepository.deletarAluno(cpf)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun buscarAluno_sucesso_retornaAluno() = runTest {
        // DADO - sucesso = cpf válido
        val cpf = "918.243.680-03"
        val alunoEsperado = StudentResponseDTO(
            "918.243.680-03", "Felipe Santos", "musculação", "04/04/2025", false
        )
        whenever(mockRemote.buscarAluno(cpf))
            .thenReturn(Response.success(alunoEsperado))

        // QUANDO
        val resultado = alunoRepository.buscarAluno(cpf)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado?.cpf).isEqualTo(alunoEsperado.cpf)
        assertThat(resultado).isEqualTo(alunoEsperado)
    }

    @Test
    fun buscarAluno_falha_retornaNull() = runTest {
        // DADO - falha = cpf inválido
        val cpf = "133.368.314-63" // -> cpf inválido
        whenever(mockRemote.buscarAluno(cpf))
            .thenReturn(Response.error(400, responseBodyError))

        // QUANDO
        val resultado = alunoRepository.buscarAluno(cpf)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun buscarAluno_falhaNaAPI_retornaNull() = runTest {
        // DADO -> falha na API
        val cpf = "133.362.312-63"
        whenever(mockRemote.buscarAluno(cpf))
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = alunoRepository.buscarAluno(cpf)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun atualizarAluno_sucesso_retornaAlunoAtualizado() = runTest {
        // DADO -> aluno atualizado
        val cpf = "105.938.774-38"
        val dadosAtualizados = StudentUpdateDTO("João Vitor", "Musculação")
        val alunoEsperado = StudentResponseDTO(cpf, "João Vitor", "Musculação", "24/03/2025", false)
        whenever(mockRemote.atualizarAluno(cpf, dadosAtualizados))
            .thenReturn(Response.success(alunoEsperado))

        // QUANDO
        val resultado = alunoRepository.atualizarAluno(cpf, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEqualTo(alunoEsperado)
        assertThat(resultado?.cpf).isEqualTo(alunoEsperado.cpf)
    }

    @Test
    fun atualizarAluno_falhaNaAPI_retornaNull() = runTest {
        // DADO -> falha na API
        val cpf = "105.938.774-38"
        val dadosAtualizados = StudentUpdateDTO("João Vitor", "Musculação")

        whenever(mockRemote.atualizarAluno(cpf, dadosAtualizados))
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = alunoRepository.atualizarAluno(cpf, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNull()
    }


    @Test
    fun realizarPagamento_sucesso_retornaTrue() = runTest {
        // Dado -> sucesso
        val cpf = "918.243.680-03"
        whenever(mockRemote.realizarPagamento(cpf))
            .thenReturn(Response.success(true))

        // QUANDO
        val resultado = alunoRepository.realizarPagamento(cpf)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isTrue()
    }

    @Test
    fun realizarPagamento_falhaNaAPI_retornaFalse() = runTest {
        // Dado -> sucesso
        val cpf = "918.243.680-03"
        whenever(mockRemote.realizarPagamento(cpf))
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = alunoRepository.realizarPagamento(cpf)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun verificarPagamento_sucesso_retornaAluno() = runTest {
        // DADO
        val cpf = "918.243.680-03"
        val alunoEsperado = StudentResponseDTO(
            cpf, "Felipe Santos", "musculação",
            "01/04/2025", false
        )
        whenever(mockRemote.verificarPagamento(cpf))
            .thenReturn(Response.success(alunoEsperado))

        // QUANDO
        val resultado = alunoRepository.verificarPagamento(cpf)

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado!!.pagamentoAtrasado).isEqualTo(alunoEsperado.pagamentoAtrasado)
        assertThat(resultado).isEqualTo(alunoEsperado)
    }

    @Test
    fun verificarPagamento_falhaNaAPI_retornaNull() = runTest {
        // DADO
        val cpf = "918.243.680-03"
        whenever(mockRemote.verificarPagamento(cpf))
            .thenReturn(Response.error(404, responseBodyError))

        // QUANDO
        val resultado = alunoRepository.verificarPagamento(cpf)

        // ENTÃO
        assertThat(resultado).isNull()
    }
}