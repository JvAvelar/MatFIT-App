package engsoft.matfit.util

// Classe implementada para gerenciar os ProgressBar e para reutilização nas telas de
// Alunos, Funcionários e Equipamentos
sealed class EstadoRequisicao<T> {
    class Carregando<T> : EstadoRequisicao<T>()
    class Sucesso<T>(val data: T) : EstadoRequisicao<T>()
    class Erro<T>(val mensagem: String) : EstadoRequisicao<T>()
}