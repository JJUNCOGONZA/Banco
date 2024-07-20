Buen día equipo Devsu,

Agradezco la oportunidad de participar en esta prueba. He completado todos los requerimientos del test y quedo atento a sus comentarios.

El proyecto está dockerizado, incluyendo tanto los microservicios como el servidor de base de datos MySQL. Para mayor accesibilidad, en el archivo `docker-compose.yml` se ha incluido la instrucción para ejecutar el script de creación de la base de datos, ubicado en la carpeta `init`.

## Microservicios

1. **Microservicio Usuarios**: 
   - Gestiona toda la lógica relacionada con personas y clientes.
   - Incluye pruebas unitarias.

2. **Microservicio Cuentas-Movimientos**: 
   - Implementa la lógica solicitada en los requerimientos.
   - Facilita la comunicación asíncrona entre microservicios, cumpliendo con todas las funcionalidades solicitadas.

## Postman

- **Banco.postman_collection.json**: Este archivo es una colección exportable de Postman para facilitar la consulta de los métodos.

## Instrucciones de Ejecución

Para levantar el proyecto, ejecutar el siguiente comando en la raíz del proyecto:

> docker-compose up


Acceso desde un visor de base de datos externo:

> URL: jdbc:mysql://localhost:3307/banco

> Usuario: root

> Contraseña: root

Saludos,

Jeisson Junco Gonzalez
