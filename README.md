PetCare - Sistema de Gestión de Boxes Veterinarios

Descripción
Aplicación de consola desarrollada en Kotlin para la gestión de boxes en clínicas veterinarias. Este proyecto resuelve la Evaluación Parcial 1 (DSY1105) y aplica conceptos de Programación Orientada a Objetos (herencia, polimorfismo), manipulación funcional de colecciones y tareas asíncronas utilizando corrutinas.

Requisitos Previos
- JDK 17 (o superior).
- IntelliJ IDEA (Community o Ultimate).
- Conexión a internet para la primera carga de dependencias de Gradle.

Instrucciones de Ejecución
1. Clona o descarga el repositorio y ábrelo con **IntelliJ IDEA**.
2. Al abrir el proyecto, permite que Gradle sincronice automáticamente las dependencias (necesario para descargar `kotlinx-coroutines-core`).
3. Navega en el panel de proyecto hasta la ruta: `src/main/kotlin/Main.kt`.
4. Haz clic derecho sobre el archivo `Main.kt` y selecciona Run 'MainKt' (o presiona el ícono verde de "Play" junto a la función `main`).
5. La simulación se ejecutará en la terminal integrada mostrando el registro asíncrono de entradas/salidas, la captura de errores controlados y el reporte de cierre de turno.

Estructura del Código Fuente
El código está modularizado para cumplir con los requerimientos técnicos del caso:
- `models/`: Contiene el modelado de datos, jerarquía de pacientes, tipos de dueños y la máquina de estados de los boxes (`sealed class`).
- `exceptions/`: Define las excepciones personalizadas para evitar cierres abruptos del sistema.
- `services/`: Contiene `SistemaPetCare.kt`, encargado de la lógica de negocio, búsquedas funcionales en colecciones y simulaciones de hardware mediante `suspend fun`.
- `Main.kt`: Punto de entrada que orquesta el caso de prueba principal.
