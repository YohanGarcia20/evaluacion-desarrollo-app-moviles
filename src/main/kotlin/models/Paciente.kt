package models

// Clase base abierta para herencia y polimorfismo
open class Paciente (
    val codigoAtencion: String, // CORREGIDO: antes decía codigoztencion
    val nombre: String,
    val especie: String,
    val fechaHoraIngreso: String,
    val tipoDueno: TipoDueno
) {
    // CORREGIDO: Este es el método que faltaba y que las clases hijas necesitan sobrescribir
    open fun calcularCostoBase(minutos: Int): Double {
        return 0.0
    }

    fun calcularMontoFinal(minutos: Int): Double {
        val costoBase = calcularCostoBase(minutos)

        // Si el costo base es 0 (felino < 20 min), el total es 0
        if (costoBase == 0.0) return 0.0

        val costoConIVA = costoBase * 1.19

        return if (tipoDueno == TipoDueno.Municipal) {
            costoConIVA * 0.50
        } else {
            costoConIVA
        }
    }
}

class Canino(
    codigoAtencion: String,
    nombre: String,
    especie: String,
    fechaHoraIngreso: String,
    tipoDueno: TipoDueno
) : Paciente(codigoAtencion, nombre, especie, fechaHoraIngreso, tipoDueno) {

    override fun calcularCostoBase(minutos: Int): Double {
        val subtotal = minutos * (12000.0 / 60.0) // $200 por minuto
        return if (tipoDueno == TipoDueno.Convenio) subtotal * 0.80 else subtotal
    }
}

class Felino(
    codigoAtencion: String,
    nombre: String,
    especie: String,
    fechaHoraIngreso: String,
    tipoDueno: TipoDueno
) : Paciente(codigoAtencion, nombre, especie, fechaHoraIngreso, tipoDueno) {

    override fun calcularCostoBase(minutos: Int): Double {
        if (minutos < 20) return 0.0
        return minutos * (9000.0 / 60.0) // $150 por minuto
    }
}

class Exotico(
    codigoAtencion: String,
    nombre: String,
    especie: String,
    fechaHoraIngreso: String,
    tipoDueno: TipoDueno,
    val esSilvestre: Boolean
) : Paciente(codigoAtencion, nombre, especie, fechaHoraIngreso, tipoDueno) {

    override fun calcularCostoBase(minutos: Int): Double {
        val subtotal = minutos * (20000.0 / 60.0)
        return if (esSilvestre) subtotal * 1.30 else subtotal
    }
}