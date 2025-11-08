package com.akshit.portfolio

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform