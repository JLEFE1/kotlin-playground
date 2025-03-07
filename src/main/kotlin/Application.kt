package com.homegrown

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabases(environment.config)
    configureMonitoring()
    configureRouting()
}
