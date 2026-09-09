package com.campeonato.tenismesa.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.campeonato.tenismesa.data.supabase // Conecta com o arquivo SupabaseClient que você criou
import io.github.jan_tennert.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import supabase

class PlacarViewModel {

    // 1. VARIÁVEIS DO PLACAR: Guardam os pontos e sets na memória do celular
    var pontosA by mutableStateOf(0)
    var pontosB by mutableStateOf(0)
    var setsA by mutableStateOf(0)
    var setsB by mutableStateOf(0)
    var saqueJogadorA by mutableStateOf(true)

    // Ferramenta que permite o app rodar comandos pesados de internet sem travar a tela
    private val escopoInternet = CoroutineScope(Dispatchers.Default)

    // 2. FUNÇÃO DE SOMAR PONTO (Envia os dados para a Internet a cada clique!)
    fun somarPonto(jogador: String) {
        if (jogador == "A") {
            pontosA++
        } else {
            pontosB++
        }

        verificarTrocaDeSaque()
        verificarFimDoSet()

        // 🚀 Conecta direto com a tabela 'placar_ao_vivo' do Supabase que você configurou hoje!
        enviarPlacarParaOSupabase()
    }

    private fun verificarTrocaDeSaque() {
        val totalPontos = pontosA + pontosB
        if (pontosA >= 10 && pontosB >= 10) {
            saqueJogadorA = !saqueJogadorA
        } else {
            if (totalPontos % 2 == 0) {
                saqueJogadorA = !saqueJogadorA
            }
        }
    }

    private fun verificarFimDoSet() {
        if (pontosA >= 11 && (pontosA - pontosB) >= 2) {
            setsA++
            zerarPlacarDoSet()
        } else if (pontosB >= 11 && (pontosB - pontosA) >= 2) {
            setsB++
            zerarPlacarDoSet()
        }
    }

    private fun zerarPlacarDoSet() {
        pontosA = 0
        pontosB = 0
        saqueJogadorA = true
    }

    fun reiniciarPartidaCompleta() {
        pontosA = 0
        pontosB = 0
        setsA = 0
        setsB = 0
        saqueJogadorA = true
        enviarPlacarParaOSupabase()
    }

    // 🌐 FUNÇÃO QUE ENVIA OS PONTOS EM TEMPO REAL PARA O BANCO DE DADOS
    private fun enviarPlacarParaOSupabase() {
        escopoInternet.launch {
            try {
                val dadosDoPlacar = mapOf(
                    "pontos_atleta_a" to pontosA,
                    "pontos_atleta_b" to pontosB,
                    "sets_atleta_a" to setsA,
                    "sets_atleta_b" to setsB,
                    "quem_saca" to if (saqueJogadorA) "A" else "B"
                )

                // Envia para a tabela que ativamos o Realtime hoje!
                supabase.postgrest["placar_ao_vivo"].insert(dadosDoPlacar)
            } catch (e: Exception) {
                println("Erro ao mandar ponto para a nuvem: ${e.message}")
            }
        }
    }
}
