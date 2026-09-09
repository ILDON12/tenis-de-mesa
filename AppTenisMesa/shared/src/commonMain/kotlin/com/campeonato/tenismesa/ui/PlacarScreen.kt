// 1. ENDEREÇO DA PASTA: Organiza este arquivo dentro da pasta visual 'ui'
package com.campeonato.tenismesa.ui

// 2. IMPORTAÇÕES: Traz as ferramentas do Jetpack Compose para desenhar botões, textos e cores
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
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
    // 3. ESTRUTURA PRINCIPAL: Uma coluna que organiza os elementos de cima para baixo ocupando a tela toda
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // Deixa o fundo do app em um preto elegante (Modo Escuro)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // 4. CABEÇALHO: Texto grande com o nome do torneio no topo
        Text(
            text = "CAMPEONATO DE TÊNIS DE MESA",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        // 5. PLACAR DE SETS: Mostra quantos sets cada atleta já fechou na partida
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "Sets: ${viewModel.setsA}", color = Color.Yellow, fontSize = 24.sp)
            Text(text = "Sets: ${viewModel.setsB}", color = Color.Yellow, fontSize = 24.sp)
        }

        // 6. ÁREA DOS BOTÕES DE PONTOS: Coloca os dois botões gigantes lado a lado na mesa
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔴 BOTÃO DO JOGADOR A (Lado Vermelho)
            Button(
                onClick = { viewModel.somarPonto("A") }, // Quando o juiz clica, roda a função de somar 1 ponto
                modifier = Modifier.weight(1f).fillMaxHeight().padding(8.dp),
                shape = RoundedCornerShape(16.dp), // Deixa os cantos do botão arredondados
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFD32F2F))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "JOGADOR A", color = Color.White, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    // Mostra a pontuação atual do Atleta A na tela grande
                    Text(text = "${viewModel.pontosA}", color = Color.White, fontSize = 64.sp, fontWeight = FontWeight.Bold)

                    // Avisa visualmente se for a vez do Jogador A realizar o saque
                    if (viewModel.saqueJogadorA) {
                        Text(text = "🏓 SAQUE", color = Color.Yellow, fontSize = 14.sp)
                    }
                }
            }

            // 🔵 BOTÃO DO JOGADOR B (Lado Azul)
            Button(
                onClick = { viewModel.somarPonto("B") }, // Soma 1 ponto para o atleta B
                modifier = Modifier.weight(1f).fillMaxHeight().padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1976D2))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "JOGADOR B", color = Color.White, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "${viewModel.pontosB}", color = Color.White, fontSize = 64.sp, fontWeight = FontWeight.Bold)

                    // Se NÃO for o saque do jogador A, mostra o aviso de saque no lado B
                    if (!viewModel.saqueJogadorA) {
                        Text(text = "🏓 SAQUE", color = Color.Yellow, fontSize = 14.sp)
                    }
                }
            }
        }

        // 7. BOTÃO DE REINICIAR: Um botão cinza escuro na parte inferior para zerar a mesa inteira
        Button(
            onClick = { viewModel.reiniciarPartidaCompleta() },
            modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
        ) {
            Text(text = "REINICIAR JOGO", color = Color.White, fontSize = 16.sp)
        }
    }
}
