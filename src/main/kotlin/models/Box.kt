package models

data class Box(
    val numero: Int,
    var estado: EstadoBox = EstadoBox.Libre
)

