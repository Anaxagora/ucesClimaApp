🌩️ UCES Clima App
Aplicación móvil desarrollada para el examen de Programación de Aplicaciones Móviles II (Kotlin – Android Studio)
📌 Descripción general
UCES Clima App es una aplicación móvil desarrollada con Kotlin y Android Studio, cuyo objetivo es brindar información meteorológica actual y un pronóstico extendido de 10 días, consumiendo datos reales desde la API de StormGlass.
La aplicación fue desarrollada con fines académicos y todos los integrantes del grupo participaron activamente en el proyecto.

🧭 Características principales
✔ 1. Registro de Ubicaciones
El usuario puede agregar ciudades manualmente ingresando:
Nombre de la ciudad
Latitud
Longitud
Las ubicaciones se guardan de forma persistente utilizando SQLite mediante un repositorio y un SQLiteOpenHelper.

✔ 2. Listado de Ubicaciones (Pantalla 1)
Lista todas las ciudades guardadas.
Cada ítem muestra ciudad, latitud y longitud.
Botón para eliminar ubicaciones mediante el repositorio SQLite.
Al tocar una ciudad, accedés a la pantalla del clima actual.

✔ 3. Clima Actual (Pantalla 2)
Consume la API weather/point de StormGlass y muestra:
Temperatura
Humedad
Viento
Nubosidad
Visibilidad
Lluvia
Presión
Hora del dato más cercano a la actual (se selecciona automáticamente usando una función que busca la hora más próxima en el array "hours").
Incluye botón para volver y botón para acceder al Pronóstico Extendido.

✔ 4. Pronóstico Extendido 10 días (Pantalla 3)
Para cada uno de los próximos 10 días se muestra:
Temperatura estimada
Probabilidad de lluvia
Para obtenerlo, la app:
Descarga el array de horas de StormGlass (hasta 10 días).
Agrupa las horas por día.
Calcula:
Promedio de temperatura para ese día
Suma promedio de precipitación como probabilidad de lluvia
Lo muestra en un RecyclerView.
Incluye botón para volver a la pantalla del clima actual.

🏗️ Arquitectura utilizada
La app está construida siguiendo el patrón MVVM (Model–View–ViewModel):
Model
Clases de datos (WeatherResponse, HourData, DataPoint).
Repositorio LocationRepository que maneja acceso a SQLite.
Cliente Retrofit para comunicación con StormGlass.
ViewModel
WeatherViewModel:
Llama a Retrofit.
Maneja LiveData de clima y errores.
Expone resultados para las vistas.
View
Activities y Fragments con ViewBinding.
RecyclerView para listas.
Observadores de LiveData para actualizar la UI de forma reactiva.

🗄️ Persistencia con SQLite
La app usa:
LocationDBHelper → extiende SQLiteOpenHelper, crea la tabla y maneja upgrades.
LocationRepository → inserta, lee y elimina ubicaciones.
La data persiste aunque se cierre la app.

🌐 Consumo de API (StormGlass)
Endpoint utilizado:
GET /v2/weather/point
Parámetros:
lat = latitud
lng = longitud
params = airTemperature, cloudCover, humidity, precipitation, visibility, windDirection, windSpeed, pressure
Cómo se utiliza la API en la app:
Retrofit realiza la llamada usando una API Key.
La respuesta contiene un array hours con datos horarios.
Para la pantalla de clima actual:
→ Se selecciona la hora más cercana a la hora actual del dispositivo.
Para la pantalla de pronóstico extendido:
→ Se agrupan las horas por día y se calculan estadísticas básicas.

🧪 Requisitos del examen cumplidos
Requisito	Implementado
Consumo de API real con API Key	✅ StormGlass
3 pantallas / Activities	✅ Ubicaciones, Clima Actual, Pronóstico
Arquitectura MVVM	✅ ViewModel + LiveData
Persistencia con SQLite	✅ Tabla de ubicaciones
Uso de Fragments o RecyclerViews	✅ RecyclerView en Pantalla 1 y 3
Navegación entre pantallas	✅ Con intents y botones
Diseño limpio utilizando ViewBinding	✅ Todo el proyecto lo usa

🧑‍🎓 Nota final
Este proyecto fue desarrollado con fines puramente académicos, sin fines comerciales.
Todos los integrantes del grupo participaron activamente en el diseño, desarrollo, pruebas y documentación de la aplicación.
