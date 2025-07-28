package engsoft.matfit.util

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

open class BaseValidacao(val context: Context) {

    // Toast para reutilização
    fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Snach de erros para reutilização
    fun snackForError(view: View, msg: String){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
            .setBackgroundTint(Color.RED)
            .show()
    }

    // validação da Carga Horária
    fun validarCargaHoraria(carga: Int) = carga > 0

    // validação do Nome
    fun validarNome(nome: String) =
        (nome.isNotBlank() && nome.isNotEmpty() && nome.length >= 4)

    // validação do esporte
    fun validarEsporte(esporte: String) =
        (esporte.length >= 3 && esporte.isNotBlank() && esporte.isNotEmpty())

    // validação do cpf
    fun validarCpf(cpfAntigo: String): Boolean {
        var cpf = cpfAntigo
        cpf = cpf.replace("[^0-9]".toRegex(), "")

        if (cpf.length != 11) {
            return false
        }

        var digitosIguais = true
        for (i in 1..10) {
            if (cpf[i] != cpf[0]) {
                digitosIguais = false
                break
            }
        }

        if (digitosIguais) {
            return false
        }

        var soma = 0
        for (i in 0..8) {
            soma += (10 - i) * (cpf[i].code - '0'.code)
        }
        var digito1 = 11 - soma % 11
        if (digito1 > 9) {
            digito1 = 0
        }
        soma = 0
        for (i in 0..9) {
            soma += (11 - i) * (cpf[i].code - '0'.code)
        }
        var digito2 = 11 - soma % 11
        if (digito2 > 9) {
            digito2 = 0
        }
        return cpf[9].code - '0'.code == digito1 && cpf[10].code - '0'.code == digito2
    }
}