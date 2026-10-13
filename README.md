# InmoSmartMobile

App móvil para gestión inmobiliaria.

INMOSMART Mobile es una aplicación Android desarrollada como continuidad del proyecto web INMOSMART. Su objetivo es trasladar las principales funcionalidades del sistema inmobiliario a un entorno móvil, permitiendo a los usuarios consultar inmuebles y realizar operaciones desde su dispositivo.

> **Estado actual:** proyecto académico en desarrollo. La app ya se comunica con el backend Django mediante una API REST (registro, login y perfil de usuario). Las demás funcionalidades se están incorporando progresivamente, según el Product Backlog y los sprints del equipo. 

## Tecnologías

**Backend**
- Python
- Django 4.2
- Django REST Framework
- Autenticación con JWT (PyJWT)

**Aplicación móvil**
- Android nativo (Java)
- Retrofit + Gson para el consumo de la API REST

**Base de datos**
- MySQL/SQLite

**Herramientas de desarrollo**
- Git y GitHub
- Android Studio
- PythonAnywhere (despliegue del backend)

## Estado de las funcionalidades

| Funcionalidad | Estado |
|---|---|
| Registro de nuevos usuarios | Implementada (conectada al backend) |
| Inicio de sesión con autenticación JWT | Implementada (conectada al backend) |
| Visualización del perfil del usuario | Implementada (conectada al backend) |
| Modificación de datos personales | Implementada (conectada al backend) |
| Consulta de información institucional ("Quiénes somos" / "Sobre nosotros") | Implementada |
| Catálogo de inmuebles con filtros (tipo, ciudad, precio máximo) | Implementada (conectada al backend) en proveso de refinamiento |
| Detalle de propiedad | En desarrollo |
| Favoritos | En revisión|
| Consulta de ubicación y dirección del inmueble | en desarrollo |
| Cierre de sesión | En desarrollo |
| Cambio de contraseña | en desarrollo |
| Formulario de contacto con la inmobiliaria | Implementada (conectada al backend) |
| Postulación a propiedades disponibles | Planificada |
| Seguimiento de postulaciones realizadas | Planificada |


## Requisitos no funcionales

- Interfaz intuitiva y fácil de utilizar.
- Adaptabilidad a diferentes tamaños de pantalla de dispositivos Android.
- Buen rendimiento en la carga y consulta de información.
- Comunicación con el backend mediante API REST y autenticación con JWT.
- Persistencia de la información en base de datos MySQL.
- Código organizado y mantenible.

**Pendientes de mejora (seguridad y calidad):**
- Almacenamiento seguro del token de sesión en el dispositivo.
- Manejo de errores de red en todas las pantallas.
- Configuración segura del backend (variables sensibles fuera del repositorio).
- Tests automatizados de la app.

## Flujo previsto de la aplicación

1. El usuario accede a la aplicación.
2. Se registra o inicia sesión. *(implementado)*
3. Una vez autenticado, accede a las funcionalidades disponibles. *(implementado)*
4. Consulta el catálogo de propiedades y usa filtros para encontrar inmuebles. *(implementado)*
5. Visualiza el detalle de una propiedad. *(en refinamiento)*
6. Realiza una postulación y hacer el seguimiento de sus postulaciones. *(planificado)*
7. Modifica sus datos personales. *(implementado)*
8. Cambia su contraseña. *(en desarrollo)*
9. Envía consultas mediante el formulario de contacto. *(implementado)*
10. Consulta la información institucional de la inmobiliaria. *(implementado)*

## Repositorios

- App Android: https://github.com/CreatiEdu/InmoSmartMobile
- Backend Django: https://github.com/CreatiEdu/InmoSmart

## Estado del proyecto

Proyecto académico en desarrollo, correspondiente a la continuidad del proyecto web INMOSMART. Se trabaja por sprints, incorporando progresivamente las funcionalidades definidas en el Product Backlog.

## Equipo de desarrollo

- Astrada Matías Eric
- Farias Michael David
- Fernandez Rubio Dolores
- Fernandez Rubio Franco
- Rodríguez Valenzuela Matías
- Gariglio Alejandro

