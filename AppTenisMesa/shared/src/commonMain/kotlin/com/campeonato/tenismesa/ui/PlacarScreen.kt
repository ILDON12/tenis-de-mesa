package com.campeonato.tenismesa.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campeonato.tenismesa.viewmodel.PlacarViewModel
import com.campeonato.tenismesa.util.LocalSoundPlayer

@Composable
fun PlacarScreen(viewModel: PlacarViewModel, onVoltar: () -> Unit) {
    val soundPlayer = LocalSoundPlayer.current
    val strings = viewModel.strings

    LaunchedEffect(viewModel) {
        viewModel.soundEvents.collect { type ->
            soundPlayer?.play(type)
        }
    }

    // Animação de pulso para o indicador de saque
    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1A1A1A), Color(0xFF000000))))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Título no topo
        Text(
            text = strings.tituloMesa,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 24.dp),
            letterSpacing = 1.sp
        )

        // Exibição do Vencedor com Animação Premium
        AnimatedVisibility(
            visible = viewModel.jogoFinalizado,
            enter = fadeIn(tween(1000)) + scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
            exit = fadeOut(),
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = strings.textoVencedor,
                        color = Color(0xFFFFD700),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = viewModel.nomeCampeao.uppercase(),
                        color = Color.White,
                        fontSize = 54.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        if (!viewModel.jogoFinalizado) {
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Placar de Sets
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SetIndicator(label = strings.sets, count = viewModel.setsA, color = Color(0xFFD32F2F))
                    SetIndicator(label = strings.sets, count = viewModel.setsB, color = Color(0xFF1976D2))
                }

                // Botões de Pontos
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ScoreButton(
                        name = viewModel.jogadorA,
                        score = viewModel.pontosA,
                        isServing = viewModel.saqueJogadorA,
                        color = Color(0xFFD32F2F),
                        pulseScale = pulseScale,
                        serveText = strings.textoSaque,
                        onClick = { viewModel.somarPonto("A") }
                    )

                    ScoreButton(
                        name = viewModel.jogadorB,
                        score = viewModel.pontosB,
                        isServing = !viewModel.saqueJogadorA,
                        color = Color(0xFF1976D2),
                        pulseScale = pulseScale,
                        serveText = strings.textoSaque,
                        onClick = { viewModel.somarPonto("B") }
                    )
                }
            }
        }

        // Botões de Ação na base (Voltar e Reiniciar)
        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onVoltar,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.15f))
            ) {
                Text(text = "← ${strings.voltar}", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { viewModel.reiniciarPartidaELimparCache() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text(text = strings.botaoReiniciar, color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SetIndicator(label: String, count: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(text = "$count", color = color, fontSize = 32.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
fun RowScope.ScoreButton(
    name: String,
    score: Int,
    isServing: Boolean,
    color: Color,
    pulseScale: Float,
    serveText: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.weight(1f).fillMaxHeight(),
        shape = RoundedCornerShape(24.dp),
        color = color.copy(alpha = 0.15f),
        border = if (isServing) BorderStroke(2.dp, color) else null
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = name.ifBlank { "---" }.uppercase(),
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            AnimatedContent(
                targetState = score,
                transitionSpec = {
                    (slideInVertically { it } + fadeIn() togetherWith
                            slideOutVertically { -it } + fadeOut()).using(SizeTransform(clip = false))
                }
            ) { targetCount ->
                Text(
                    text = "$targetCount",
                    color = color,
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Black
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            if (isServing) {
                Text(
                    text = serveText,
                    color = Color.Yellow,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.scale(pulseScale)
                )
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
