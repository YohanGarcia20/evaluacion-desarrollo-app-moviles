package models

data class Ticket (
    val numeroTicket: Int,
    val paciente: Paciente,
    val tiempoAtendidoMinutos: Int,
    val montoCobrado: Double
)