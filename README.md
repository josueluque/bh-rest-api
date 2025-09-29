
# API REST

_API REST que consume y procesa datos de [JSONPlaceholder](https://jsonplaceholder.typicode.com)._

### 📋 Pre-requisitos 

_Asegurate de tener las siguientes herramientas, para este proyecto se usaron las siguiente versiones_

- Java 17 
- Maven 3.9.4
- SpringBoout 3.5.6

----
### Instalación 🔧

_1) Clonar este repositorio_
```
git clone https://github.com/josueluque/bh-rest-api.git
cd bh-rest-api
```
_2) Instalar dependencias del proyecto_
```
mvn clean install
```
_3) Ejecutar la aplicación_
```
mvn spring-boot:run
```
----
### 📚 Pruebas y documentación con Swagger 

Este proyecto expone su documentación de API usando Swagger UI.
Una vez levantada la aplicación, podés acceder a la interfaz web en: http://localhost:8080/swagger-ui.html


##### Obtener posts paginados con comentarios y usuario
```http
GET /api/posts
```

Ejemplo
```
curl 'http://localhost:8080/api/posts?page=2&size=8'
```
Estructura de respuesta Body
```json
[
  {
    "post": {
      "id": 1,
      "title": "titulo del post",
      "body": "contenido del post"
    },
    "user": {
      "id": 1,
      "name": "Leanne Graham",
      "email": "Sincere@april.biz"
    },
    "comments": [
      {
        "id": 1,
        "name": "comentario",
        "email": "email@ejemplo.com",
        "body": "contenido del comentario"
      }
    ]
  }
]
```


##### Eliminar un post por su id

```
DELETE /api/posts/{id}
```
Ejemplo
```
curl -X DELETE "http://localhost:8080/api/posts/10"
```
Respuestas posibles:
- 204 No Content → Se eliminó correctamente (simulación)
- 404 Not Found → No existe el post
- 500 Internal Server Error → Error al llamar a la API externa

----
### 🏗️ Descripción de la arquitectura 
- Controller: Maneja solicitudes HTTP y devuelve respuestas JSON.

- Service: Contiene la lógica y realiza llamadas a servicio externo.

- Client / RestTemplate: Gestiona llamadas a API externa (JSONPlaceholder).

- DTOs: Transfieren únicamente los datos necesarios.

### 🛠️ Stack
- Java 17 – Lenguaje de programación principal.

- Maven → Gestión de dependencias y compilación del proyecto.

- Spring Boot 3.5.6  → Framework para desarrollo de aplicaciones RESTful.

- RestTemplate → Cliente HTTP para consumir APIs externas.

- Springdoc OpenAPI / Swagger UI  → Documentación y pruebas de los endpoints.

- Jackson / JSON → Serialización y deserialización de datos JSON.

- Spring Web / Spring MVC  → Para la creación de endpoints REST.