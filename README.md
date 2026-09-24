# Sistema de Gestión de Biblioteca — API REST

API REST para gestionar **autores**, **libros**, **usuarios** y **préstamos**, construida con Spring Boot y PostgreSQL.

## Tecnologías

- Java 21
- Spring Boot 4 (Web MVC, Data JPA, Validation)
- PostgreSQL
- Lombok
- SLF4J + Logback para logs

## Cómo ejecutarlo

1. Crear la base de datos en PostgreSQL:
   ```sql
   CREATE DATABASE biblioteca_db;
   ```
2. Configurar la conexión con variables de entorno (las credenciales no se guardan en el repositorio):

   | Variable | Obligatoria | Valor por defecto |
   |---|---|---|
   | `DB_PASSWORD` | Sí | — |
   | `DB_USERNAME` | No | `postgres` |
   | `DB_URL` | No | `jdbc:postgresql://localhost:5432/biblioteca_db` |

   En IntelliJ: *Run → Edit Configurations → Environment variables*, por ejemplo `DB_USERNAME=postgres;DB_PASSWORD=mi_clave`.
3. Ejecutar la aplicación:
   ```bash
   # Windows (PowerShell)
   $env:DB_PASSWORD="mi_clave"; ./mvnw spring-boot:run

   # Linux / macOS
   DB_PASSWORD=mi_clave ./mvnw spring-boot:run
   ```
   Las tablas se crean automáticamente (`spring.jpa.hibernate.ddl-auto=update`).
4. La API queda disponible en `http://localhost:8080`.

## Arquitectura

```
controller/   Endpoints REST. Reciben la petición, validan con @Valid y devuelven ResponseEntity.
service/      Interfaces con las operaciones de negocio.
service/impl/ Implementaciones: reglas de negocio, transacciones y logs.
repository/   Acceso a datos con Spring Data JPA (+ una consulta personalizada con EntityManager).
entity/       Entidades JPA mapeadas a las tablas.
dto/          Objetos de entrada (RequestDTO) y salida (ResponseDTO) de la API.
enums/        Enum Genero.
exception/    Excepciones de negocio y manejador global de errores.
```

Flujo de una petición: `Controller → Service → Repository → Base de datos`, y la respuesta vuelve convertida en DTO.

### Modelo de datos

```
usuarios 1 ── N prestamos N ── 1 libros N ── 1 autores
```

- Un autor tiene muchos libros; un libro tiene un solo autor.
- Un préstamo pertenece a un libro y a un usuario.

## Endpoints

### Autores
| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| POST | `/autores` | Crear un autor | 201 |
| GET | `/autores` | Listar autores | 200 |
| GET | `/autores/{id}` | Obtener un autor | 200 / 404 |
| GET | `/autores/{id}/libros` | Libros de un autor | 200 / 404 |

### Libros
| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| POST | `/libros` | Crear un libro | 201 / 404 / 409 |
| GET | `/libros` | Listar libros | 200 |
| GET | `/libros/{id}` | Obtener un libro | 200 / 404 |
| PUT | `/libros/{id}` | Actualizar un libro | 200 / 404 / 409 |
| DELETE | `/libros/{id}` | Eliminar un libro | 204 / 404 / 409 |

### Usuarios
| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| POST | `/usuarios` | Registrar un usuario | 201 / 409 |
| GET | `/usuarios` | Listar usuarios | 200 |
| GET | `/usuarios/{id}` | Obtener un usuario | 200 / 404 |

### Préstamos
| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| POST | `/prestamos` | Registrar un préstamo | 201 / 404 / 409 |
| GET | `/prestamos` | Listar préstamos | 200 |
| GET | `/prestamos/usuario/{usuarioId}` | Préstamos de un usuario | 200 / 404 |
| PUT | `/prestamos/{id}/devolver` | Marcar como devuelto | 200 / 404 / 409 |

## Ejemplos de uso

Crear un autor:
```json
POST /autores
{ "nombre": "Gabriel García Márquez", "nacionalidad": "Colombiana", "fechaNacimiento": "1927-03-06" }
```

Crear un libro (géneros válidos: `NOVELA`, `CIENCIA_FICCION`, `FANTASIA`, `HISTORIA`, `POESIA`, `TERROR`, `BIOGRAFIA`, `INFANTIL`):
```json
POST /libros
{ "titulo": "Cien años de soledad", "isbn": "978-0307474728", "anioPublicacion": 1967, "genero": "NOVELA", "autorId": 1 }
```

Registrar un usuario:
```json
POST /usuarios
{ "nombre": "Ana Pérez", "email": "ana@mail.com" }
```

Registrar un préstamo:
```json
POST /prestamos
{ "usuarioId": 1, "libroId": 1, "fechaPrestamo": "2026-09-24", "fechaDevolucion": "2026-10-08" }
```

## Reglas de negocio

- El ISBN de un libro es único (al crear y al actualizar).
- El año de publicación no puede ser futuro.
- El autor de un libro, y el usuario y el libro de un préstamo, deben existir.
- No se puede prestar un libro que tiene un préstamo sin devolver.
- La fecha de devolución debe ser posterior a la fecha de préstamo.
- Un préstamo no se puede devolver dos veces.
- No se puede eliminar un libro que tiene préstamos registrados.
- El email de un usuario es único.

## Manejo de errores

Todos los errores responden con el mismo formato:
```json
{
  "fecha": "2026-09-24T10:15:30",
  "estado": 409,
  "error": "Conflict",
  "mensaje": "El libro 1 ya se encuentra prestado",
  "detalles": {}
}
```

| Código | Cuándo |
|---|---|
| 400 | Datos inválidos (validaciones del DTO, JSON mal formado, género inexistente, id no numérico). En validaciones, `detalles` indica el error de cada campo. |
| 404 | El recurso no existe. |
| 409 | La operación viola una regla de negocio. |
| 500 | Error inesperado. |

## Decisiones de diseño

- **Entidad `Usuario`:** el enunciado indica que el préstamo tiene una relación muchos a uno con el usuario, así que se modeló como entidad (en lugar de un texto). Por eso se agregaron los endpoints `/usuarios`, y `/prestamos/usuario/{usuarioId}` recibe el id del usuario.
- **DTOs de entrada y salida separados:** la API nunca expone las entidades. Esto evita ciclos en la serialización (autor → libros → autor), impide que el cliente envíe campos que no le corresponden (como el `id`) y desacopla la API de la base de datos.
- **Validaciones en dos capas:** Bean Validation en los DTOs (respuesta 400 clara) y restricciones en la base de datos (`NOT NULL`, `UNIQUE`) como última barrera.
- **Manejo de errores centralizado** con `@RestControllerAdvice`: los services lanzan excepciones de negocio y un único manejador las traduce a códigos HTTP.
- **Transacciones:** los services son `@Transactional(readOnly = true)` por defecto; los métodos que escriben usan `@Transactional`.
- **Relaciones `LAZY` y `open-in-view` desactivado:** el acceso a datos ocurre solo dentro de la capa de servicio.
- **Consultas sin N+1:** el listado de libros usa `@Query` con `JOIN FETCH`, y los préstamos usan una consulta personalizada con `EntityManager` (`PrestamoRepositoryCustomImpl`) que trae usuario y libro en una sola consulta.

## Posibles mejoras

- Migraciones con Flyway y `ddl-auto=validate`.
- Guardar la fecha real de devolución para detectar retrasos.
- Paginación en los listados.
- Documentación interactiva con OpenAPI/Swagger.
- Pruebas unitarias y de integración.
