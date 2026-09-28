package exceptions

import exceptions.*
import models.*

class CodigoInvalidoException(mensaje: String) : Exception(mensaje)
class TarifaInvalidaException(mensaje: String) : Exception(mensaje)
class PacienteNoEncontradoException(mensaje: String) : Exception(mensaje)
class SinCapacidadException(mensaje: String) : Exception(mensaje)