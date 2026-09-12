package com.campeonato.tenismesa

import androidx.compose.animation.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.campeonato.tenismesa.ui.CadastroScreen
import com.campeonato.tenismesa.ui.PlacarScreen
import com.campeonato.tenismesa.viewmodel.PlacarViewModel
import com.campeonato.tenismesa.util.LocalSoundPlayer
import com.campeonato.tenismesa.util.rememberSoundPlayer

@Composable
fun App() {
    val soundPlayer = rememberSoundPlayer()
    CompositionLocalProvider(LocalSoundPlayer provides soundPlayer) {
        MaterialTheme {
            // 1. Inicializa a lógica do placar na memória do aplicativo
            val viewModel = remember { PlacarViewModel() }

            // 2. Controla qual tela deve aparecer (começa na tela de cadastro)
            var mostrarPlacarDeJogo by remember { mutableStateOf(false) }

            AnimatedContent(
                targetState = mostrarPlacarDeJogo,
                transitionSpec = {
                    if (targetState) {
                        slideInHorizontally { it } + fadeIn() togetherWith
                                slideOutHorizontally { -it } + fadeOut()
                    } else {
                        slideInHorizontally { -it } + fadeIn() togetherWith
                                slideOutHorizontally { it } + fadeOut()
                    }
                }
            ) { targetMostrarPlacar ->
                if (targetMostrarPlacar) {
                    // Se o cadastro foi concluído, abre o placar com os botões grandes
                    PlacarScreen(
                        viewModel = viewModel,
                        onVoltar = { mostrarPlacarDeJogo = false }
                    )
                } else {
                    // Tela inicial onde os amigos digitam os nomes
                    CadastroScreen(
                        viewModel = viewModel,
                        onCadastroConcluido = { mostrarPlacarDeJogo = true }
                    )
                }
            }
        }
    }
}
