package com.campeonato.tenismesa.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

// Conexão oficial com o banco de dados do campeonato
val supabase = createSupabaseClient(
    supabaseUrl = "https://iakjamcqlcurosynbjpg.supabase.co",
    supabaseKey = "sb_publishable_LMhQ0POM-Jp6U9VfBDUt2Q_ib2AVVdG"
) {
    install(Postgrest)
    install(Realtime)
}
