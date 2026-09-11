package com.campeonato.tenismesa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campeonato.tenismesa.viewmodel.PlacarViewModel

@Composable
fun PlacarScreen(viewModel: PlacarViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // Fundo escuro elegante
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // TÍTULO DO JOGO
        Text(
            text = "CAMPEONATO DE TÊNIS DE MESA",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        // PLACAR DE SETS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "Sets: ${viewModel.setsA}", color = Color.Yellow, fontSize = 24.sp)
            Text(text = "Sets: ${viewModel.setsB}", color = Color.Yellow, fontSize = 24.sp)
        }

        // BLOCO DOS DOIS BOTÕES GRANDES DE PONTOS
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // BOTÃO JOGADOR A (VERMELHO)
            Button(
                onClick = { viewModel.somarPonto("A") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "JOGADOR A", color = Color.White, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "${viewModel.pontosA}", color = Color.White, fontSize = 64.sp, fontWeight = FontWeight.Bold)
                    if (viewModel.saqueJogadorA) {
                        Text(text = "🏓 SAQUE", color = Color.Yellow, fontSize = 14.sp)
                    }
                }
            }

            // BOTÃO JOGADOR B (AZUL)
            Button(
                onClick = { viewModel.somarPonto("B") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "JOGADOR B", color = Color.White, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "${viewModel.pontosB}", color = Color.White, fontSize = 64.sp, fontWeight = FontWeight.Bold)
                    if (!viewModel.saqueJogadorA) {
                        Text(text = "🏓 SAQUE", color = Color.Yellow, fontSize = 14.sp)
                    }
                }
            }
        }

        // BOTÃO DE REINICIAR PARTIDA
        Button(
            onClick = { viewModel.reiniciarPartidaCompleta() },
            modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
        ) {
            Text(text = "REINICIAR JOGO", color = Color.White, fontSize = 16.sp)
        }
    }
}
