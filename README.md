# Hotel Spring Boot - Pruebas Unitarias con JUnit 5 y Mockito

Proyecto de sistema de reservas hoteleras desarrollado con Spring Boot.
Este repositorio incluye pruebas unitarias implementadas con JUnit 5 y Mockito como parte del taller de la asignatura.

---

## Tecnologías utilizadas

- Java 17
- Spring Boot 3.3.4
- Spring Data JPA
- PostgreSQL
- MapStruct
- JUnit 5
- Mockito

---

## Estructura del proyecto

```
src/
├── main/java/com/hotel/Hotel/
│   ├── controller/        → Controladores REST (ClienteController, ReservaController)
│   ├── domain/            → Entidades del dominio (Cliente, Habitacion, Reserva, etc.)
│   ├── dto/               → Objetos de transferencia de datos (request y response)
│   ├── exception/         → Manejo de excepciones personalizadas
│   ├── mapper/            → Mappers con MapStruct
│   ├── repository/        → Repositorios JPA
│   └── service/           → Lógica de negocio (ClienteService, ReservaService)
│
└── test/java/com/hotel/Hotel/
    ├── domain/
    │   └── ReservaTest.java          → Prueba de dominio puro (sin mocks)
    └── service/
        ├── ReservaServiceTest.java   → Prueba de servicio con Mockito (ejemplo del docente)
        └── ClienteServiceTest.java   → 5 pruebas unitarias (taller estudiante)
```

---

## Pruebas unitarias implementadas

### Ejemplo del docente - ReservaServiceTest.java

Prueba de ejemplo proporcionada por el docente que muestra el patrón base para pruebas con Mockito.

| # | Método probado | Escenario |
|---|---|---|
| 1 | `confirmar()` | Confirma exitosamente una reserva existente en estado PENDIENTE |

### Prueba de dominio - ReservaTest.java

Prueba directa sobre el dominio sin necesidad de mocks ni base de datos.

| # | Método probado | Escenario |
|---|---|---|
| 1 | `confirmar()` | Confirma reserva y verifica cambio de estado en habitación |

### Pruebas del estudiante - ClienteServiceTest.java

5 pruebas unitarias implementadas sobre `ClienteService` usando JUnit 5 y Mockito.

| # | Método probado | Escenario |
|---|---|---|
| 1 | `crear()` | Crea un cliente exitosamente cuando los datos son válidos |
| 2 | `crear()` | Lanza excepción cuando el nombre del cliente está vacío |
| 3 | `obtenerPorId()` | Retorna el cliente cuando se busca por un ID existente |
| 4 | `obtenerPorId()` | Lanza excepción cuando el cliente no existe |
| 5 | `listarTodos()` | Retorna la lista completa de clientes |

---

## Cómo ejecutar las pruebas

Desde la terminal en la raíz del proyecto:

```bash
# Correr todas las pruebas
./mvnw test

# Correr solo las pruebas del estudiante
./mvnw test -Dtest=ClienteServiceTest

# Correr solo las pruebas del docente
./mvnw test -Dtest=ReservaServiceTest
```

Resultado esperado:

```
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## Conceptos clave usados en las pruebas

- **`@ExtendWith(MockitoExtension.class)`** → Activa Mockito en la clase de prueba
- **`@Mock`** → Crea un objeto falso del repositorio (no toca la base de datos real)
- **`@InjectMocks`** → Crea el servicio real con los mocks inyectados adentro
- **`@Test`** → Marca el método como una prueba ejecutable por JUnit
- **`@Nested`** → Agrupa pruebas relacionadas dentro de la misma clase
- **`@DisplayName`** → Nombre descriptivo de la prueba para reportes
- **`when(...).thenReturn(...)`** → Le dice al mock qué devolver cuando se llama un método
- **`verify(...)`** → Verifica que el mock fue llamado correctamente
- **`assertEquals`** → Verifica que dos valores son iguales
- **`assertThrows`** → Verifica que se lanzó una excepción esperada
- **`assertNotNull`** → Verifica que el resultado no es nulo

---

## Patrón Arrange - Act - Assert

Todas las pruebas siguen este patrón:

```java
// Arrange → preparo los objetos y configuro los mocks
// Act     → llamo al método que quiero probar
// Assert  → verifico que el resultado fue el correcto
```
