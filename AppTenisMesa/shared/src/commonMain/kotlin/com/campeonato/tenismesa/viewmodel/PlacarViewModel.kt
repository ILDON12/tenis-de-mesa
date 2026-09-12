package com.campeonato.tenismesa.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.campeonato.tenismesa.data.supabase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.realtime.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import com.campeonato.tenismesa.util.SoundType
import com.campeonato.tenismesa.util.Localization
import com.campeonato.tenismesa.util.AppStrings

@Serializable
data class PlacarState(
    val pontosA: Int,
    val pontosB: Int,
    val setsA: Int,
    val setsB: Int,
    val saqueJogadorA: Boolean,
    val jogoFinalizado: Boolean,
    val nomeCampeao: String,
    val jogadorA: String,
    val jogadorB: String,
    val idiomaSelecionado: Int
)

class PlacarViewModel : ViewModel() {
    private var channel = supabase.realtime.channel("partida_realtime")

    private val _soundEvents = MutableSharedFlow<SoundType>()
    val soundEvents = _soundEvents.asSharedFlow()

    // ID único para sincronizar entre aparelhos
    var partidaId by mutableStateOf("mesa_1")

    // 1. SISTEMA DE IDIOMA
    var idiomaSelecionado by mutableStateOf(0)
    val strings: AppStrings get() = Localization.getStrings(idiomaSelecionado)

    // 2. CONTROLE DE ATLETAS
    var jogadorA by mutableStateOf("")
    var jogadorB by mutableStateOf("")
    val listaInscritos = mutableListOf<String>()
    val limiteMaximoAtletas = 15

    // 3. VARIÁVEIS DA PARTIDA
    var pontosA by mutableStateOf(0)
    var pontosB by mutableStateOf(0)
    var setsA by mutableStateOf(0)
    var setsB by mutableStateOf(0)
    var saqueJogadorA by mutableStateOf(true)
    var jogoFinalizado by mutableStateOf(false)
    var nomeCampeao by mutableStateOf("")

    private var isUpdatingFromRemote = false

    init {
        conectarAoRealtime()
    }

    fun conectarAoRealtime() {
        viewModelScope.launch {
            try {
                supabase.realtime.connect()
                
                // Limpa canal anterior se houver
                channel.unsubscribe()
                
                channel = supabase.realtime.channel("partida_$partidaId")
                
                // Ouve atualizações de outros celulares
                channel.broadcastFlow<PlacarState>("update")
                    .onEach { state ->
                        isUpdatingFromRemote = true
                        atualizarEstadoLocal(state)
                        isUpdatingFromRemote = false
                    }.launchIn(this)

                channel.subscribe()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun atualizarEstadoLocal(state: PlacarState) {
        pontosA = state.pontosA
        pontosB = state.pontosB
        setsA = state.setsA
        setsB = state.setsB
        saqueJogadorA = state.saqueJogadorA
        jogoFinalizado = state.jogoFinalizado
        nomeCampeao = state.nomeCampeao
        jogadorA = state.jogadorA
        jogadorB = state.jogadorB
        idiomaSelecionado = state.idiomaSelecionado
    }

    private fun broadcastUpdate() {
        if (isUpdatingFromRemote) return // Evita loop infinito
        
        viewModelScope.launch {
            try {
                val state = PlacarState(
                    pontosA, pontosB, setsA, setsB, 
                    saqueJogadorA, jogoFinalizado, nomeCampeao,
                    jogadorA, jogadorB, idiomaSelecionado
                )
                channel.broadcast("update", state)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun emitSound(type: SoundType) {
        viewModelScope.launch { _soundEvents.emit(type) }
    }

    // 4. FUNÇÃO DE SOMAR PONTO
    fun somarPonto(jogador: String) {
        if (jogoFinalizado) return

        if (jogador == "A") {
            pontosA++
        } else {
            pontosB++
        }

        emitSound(SoundType.PONTO)
        verificarTrocaDeSaque()
        verificarFimDoSet()
        broadcastUpdate()
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
        // Regra Oficial: Ganha quem chega a 11 pontos com 2 de vantagem
        if (pontosA >= 11 && (pontosA - pontosB) >= 2) {
            setsA++
            if (setsA >= 2) { // Melhor de 3 sets define o campeão
                jogoFinalizado = true
                nomeCampeao = jogadorA
                emitSound(SoundType.CAMPEAO)
            } else {
                emitSound(SoundType.SET_FINALIZADO)
                resetarPontosDoSet()
            }
        } else if (pontosB >= 11 && (pontosB - pontosA) >= 2) {
            setsB++
            if (setsB >= 2) {
                jogoFinalizado = true
                nomeCampeao = jogadorB
                emitSound(SoundType.CAMPEAO)
            } else {
                emitSound(SoundType.SET_FINALIZADO)
                resetarPontosDoSet()
            }
        }
    }

    private fun resetarPontosDoSet() {
        pontosA = 0
        pontosB = 0
        saqueJogadorA = true
    }

    // 🧹 5. LIMPEZA AUTOMÁTICA
    fun reiniciarPartidaELimparCache() {
        pontosA = 0
        pontosB = 0
        setsA = 0
        setsB = 0
        saqueJogadorA = true
        jogoFinalizado = false
        nomeCampeao = ""
        jogadorA = ""
        jogadorB = ""
        listaInscritos.clear()
        emitSound(SoundType.RESET)
        broadcastUpdate()
    }
}
