package engsoft.matfit.util

// Classe implementada para gerenciar os ProgressBar e para reutilização nas telas de
// Alunos, Funcionários e Equipamentos
sealed class RequestState<T> {
    class Carregando<T> : RequestState<T>()
    class Sucesso<T>(val data: T) : RequestState<T>()
    class Erro<T>(val mensagem: String) : RequestState<T>()
}