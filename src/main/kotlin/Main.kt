import exceptions.*
import kotlinx.coroutines.runBlocking
import models.*
import services.SistemaPetCare

fun main() = runBlocking {
    val sistema = SistemaPetCare()

    println("=== SISTEMA DE GESTION VETERINARIA PETCARE ===\n")

    // Datos de prueba sugeridos en el enunciado
    val p1 = Canino("CA12CD", "Max", "Golden Retriever", "2026-09-28 08:00", TipoDueno.Convenio)
    val p2 = Canino("CA99ZA", "Luna", "Labrador", "2026-09-28 08:15", TipoDueno.Particular)
    val p3 = Felino("FE22TO", "Misi", "Siamés", "2026-09-28 08:30", TipoDueno.Particular)
    val p4 = Exotico("EX44RG", "Loro", "Amazónico", "2026-09-28 08:45", TipoDueno.Municipal, esSilvestre = true)
    val p5 = Exotico("EX77RG", "Iguana", "Verde", "2026-09-28 09:00", TipoDueno.Particular, esSilvestre = false)

    // 1. Prueba de control de error: Codigo invalido (R6 & IE 1.3.4)
    println("1. Probando error de código inválido:")
    try {
        val invalido = Canino("123ABC", "ErrorBot", "Mestizo", "2026-09-28 09:10", TipoDueno.Particular)
        sistema.registrarEntrada(invalido)
    } catch (e: CodigoInvalidoException) {
        println("   [Error Controlado]: ${e.message}\n")
    }

    // 2. Registro asincrono de entradas (R5 & IE 1.3.3)
    println("2. Registrando entradas de pacientes...")
    try {
        sistema.registrarEntrada(p1)
        sistema.registrarEntrada(p2)
        sistema.registrarEntrada(p3)
        sistema.registrarEntrada(p4)
        sistema.registrarEntrada(p5)
    } catch (e: Exception) {
        println("   Error en entrada: ${e.message}")
    }

    // 3. Registro asincrono de salidas y calculo de tarifas (R5)
    println("3. Registrando salidas con tiempos sugeridos...")
    try {
        sistema.registrarSalida("CA12CD", 75)  // Canino convenio (-20%)
        sistema.registrarSalida("CA99ZA", 180) // Canino particular
        sistema.registrarSalida("FE22TO", 18)  // Felino < 20 min ($0)
        sistema.registrarSalida("EX44RG", 120) // Exotico silvestre (+30%), municipal (-50%)
        sistema.registrarSalida("EX77RG", 45)  // Exotico particular
    } catch (e: Exception) {
        println("   Error en salida: ${e.message}")
    }

    // 4. Prueba de control de error: Paciente no encontrado (R6)
    println("4. Probando error de paciente no encontrado:")
    try {
        sistema.registrarSalida("XX00XX", 30)
    } catch (e: PacienteNoEncontradoException) {
        println("   [Error Controlado]: ${e.message}\n")
    }

    // 5. Consultas de negocio (R4 - IE 1.2.3)
    println("5. Ejecutando consultas de negocio:")
    println(" - Boxes disponibles: ${sistema.contarBoxesDisponibles()}")
    println(" - Pacientes con convenio: ${sistema.obtenerPacientesConvenio().map { it.nombre }}")
    println(" - Paciente con mas tiempo: ${sistema.obtenerPacienteMayorTiempo()?.nombre ?: "N/A"}")
    println(" - Codigos finalizados: ${sistema.obtenerCodigosFinalizados()}\n")

    // 6. Generacion del Reporte de Cierre de Turno (R4 & IE 1.2.4)
    sistema.generarReporteCierreTurno()
}