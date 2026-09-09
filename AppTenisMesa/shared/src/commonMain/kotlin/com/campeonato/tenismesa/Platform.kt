package com.campeonato.tenismesa

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform