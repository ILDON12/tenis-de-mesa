kotlin// 1. ENDEREÇO DA PASTA: Diz ao Android Studio que este arquivo está guardado dentro da pasta 'data'
package com.campeonato.tenismesa.data

// 2. IMPORTAÇÕES: São as ferramentas do Supabase que trouxemos da internet para usar no app
import io.github.jan_tennert.supabase.createSupabaseClient // Cria a conexão principal
import io.github.jan_tennert.supabase.postgrest.Postgrest   // Permite ler e salvar dados nas tabelas (atletas, jogos)
import io.github.jan_tennert.supabase.realtime.Realtime     // Faz o placar atualizar sozinho e ao vivo na tela de todo mundo

// 3. CRIAÇÃO DA CONEXÃO: Criamos a variável 'supabase' que o aplicativo inteiro vai usar para acessar o banco
val supabase = createSupabaseClient(

    // 4. URL DO SEU BANCO: O link exclusivo do SEU servidor na nuvem que você salvou
    supabaseUrl = "https://iakjamcqlcurosynbjpg.supabase.co",

    // 5. CHAVE DE ACESSO PÚBLICA: A sua chave "anon" que dá permissão de segurança para o app entrar no servidor
    supabaseKey = "sb_publishable_LMhQ0POM-Jp6U9VfBDUt2Q_ib2AVVdG"
) {
    // 6. ATIVANDO RECURSOS: Aqui dentro nós ativamos o que essa conexão é capaz de fazer

    install(Postgrest) // Ativa a permissão de salvar os dados (como quando o jogador se cadastrar)

    install(Realtime)  // Ativa o placar ao vivo (para a torcida ver os pontos mudando na mesma hora) [INDEX]
}