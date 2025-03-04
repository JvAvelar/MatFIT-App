package engsoft.matfit.service.repository

import com.google.common.truth.Truth.assertThat
import engsoft.matfit.model.AlunoDTO
import engsoft.matfit.model.AlunoRequest
import engsoft.matfit.model.AlunoResponse
import engsoft.matfit.service.AlunoService
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class AlunoDTORepositoryTest {

    @Mock
    private lateinit var mockAlunoRepository: AlunoRepository

    @Mock
    private lateinit var mockRemote: AlunoService

    private lateinit var alunoRepository: AlunoRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        // Instancia real do repositorio
        alunoRepository = Mockito.spy(AlunoRepository())

        // Injetando o mock do remote via Reflection
        val remoteField = AlunoRepository::class.java.getDeclaredField("remote")
        remoteField.isAccessible = true
        remoteField.set(alunoRepository, mockRemote)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun listarAlunos_retornaNaoVazia() = runTest {
        // DADO
        val mockListaAlunos = listOf(
            AlunoDTO("129.078.459-12", "Joaquim Abreu", "Musculação"),
            AlunoDTO("038.295.820-99", "Cosmos Dantas", "Crossfit")
        )
        // moca os dados e retorna o comportamento esperado
        Mockito.`when`(mockAlunoRepository.listarAlunos()).thenReturn(mockListaAlunos)

        // QUANDO
        val lista = mockAlunoRepository.listarAlunos()

        // ENTÂO
        assertThat(lista).isNotEmpty()
        assertThat(lista).hasSize(2)
    }

    @Test
    fun listarAlunos_retornaVazia() = runTest {
        // DADO
        val mockListaVazia = emptyList<AlunoDTO>()
        Mockito.`when`(mockAlunoRepository.listarAlunos()).thenReturn(mockListaVazia)

        // QUANDO
        val lista = mockAlunoRepository.listarAlunos()

        // ENTÃO
        assertThat(lista).isEmpty()
        assertThat(lista).hasSize(0)
    }

    @Test
    fun cadastrarAlunoCpfFalso_retornaFalse() = runTest { // CPF INVÁLIDO - inventado
        // DADO
        val alunoCpfFalso = AlunoRequest("129.078.459-12", "Joaquim Abreu", "Musculação")
        Mockito.`when`(mockAlunoRepository.cadastrarAluno(alunoCpfFalso)).thenReturn(false)

        // QUANDO
        val salvarAluno = mockAlunoRepository.cadastrarAluno(alunoCpfFalso)

        //ENTÃO
        assertThat(salvarAluno).isFalse()
    }

    @Test
    fun cadastrarAlunoCpfVeridico_retornaTrue() = runTest { // CPF VÁLIDO via gerador de cpf
        // DADO
        val alunoCpfVeridico = AlunoRequest("038.295.820-99", "Cosmos Dantas", "Crossfit")
        Mockito.`when`(mockAlunoRepository.cadastrarAluno(alunoCpfVeridico)).thenReturn(true)

        // QUANDO
        val salvar = mockAlunoRepository.cadastrarAluno(alunoCpfVeridico)

        //ENTÃO
        assertThat(salvar).isTrue()
    }

    @Test
    fun deletarAlunoExistente_retornaFalse() = runTest {
        // DADO
        val cpfValido = "426.411.790-91"
        Mockito.`when`(mockAlunoRepository.deletarAluno(cpfValido)).thenReturn(true)

        // QUANDO
        val deletar = mockAlunoRepository.deletarAluno(cpfValido)

        // ENTÃO
        assertThat(deletar).isTrue()
    }

    @Test
    fun deletarAlunoInexistente_retornaTrue() = runTest {
        // DADO
        val cpfInvalido = "129.078.459-12"
        Mockito.`when`(mockAlunoRepository.deletarAluno(cpfInvalido)).thenReturn(false)

        // QUANDO
        val deletar = mockAlunoRepository.deletarAluno(cpfInvalido)

        // ENTÃO
        assertThat(deletar).isFalse()
    }

    @Test
    fun buscarAlunoValido_RetornaAluno() = runTest {
        // DADO - cpf válido
        val cpfEntrada = "133.368.314-66"
        val alunoEsperado = AlunoResponse(
            "133.368.314-66", "Valdisnéia Silva", "Funcional",
            "04/04/2025", false
        )
        Mockito.`when`(mockRemote.buscarAluno(cpfEntrada)).thenReturn( Response.success(alunoEsperado) )

        // QUANDO
        val busca = alunoRepository.buscarAluno(cpfEntrada)

        // ENTÃO
        assertThat(busca).isEqualTo(alunoEsperado)
        assertThat(busca!!.cpf).isEqualTo(alunoEsperado.cpf)
    }

    @Test
    fun buscarAlunoInvalido_RetornaNull() = runTest {
        // DADO - cpf inválido
        val cpf = "133.368.314-63"
        val alunoEsperado = AlunoResponse(
            "133.368.314-66", "Valdisnéia Silva", "Funcional",
            "04/04/2025", false
        )
        Mockito.`when`(mockRemote.buscarAluno(cpf)).thenReturn( Response.success(alunoEsperado) )

        // QUANDO
        val busca = alunoRepository.buscarAluno(cpf)

        // ENTÃO
       assertThat(busca).isNull()
    }

    @Test
    fun atualizarAluno() = runTest {

    }

    @Test
    fun realizarPagamento() = runTest {


    }

    @Test
    fun verificarPagamento() = runTest {

    }
}