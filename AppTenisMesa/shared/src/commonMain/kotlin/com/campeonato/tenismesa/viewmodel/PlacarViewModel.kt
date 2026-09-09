// 1. VARIÁVEIS DO PLACAR: Guardam os pontos e sets atuais da partida na memória do celular
var pontosA by androidx.compose.runtime.mutableStateOf(0)
var pontosB by androidx.compose.runtime.mutableStateOf(0)
var setsA by androidx.compose.runtime.mutableStateOf(0)
var setsB by androidx.compose.runtime.mutableStateOf(0)

// 2. CONTROLE DE SAQUE: Diz de quem é a vez de sacar (true = Jogador A, false = Jogador B)
var saqueJogadorA by androidx.compose.runtime.mutableStateOf(true)

// 3. FUNÇÃO DE SOMAR PONTO: Chamada toda vez que o juiz clica no botão colorido na mesa
fun somarPonto(jogador: String) {
    if (jogador == "A") {
        pontosA++
    } else {
        pontosB++
    }

    // Toda vez que alguém faz ponto, o app confere o saque e as regras de vitória
    verificarTrocaDeSaque()
    verificarFimDoSet()
}

// 4. REGRA DO SAQUE: No tênis de mesa, o saque muda a cada 2 pontos. Se empatar em 10x10, muda a cada 1 ponto.
private fun verificarTrocaDeSaque() {
    val totalPontos = pontosA + pontosB

    if (pontosA >= 10 && pontosB >= 10) {
        // Empatou em 10x10? Troca o saque a cada 1 ponto jogado
        saqueJogadorA = !saqueJogadorA
    } else {
        // Jogo normal? Troca o saque a cada 2 pontos jogados
        if (totalPontos % 2 == 0) {
            saqueJogadorA = !saqueJogadorA
        }
    }
}

// 5. REGRA DE VITÓRIA DO SET: Vence quem chega a 11 pontos primeiro, com pelo menos 2 pontos de vantagem!
private fun verificarFimDoSet() {
    if (pontosA >= 11 && (pontosA - pontosB) >= 2) {
        setsA++
        zerarPlacarDoSet()
    } else if (pontosB >= 11 && (pontosB - pontosA) >= 2) {
        setsB++
        zerarPlacarDoSet()
    }
}

// 6. LIMPEZA DA MESA: Zera os pontos mantendo o placar de sets quando o set acaba
private fun zerarPlacarDoSet() {
    pontosA = 0
    pontosB = 0
    saqueJogadorA = true // O primeiro saque do novo set volta para o jogador A
}

// 7. REINICIAR TUDO: Zera o jogo inteiro se o juiz clicar no botão de reset
fun reiniciarPartidaCompleta() {
    pontosA = 0
    pontosB = 0
    setsA = 0
    setsB = 0
    saqueJogadorA = true
}