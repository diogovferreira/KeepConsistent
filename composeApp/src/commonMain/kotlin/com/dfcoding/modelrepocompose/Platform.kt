package com.dfcoding.modelrepocompose

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform