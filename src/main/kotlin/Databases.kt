package com.homegrown

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection
import java.sql.DriverManager

fun Application.configureDatabases(config: ApplicationConfig) {
    Class.forName("org.postgresql.Driver")
    val url = config.property("postgres.url").getString()
    val user = config.property("postgres.user").getString()
    val password = config.property("postgres.password").getString()

    val dbConnection: Connection = DriverManager.getConnection(url, user, password)
    val cityService = CityService(dbConnection)

    routing {

        // Create city
        post("/cities") {
            val city = call.receive<City>()
            val id = cityService.create(city)
            call.respond(HttpStatusCode.Created, id)
        }

        // Read city
        get("/cities") {
            try {
                val cities = cityService.readAll()
                call.respond(HttpStatusCode.OK, cities)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // Read city
        get("/cities/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            try {
                val city = cityService.read(id)
                call.respond(HttpStatusCode.OK, city)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // Update city
        put("/cities/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<City>()
            cityService.update(id, user)
            call.respond(HttpStatusCode.OK)
        }

        // Delete city
        delete("/cities/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            cityService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}
