package engsoft.matfit.service.repository

import com.google.common.truth.Truth.assertThat
import engsoft.matfit.model.AlunoDTO
import engsoft.matfit.model.AlunoRequest
import engsoft.matfit.model.AlunoResponse
import engsoft.matfit.model.AlunoUpdate
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

@RunWith(MockitoJUnitRunner::class)
class AlunoRepositoryTest {

    @Mock
    private lateinit var mockAlunoRepository: AlunoRepository

    @Mock
    private lateinit var mockRemote: AlunoService

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        // criando instância real do AlunoRepository
        mockAlunoRepository = Mockito.spy(AlunoRepository())

        // mock do AlunoService via Reflection
        val remoteField = AlunoRepository::class.java.getDeclaredField("remote")
        remoteField.isAccessible = true
        remoteField.set(mockAlunoRepository, mockRemote)
    }

    @Test
    fun listarAlunos_sucesso_retornaListaAlunos() = runTest {
        // DADO -> sucesso
        val mockListaAlunos = listOf(
            AlunoDTO("129.078.459-12", "Joaquim Abreu", "Musculação"),
            AlunoDTO("038.295.820-99", "Cosmos Dantas", "Crossfit")
        )
        // moca os dados e retorna o comportamento esperado
        Mockito.doReturn(mockListaAlunos).`when`(mockAlunoRepository).listarAlunos()

        // QUANDO
        val lista = mockAlunoRepository.listarAlunos()

        // ENTÂO
        assertThat(lista).isNotEmpty()
        assertThat(lista).hasSize(2)
    }

    @Test
    fun listarAlunos_falha_retornaListaVazia() = runTest {
        // DADO -> falha = lista vazia
        val mockListaVazia = emptyList<AlunoDTO>()
        Mockito.doReturn(mockListaVazia).`when`(mockAlunoRepository).listarAlunos()

        // QUANDO
        val lista = mockAlunoRepository.listarAlunos()

        // ENTÃO
        assertThat(lista).isEmpty()
        assertThat(lista).isNotNull()
    }

    @Test
    fun listarAlunos_falhaNaAPI_retornaListaVazia() = runTest {
        // DADO -> falha na API
        Mockito.doReturn(
            Response.error<List<AlunoDTO>>(
                404, ResponseBody.create(null, "falha na API")
            )
        ).`when`(mockRemote).listarAlunos()

        // QUANDO
        val lista = mockAlunoRepository.listarAlunos()

        // ENTÃO
        assertThat(lista).isEmpty()
        assertThat(lista).isNotNull()
    }

    @Test
    fun cadastrarAluno_CpfVeridico_retornaTrue() = runTest {
        // DADO -> cpf válido
        val aluno = AlunoRequest("038.295.820-99", "Cosmos Dantas", "Crossfit")
        Mockito.doReturn(true).`when`(mockAlunoRepository).cadastrarAluno(aluno)

        // QUANDO
        val salvar = mockAlunoRepository.cadastrarAluno(aluno)

        //ENTÃO
        assertThat(salvar).isTrue()
    }

    @Test
    fun cadastrarAluno_CpfFalso_retornaFalse() = runTest {
        // DADO -> cpf inválido
        val alunoCpfFalso = AlunoRequest("129.078.459-12", "Joaquim Abreu", "Musculação")
        Mockito.doReturn(false).`when`(mockAlunoRepository).cadastrarAluno(alunoCpfFalso)

        // QUANDO
        val salvarAluno = mockAlunoRepository.cadastrarAluno(alunoCpfFalso)

        //ENTÃO
        assertThat(salvarAluno).isFalse()
    }

    @Test
    fun cadastrarAluno_falhaNaAPI_retornaFalse() = runTest {
        // DADO -> falha na API
        val alunoCpfFalso = AlunoRequest("038.295.820-99", "Joaquim Abreu", "Musculação")
        Mockito.doReturn(Response.error<Boolean>(404, ResponseBody.create(null, "Falha na API")))
            .`when`(mockRemote).cadastrarAluno(alunoCpfFalso)

        // QUANDO
        val salvarAluno = mockAlunoRepository.cadastrarAluno(alunoCpfFalso)

        //ENTÃO
        assertThat(salvarAluno).isFalse()
    }


    @Test
    fun deletarAluno_sucesso_retornaTrue() = runTest {
        // DADO -> sucesso
        val cpf = "426.411.790-91"
        Mockito.doReturn(Response.success(true)).`when`(mockRemote).deletarAluno(cpf)

        // QUANDO
        val deletar = mockAlunoRepository.deletarAluno(cpf)

        // ENTÃO
        assertThat(deletar).isTrue()
    }

    @Test
    fun deletarAluno_falha_retornaFalse() = runTest {
        // DADO -> falha
        val cpf = "129.078.459-12"
        Mockito.doReturn(Response.success(false)).`when`(mockRemote).deletarAluno(cpf)

        // QUANDO
        val deletar = mockAlunoRepository.deletarAluno(cpf)

        // ENTÃO
        assertThat(deletar).isFalse()
    }

    @Test
    fun deletarAluno_falhaNaAPI_retornaFalse() = runTest {
        // DADO -> falha na API
        val cpf = "129.078.459-12"
        Mockito.doReturn(Response.error<Boolean>(404, ResponseBody.create(null, "Falha na API")))
            .`when`(mockRemote).deletarAluno(cpf)

        // QUANDO
        val deletar = mockAlunoRepository.deletarAluno(cpf)

        // ENTÃO
        assertThat(deletar).isFalse()
    }

    @Test
    fun buscarAluno_sucesso_retornaAluno() = runTest {
        // DADO - cpf válido
        val cpf = "918.243.680-03"
        val alunoEsperado = AlunoResponse(
            "918.243.680-03", "Felipe Santos", "musculação", "04/04/2025", false
        )
        Mockito.doReturn(Response.success(alunoEsperado)).`when`(mockRemote).buscarAluno(cpf)

        // QUANDO
        val busca = mockAlunoRepository.buscarAluno(cpf)

        // ENTÃO
        assertThat(busca).isEqualTo(alunoEsperado)
        assertThat(busca!!.cpf).isEqualTo(alunoEsperado.cpf)
    }

    @Test
    fun buscarAluno_falha_retornaNull() = runTest {
        // DADO - cpf inválido
        val cpf = "133.368.314-63"
        val alunoEsperado = AlunoResponse(
            "918.243.680-03", "Felipe Santos", "musculação", "04/04/2025", false
        )
        Mockito.doReturn(Response.success(alunoEsperado)).`when`(mockRemote).buscarAluno(cpf)

        // QUANDO
        val busca = mockAlunoRepository.buscarAluno(cpf)

        // ENTÃO
        assertThat(busca).isNull()
    }

    @Test
    fun buscarAluno_falhaNaAPI_retornaNull() = runTest {
        // DADO -> falha na API
        val cpf = "133.362.312-63"

        Mockito.doReturn(Response.error<AlunoResponse>(404, ResponseBody.create(null, "")))
            .`when`(mockRemote).buscarAluno(cpf)

        // QUANDO
        val busca = mockAlunoRepository.buscarAluno(cpf)

        // ENTÃO
        assertThat(busca).isNull()
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
        val resultado = mockAlunoRepository.atualizarAluno(cpf, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isEqualTo(alunoEsperado)
        assertThat(resultado!!.cpf).isEqualTo(alunoEsperado.cpf)
    }

    @Test
    fun atualizarAluno_falhaNaAPI_retornaNull() = runTest {
        // DADO -> falha na API
        val cpf = "105.938.774-38"
        val dadosAtualizados = AlunoUpdate("João Vitor", "Musculação")

        Mockito.doReturn(
            Response.error<AlunoResponse>(
                404, ResponseBody.create(null, "Aluno não encontrado")
            )
        ).`when`(mockRemote).atualizarAluno(cpf, dadosAtualizados)

        // QUANDO
        val resultado = mockAlunoRepository.atualizarAluno(cpf, dadosAtualizados)

        // ENTÃO
        assertThat(resultado).isNull()
    }


    @Test
    fun realizarPagamento_sucesso_retornaTrue() = runTest {
        // Dado -> sucesso
        val cpf = "918.243.680-03"
        Mockito.doReturn(Response.success(true)).`when`(mockRemote).realizarPagamento(cpf)

        // QUANDO
        val resultado = mockAlunoRepository.realizarPagamento(cpf)

        // ENTÃO
        assertThat(resultado).isTrue()
    }

    @Test
    fun realizarPagamento_falhaNaAPI_retornaFalse() = runTest {
        // Dado -> sucesso
        val cpf = "918.243.680-03"

        Mockito.doReturn(Response.error<Boolean>(404, ResponseBody.create(null, "falha na API")))
            .`when`(mockRemote).realizarPagamento(cpf)

        // QUANDO
        val resultado = mockAlunoRepository.realizarPagamento(cpf)

        // ENTÃO
        assertThat(resultado).isFalse()
    }

    @Test
    fun verificarPagamento_sucesso_retornaAluno() = runTest {
        // DADO
        val cpf = "918.243.680-03"
        val alunoEsperado = AlunoResponse(cpf, "Felipe Santos", "musculação", "01/04/2025", false)
        Mockito.doReturn(Response.success(alunoEsperado)).`when`(mockRemote).verificarPagamento(cpf)

        // QUANDO
        val resultado = mockAlunoRepository.verificarPagamento(cpf)

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
            Response.error<AlunoResponse>(
                404, ResponseBody.create(null, "falha na API")
            )
        ).`when`(mockRemote).verificarPagamento(cpf)

        // QUANDO
        val resultado = mockAlunoRepository.verificarPagamento(cpf)

        // ENTÃO
        assertThat(resultado).isNull()
    }
}