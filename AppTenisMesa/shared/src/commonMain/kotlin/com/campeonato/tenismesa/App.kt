package com.campeonato.tenismesa

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.campeonato.tenismesa.ui.PlacarScreen
import com.campeonato.tenismesa.viewmodel.PlacarViewModel

@Composable
fun App() {
    MaterialTheme {
        // 1. CRIAR O CÉREBRO: Inicializa a lógica dos 11 pontos na memória do app
        val viewModel = remember { PlacarViewModel() }

        // 2. CARREGAR A TELA: Chama a tela visual dos botões grandes que você criou
        PlacarScreen(viewModel = viewModel)
    }
}
