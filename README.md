# Full-Stack-Hospital_VM

Este proyecto es el avance técnico formal para la asignatura de Desarrollo FullStack. Consiste en una arquitectura backend distribuida basada en microservicios diseñada para gestionar de manera integral las operaciones de un entorno hospitalario.

## 🚀 Características Técnicas

* **Arquitectura:** Microservicios independientes bajo el patrón CSR (*Controller-Service-Repository*).
* **Persistencia:** Cada microservicio gestiona su propia base de datos relacional (**MySQL**) mediante **Spring Data JPA** e **Hibernate**.
* **Interoperabilidad:** Comunicación entre servicios implementada con **OpenFeign**, garantizando transferencia coherente de datos.
* **Seguridad y Validación:** Validación robusta de datos de entrada mediante **Bean Validation (Jakarta/JSR 380)** y uso de **DTOs** para la transferencia de información segura.
* **Gestión de Errores:** Manejo centralizado de excepciones con `@ControllerAdvice` y registro de logs estructurados con **SLF4J** para trazabilidad completa.
* **Testing Automatizado:** Suite de pruebas unitarias aisladas implementada con **JUnit 5** y **Mockito**.
* **Poblado de Datos Inteligente:** Inicialización masiva automatizada integrada en el ciclo de arranque mediante **Datafaker**.

---

## 1. Configuración de la Base de Datos (Laragon / MySQL)

Antes de ejecutar el código en IntelliJ o VS Code, la base de datos debe estar lista:

1.  **Abrir Laragon:** Ejecuta Laragon y presiona el botón *"Start All"*. Asegúrate de que los servicios de MySQL y Apache estén activos (en verde).
2.  **Acceder a la Consola/HeidiSQL:** Haz clic en el botón *"Database"* de Laragon para abrir HeidiSQL.
3.  **Crear la Base de Datos:** Crea una nueva base de datos llamada `hospital_vm`.
4.  **Verificar Credenciales:** Asegúrate de que el usuario sea `root` y la contraseña esté vacía (configuración por defecto de Laragon).

---

## 2. Ejecución del Proyecto (Spring Boot)

Una vez que MySQL está corriendo, procedemos a levantar el microservicio.

### Configuración de Propiedades
Abre `src/main/resources/application.properties` y verifica la configuración base. 

*Nota: Si experimentas el error de red `Port 8080 was already in use`, puedes reconfigurar el puerto local al `8081`:*

```properties
spring.application.name=hospital-vm
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/hospital_vm?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.com.hospital_vm_cl.hospital_vm=DEBUG
```

Lanzar la Aplicación
Opción A (IDE): Busca la clase HospitalVmApplication.java, haz clic derecho y presiona el botón Run.

Opción B (Terminal): Ejecuta el comando ./mvnw spring-boot:run o mvn spring-boot:run.

3. Inicialización Automática de Datos (DataInitializer)
El proyecto cuenta con un componente CommandLineRunner acoplado a Datafaker (localizado en es-CL) que automatiza el poblado de la base de datos en Laragon en el primer arranque si las tablas se encuentran vacías:

Médicos Clínicos: Genera de forma automática 5 médicos de prueba con nombres realistas, RUTs estructurados y asignación aleatoria de especialidades (Traumatología, Neurocirugía, Cardiología, Pediatría, Medicina General, Urgencias).

Pacientes Masivos: Genera e inserta 500 pacientes de prueba con datos demográficos aleatorios y fechas de nacimiento mapeadas de forma nativa mediante LocalDate.

Integridad Referencial: Utilizando un algoritmo de distribución probabilística (Random), vincula dinámicamente cada uno de los 500 pacientes a uno de los 5 médicos creados, manteniendo la consistencia de las llaves foráneas (FK) en la base de datos MySQL.

4. Suite de Pruebas Unitarias (JUnit 5 & Mockito)
El entorno de testing cumple estrictamente con el estándar de empaquetado de Maven, aislando los entornos de desarrollo y pruebas dentro de la carpeta espejo src/test/java/.

La clase de pruebas PacienteServiceTest utiliza la extensión @ExtendWith(MockitoExtension.class) para simular comportamientos en la memoria RAM y validar la lógica del negocio sin alterar los registros reales de Laragon.

Casos de Prueba Verificados
guardarPacienteExitoso(): Comprueba que al procesar un PacienteDTO, el sistema rescate correctamente al médico asignado por ID a través de medicoRepository.findById(), efectúe la asociación relacional y guarde el registro sin fallas.

buscarPorIdNulo(): Garantiza que si se solicita una búsqueda con parámetros nulos, el sistema retorne limpiamente un Optional.empty() de manera controlada y sin interactuar con los repositorios (verifyNoInteractions).

Para ejecutar los test, haz clic derecho sobre el archivo en tu IDE y selecciona Run As -> JUnit Test.

5. Pruebas de Funcionamiento (Postman)
```
Creación de USUARIO   // ADMINISTRADOR-MEDICO
http://localhost:8080/api/auth/register
{
    "name": "Diego Administrador",
    "email": "diego@admin.hospitalduoc.cl",
    "password": "admin123"
}
(Asignación de ROLES)
ADMINITRADOR   email": "diego@admin.hospitalduoc.cl
MEDICOS        email": diego@hospitalduoc.cl
PACIENTE       email": diego@gmail.cl

--------------------------------------------
Obtención de TOKEN
http://localhost:8080/api/auth/login
{
    "email": "diego@admin.hospitalduoc.cl",
    "password": "admin123"
}
--------------------------------------------
CREACION DE MEDICOS (Registrar ID)   // ADMINISTRADOR-MEDICO
http://localhost:8080/api/medicos
{
    "nombre": "Dr. Camilo Riquelme",
    "rut": "11.222.333-4",
    "especialidad": "Traumatología"
}
--------------------------------------------
CREACION DE PACIENTE (Registrar ID)   // ADMINISTRADOR-MEDICO
http://localhost:8080/api/pacientes
{
    "run": "19876543-2",
    "nombres": "Esteban",
    "apellidos": "Araya",
    "fechaNacimiento": "1995-08-24",
    "correo": "esteban.araya@gmail.com",
    "especialidad": "Traumatología",
    "comentario": "Control post-operatorio.",
    "medicoId": ...
}
--------------------------------------------
MODIFICACION DE DATOS (UTILIZAR ID)   // ADMINISTRADOR-MEDICO
http://localhost:8080/api/pacientes/...
{
    "run": "19876543-2",
    "nombres": "Esteban Ignacio",
    "apellidos": "Araya Toledo",
    "fechaNacimiento": "1995-08-24",
    "correo": "esteban.araya@gmail.com",
    "especialidad": "Neurocirugía",
    "comentario": "El paciente fue reevaluado por el Dr. XX. Se programa cirugía para la próxima semana.",
    "medicoId": ...
}

--------------------------------------------
ENTORNO DE PRUEBA PARA POBLADOS
http://localhost:8080/api/auth/login
{
    "email": "EJEMPLO@hospitalduoc.cl",
    "password": "Medico2026!"   (No cambiar)
}
Listar Registros (GET)
URL: http://localhost:8081/api/pacientes

Método: GET
```
Validación de Errores
Si intentas enviar un JSON omitiendo campos obligatorios (como el formato del correo o nombres vacíos), el sistema interceptará la petición mediante el GlobalExceptionHandler y retornará un código de estado 400 Bad Request acompañado de un desglose con las reglas de Jakarta que han sido infringidas.

Gracias por ver hasta acá, espero este código sea de ayuda para los buscadores de sabiduría.
By D_Gamn
