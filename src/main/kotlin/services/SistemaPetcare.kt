package services

import exceptions.*
import kotlinx.coroutines.delay
import models.*

// CORREGIDO: La 'C' de PetCare ahora es mayúscula para evitar errores de referencia en el Main
class SistemaPetCare {
    val nombreSistema = "PetCare"
    val boxes: List<Box> = List(10) { i -> Box(numero = i + 1) }
    val historialTickets: MutableList<Ticket> = mutableListOf()
    private var contadorTickets = 1

    // Validacion del formato de codigo: 2 letras, 2 numeros, 2 letras (R3)
    fun validarCodigoAtencion(codigo: String): Boolean {
        return Regex("^[A-Za-z]{2}\\d{2}[A-Za-z]{2}$").matches(codigo)
    }

    // Entrada asincrona simulando espera de sensor (3s) (R5)
    suspend fun registrarEntrada(paciente: Paciente) {
        if (!validarCodigoAtencion(paciente.codigoAtencion)) {
            throw CodigoInvalidoException("El código '${paciente.codigoAtencion}' no cumple con el formato requerido (XX00XX).")
        }

        val boxLibre = boxes.firstOrNull { it.estado is EstadoBox.Libre }
            ?: throw SinCapacidadException("No hay boxes disponibles en PetCare.")

        boxLibre.estado = EstadoBox.EnProceso("Registrando entrada...")
        println("[SENSOR ENTRADA] Conectando con sensor en Box ${boxLibre.numero} (esperando 3s)...")
        delay(3000)

        boxLibre.estado = EstadoBox.EnAtencion(paciente)
        println("-> ENTRADA OK: Paciente ${paciente.nombre} (${paciente.codigoAtencion}) asignado al Box ${boxLibre.numero}.\n")
    }

    // Salida asincrona simulando calculo (6.5s) (R5)
    suspend fun registrarSalida(codigoAtencion: String, tiempoMinutos: Int): Ticket {
        val boxEncontrado = boxes.firstOrNull { box ->
            when (val estado = box.estado) {
                is EstadoBox.EnAtencion -> estado.paciente.codigoAtencion.equals(codigoAtencion, ignoreCase = true)
                else -> false
            }
        } ?: throw PacienteNoEncontradoException("No se encontró paciente activo con el código: $codigoAtencion")

        val paciente = (boxEncontrado.estado as EstadoBox.EnAtencion).paciente

        boxEncontrado.estado = EstadoBox.EnProceso("Calculando tarifa...")
        println("[SENSOR SALIDA] Procesando salida del Box ${boxEncontrado.numero} para ${paciente.nombre} (esperando 6.5s)...")
        delay(6500)

        val montoTotal = paciente.calcularMontoFinal(tiempoMinutos)

        // Validacion de tarifa (R3 y R6)
        if (montoTotal <= 0.0 && !(paciente is Felino && tiempoMinutos < 20)) {
            boxEncontrado.estado = EstadoBox.EnAtencion(paciente)
            throw TarifaInvalidaException("Tarifa inválida ($montoTotal) calculada para ${paciente.nombre}.")
        }

        val ticket = Ticket(contadorTickets++, paciente, tiempoMinutos, montoTotal)
        historialTickets.add(ticket)
        boxEncontrado.estado = EstadoBox.Libre

        println("-> SALIDA OK: Ticket #${ticket.numeroTicket} para ${paciente.nombre}. Total pagado: $${montoTotal.toInt()}\n")
        return ticket
    }

    // Consultas de negocio utilizando funciones de orden superior (R4 - IE 1.2.3)
    fun contarBoxesDisponibles(): Int = boxes.count { it.estado is EstadoBox.Libre }

    fun obtenerPacientesConvenio(): List<Paciente> =
        historialTickets.map { it.paciente }.filter { it.tipoDueno == TipoDueno.Convenio }

    fun calcularIngresoPromedio(): Double =
        if (historialTickets.isEmpty()) 0.0 else historialTickets.map { it.montoCobrado }.average()

    fun obtenerCodigosFinalizados(): List<String> = historialTickets.map { it.paciente.codigoAtencion }

    fun obtenerPacienteMayorTiempo(): Paciente? =
        historialTickets.maxByOrNull { it.tiempoAtendidoMinutos }?.paciente

    // Reporte de Cierre de Turno (R4)
    fun generarReporteCierreTurno() {
        println("==================================================")
        println("       REPORTE DE CIERRE DE TURNO - PETCARE       ")
        println("==================================================")

        if (historialTickets.isEmpty()) {
            println("No hubo atenciones en este turno.")
            return
        }

        println("\nDETALLE DE TICKETS ATENDIDOS:")
        historialTickets.forEach { t ->
            val esSilvestreTxt = if (t.paciente is Exotico && t.paciente.esSilvestre) " [Silvestre]" else ""
            println("Ticket #${t.numeroTicket} | Tipo: ${t.paciente.javaClass.simpleName}$esSilvestreTxt | Código: ${t.paciente.codigoAtencion} | Tiempo: ${t.tiempoAtendidoMinutos}m | Cobro: $${t.montoCobrado.toInt()}")
        }

        val totalRecaudado = historialTickets.sumOf { it.montoCobrado }
        val tipoMasIngresos = historialTickets
            .groupBy { it.paciente.javaClass.simpleName }
            .maxByOrNull { entry -> entry.value.sumOf { it.montoCobrado } }?.key ?: "N/A"

        println("\nRESUMEN DEL TURNO:")
        println(" Total Recaudado: $${totalRecaudado.toInt()}")
        println(" Pacientes Atendidos: ${historialTickets.size}")
        println(" Ingreso Promedio: $${calcularIngresoPromedio().toInt()}")
        println(" Tipo con Más Ingresos: $tipoMasIngresos")
        println(" Boxes Disponibles al Cierre: ${contarBoxesDisponibles()}")
        println("==================================================\n")
    }
}