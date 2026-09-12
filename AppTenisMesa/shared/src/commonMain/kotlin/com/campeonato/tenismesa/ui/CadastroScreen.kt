package com.campeonato.tenismesa.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campeonato.tenismesa.viewmodel.PlacarViewModel
import com.campeonato.tenismesa.util.LocalSoundPlayer
import com.campeonato.tenismesa.util.SoundType
import com.campeonato.tenismesa.util.Language

@Composable
fun CadastroScreen(viewModel: PlacarViewModel, onCadastroConcluido: () -> Unit) {
    val soundPlayer = LocalSoundPlayer.current
    val strings = viewModel.strings

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp)
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = strings.tituloConfiguracao,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        // Seleção de Idioma
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            items(Language.values()) { lang ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (viewModel.idiomaSelecionado == lang.code) Color.White.copy(alpha = 0.2f) else Color.Transparent)
                        .clickable { viewModel.idiomaSelecionado = lang.code }
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = lang.flag, fontSize = 24.sp)
                }
            }
        }

        OutlinedTextField(
            value = viewModel.jogadorA,
            onValueChange = { viewModel.jogadorA = it },
            label = { Text(strings.nomeJogadorA, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFD32F2F),
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.jogadorB,
            onValueChange = { viewModel.jogadorB = it },
            label = { Text(strings.nomeJogadorB, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFF1976D2),
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.partidaId,
            onValueChange = { viewModel.partidaId = it },
            label = { Text(strings.idPartida, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.Yellow,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                soundPlayer?.play(SoundType.PONTO)
                onCadastroConcluido()
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text(strings.iniciarPartida, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = strings.dicaSincronizacao,
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
