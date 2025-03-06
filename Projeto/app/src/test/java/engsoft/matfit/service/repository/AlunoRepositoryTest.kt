package engsoft.matfit.service.repository

import com.google.common.truth.Truth.assertThat
import engsoft.matfit.model.AlunoDTO
import engsoft.matfit.model.AlunoRequest
import engsoft.matfit.model.AlunoResponse
import engsoft.matfit.model.AlunoUpdate
import engsoft.matfit.model.EquipamentoDTO
import engsoft.matfit.service.AlunoService
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
class AlunoRepositoryTest {

    @Mock
    private lateinit var mockRemote: AlunoService

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
            AlunoDTO("129.078.459-12", "Joaquim Abreu", "Musculação"),
            AlunoDTO("038.295.820-99", "Cosmos Dantas", "Crossfit")
        )
        // moca os dados e retorna o comportamento esperado
        Mockito.doReturn(Response.success(listaAlunos)).`when`(mockRemote).listarAlunos()

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
        val listaVazia = emptyList<AlunoDTO>()
        Mockito.doReturn(Response.success(listaVazia)).`when`(mockRemote).listarAlunos()

        // QUANDO
        val resultado = alunoRepository.listarAlunos()

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEmpty()
    }

    @Test
    fun listarAlunos_falhaNaAPI_retornaListaVazia() = runTest {
        // DADO -> falha na API
        Mockito.doReturn(
            Response.error<List<AlunoDTO>>(
                404, responseBodyError
            )
        ).`when`(mockRemote).listarAlunos()

        // QUANDO
        val resultado = alunoRepository.listarAlunos()

        // ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isEmpty()
    }

    @Test
    fun cadastrarAluno_sucesso_retornaTrue() = runTest {
        // DADO -> sucesso
        val alunoEsperado =
            AlunoResponse("038.295.820-99", "Cosmos Dantas", "Crossfit", "06/03/2025", false)
        val aluno = AlunoRequest("038.295.820-99", "Cosmos Dantas", "Crossfit")
        Mockito.doReturn(Response.success(alunoEsperado)).`when`(mockRemote).cadastrarAluno(aluno)

        // QUANDO
        val resultado = alunoRepository.cadastrarAluno(aluno)

        //ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isTrue()
    }

    @Test
    fun cadastrarAluno_falha_retornaFalse() = runTest {
        // DADO -> falha -> cpf invalido
        val aluno = AlunoRequest("123.456.789-10", "Joaquim Abreu", "Musculação")
        Mockito.doReturn(Response.error<Boolean>(400, responseBodyError)).`when`(mockRemote).cadastrarAluno(aluno)

        // QUANDO
        val resultado = alunoRepository.cadastrarAluno(aluno)

        //ENTÃO
        assertThat(resultado).isNotNull()
        assertThat(resultado).isFalse()
    }

    @Test
    fun cadastrarAluno_falhaNaAPI_retornaFalse() = runTest {
        // DADO -> falha na API
        val aluno = AlunoRequest("038.295.820-99", "Joaquim Abreu", "Musculação")
        Mockito.doReturn(Response.error<Boolean>(404, responseBodyError)).`when`(mockRemote)
            .cadastrarAluno(aluno)

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
        Mockito.doReturn(Response.success(true)).`when`(mockRemote).deletarAluno(cpf)

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
        Mockito.doReturn(Response.error<Boolean>(400, responseBodyError)).`when`(mockRemote).deletarAluno(cpf)

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
        Mockito.doReturn(Response.error<Boolean>(404, responseBodyError)).`when`(mockRemote)
            .deletarAluno(cpf)

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
        val alunoEsperado = AlunoResponse(
            "918.243.680-03", "Felipe Santos", "musculação", "04/04/2025", false
        )
        Mockito.doReturn(Response.success(alunoEsperado)).`when`(mockRemote).buscarAluno(cpf)

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
        Mockito.doReturn(Response.error<AlunoResponse>(400, responseBodyError))
            .`when`(mockRemote).buscarAluno(cpf)

        // QUANDO
        val resultado = alunoRepository.buscarAluno(cpf)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun buscarAluno_falhaNaAPI_retornaNull() = runTest {
        // DADO -> falha na API
        val cpf = "133.362.312-63"
        Mockito.doReturn(Response.error<AlunoResponse>(404, responseBodyError)).`when`(mockRemote)
            .buscarAluno(cpf)

        // QUANDO
        val resultado = alunoRepository.buscarAluno(cpf)

        // ENTÃO
        assertThat(resultado).isNull()
    }

    @Test
    fun atualizarAluno_sucesso_retornaAlunoAtualizado() = runTest {
        // DADO -> aluno atualizado
        val cpf = "105.938.774-38"
        val dadosAtualizados = AlunoUpdate("João Vitor", "Musculação")
        val alunoEsperado = AlunoResponse(cpf, "João Vitor", "Musculação", "24/03/2025", false)

        Mockito.doReturn(Response.success(alunoEsperado)).`when`(mockRemote)
            .atualizarAluno(cpf, dadosAtualizados)

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
        val dadosAtualizados = AlunoUpdate("João Vitor", "Musculação")

        Mockito.doReturn(
            Response.error<AlunoResponse>(404, responseBodyError)
        ).`when`(mockRemote).atualizarAluno(cpf, dadosAtualizados)

        // QUANDO
        val resultado = alunoRepository.atualizarAluno(cpf, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNull()
    }


    @Test
    fun realizarPagamento_sucesso_retornaTrue() = runTest {
        // Dado -> sucesso
        val cpf = "918.243.680-03"
        Mockito.doReturn(Response.success(true)).`when`(mockRemote).realizarPagamento(cpf)

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
        Mockito.doReturn(Response.error<Boolean>(404, ResponseBody.create(null, "falha na API")))
            .`when`(mockRemote).realizarPagamento(cpf)

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
        val alunoEsperado = AlunoResponse(cpf, "Felipe Santos", "musculação",
            "01/04/2025", false)
        Mockito.doReturn(Response.success(alunoEsperado)).`when`(mockRemote).verificarPagamento(cpf)

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
        Mockito.doReturn(
            Response.error<AlunoResponse>(404, responseBodyError)
        ).`when`(mockRemote).verificarPagamento(cpf)

        // QUANDO
        val resultado = alunoRepository.verificarPagamento(cpf)

        // ENTÃO
        assertThat(resultado).isNull()
    }
}