# Full-Stack-Hospital_VM
Este proyecto es el avance técnico formal para la asignatura de Desarrollo FullStack 1. Consiste en una arquitectura backend distribuida basada en microservicios diseñada para gestionar de manera integral las operaciones de un entorno hospitalario.

Características TécnicasArquitectura: Microservicios independientes bajo el patrón CSR (Controller-Service-Repository).
Persistencia: Cada microservicio gestiona su propia base de datos relacional (MySQL) mediante Spring Data JPA e Hibernate.
Interoperabilidad: Comunicación entre servicios implementada con OpenFeign, garantizando transferencia coherente de datos.
Seguridad y Validación: Validación robusta de datos de entrada mediante Bean Validation (JSR 380) y uso de DTOs para la transferencia de información segura.
Gestión de Errores: Manejo centralizado de excepciones con @ControllerAdvice y registro de logs estructurados con SLF4J para trazabilidad completa.

1. Configuración de la Base de Datos (Laragon / MySQL)
Antes de ejecutar el código en IntelliJ o VS Code, la base de datos debe estar lista.
Abrir Laragon: Ejecuta Laragon y presiona el botón "Start All". Asegúrate de que los servicios de MySQL y Apache (opcional) estén en verde.
Acceder a la Consola/HeidiSQL: * Haz clic en el botón "Database" de Laragon para abrir HeidiSQL.
Crea una nueva base de datos llamada hospital_vm.
Verificar Credenciales: Asegúrate de que el usuario sea root y la contraseña esté vacía (configuración por defecto de Laragon).

2. Ejecución del Proyecto (Spring Boot)
Una vez que MySQL está corriendo, procedemos a levantar el microservicio.
Configuración de Propiedades: Abre src/main/resources/application.properties y verifica estas líneas:
Properties
spring.application.name=hospital-vm
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_vm?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
logging.level.com.hospital_vm_cl.hospital_vm=DEBUG

Lanzar la Aplicación: * Opción A (IDE): Busca la clase HospitalVmApplication.java y presiona el botón Run.
Opción B (Terminal): Ejecuta el comando ./mvnw spring-boot:run.
Confirmación: Revisa la consola. Debes ver el mensaje: Started HospitalVmApplication in X seconds.

3. Pruebas de Funcionamiento (Postman)
Crear una Petición POST:
URL: http://localhost:8080/api/pacientes
Método: POST
Headers: Content-Type: application/json
Body (JSON Raw):

JSON(Ejemplo)
{
  "run": "12345678-9",
  "nombres": "Juan",
  "apellidos": "Pérez",
  "fechaNacimiento": "1990-05-15",
  "correo": "juan.perez@email.com",
  "medicoId": 1
}
Ejecutar GET: Cambia el método a GET y usa la URL http://localhost:8080/api/pacientes para listar los registros.

Validar Errores: Intenta enviar un JSON sin el correo. Postman debería devolver un error 400 Bad Request gracias a nuestro GlobalExceptionHandler.

Gracias por ver hasta aca espero este codigo sea de ayuda para los buscadores de sabiduria.
By D_Gamn
