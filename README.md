# RentEasy - Sistema de Gestión de Propiedades en Alquiler

## Descripción
RentEasy es una aplicación de escritorio desarrollada en Java con Swing para la gestión de propiedades en alquiler. La plataforma permite a los propietarios registrar y administrar sus propiedades, mientras que los arrendatarios pueden buscar propiedades disponibles y realizar reservas.

## Características Principales
- Registro y administración de propiedades.
- Edición y eliminación de propiedades.
- Búsqueda de propiedades con filtros de precio, ciudad y tipo.
- Visualización de la información del propietario.
- Gestión de perfiles para propietarios y arrendatarios.
- Funcionalidad de reserva de propiedades.

## Video Explicativo
Ejemplo de uso: https://youtu.be/kqynjbb4QQk?si=zgSvJmrKRm3_uiXU


[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/YOUTUBE_VIDEO_ID_HERE/0.jpg)](https://www.youtube.com/watch?v=kqynjbb4QQk)

## Instalación y Ejecución
### Requisitos
- Java 11 o superior
- IDE compatible con Java (IntelliJ IDEA, Eclipse, NetBeans, etc.)

### Instalación
1. Clonar el repositorio:
   ```sh
   git clone https://github.com/jmvillanueva-dev/RentEasy-App.git
   ```
2. Importar el proyecto en un IDE compatible con Java.
3. Configurar la base de datos (ver sección siguiente).
4. Ejecutar la aplicación desde `Main.java`.

## Base de Datos
### Creación de la Tabla `properties`
```sql
CREATE TABLE IF NOT EXISTS user_roles (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_name VARCHAR(50) INTEGER NOT NULL,
  last_name VARCHAR(50) INTEGER NOT NULL,
  email VARCHAR(100) NOT NULL,
  password VARCHAR(200) NOT NULL,
  accepted_privacy_policy INTEGER DEFAULT 0,
  accepted_terms_and_conditions INTEGER DEFAULT 0,
  created_at TEXT DEFAULT CURRENT_TIMESTAMP,
  updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
);

-- Crear la tabla user_roles con la clave foránea a user(id)
CREATE TABLE IF NOT EXISTS user_roles (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  userId INTEGER NOT NULL,
  role_assignment VARCHAR(100) NOT NULL,
  FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE
);

-- Crear la tabla owners con la clave foránea user_id referenciando users(id)
CREATE TABLE IF NOT EXISTS owners (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER NOT NULL,
  dni_number VARCHAR(50) UNIQUE,
  birth_date VARCHAR(50),
  address TEXT,
  phone_number VARCHAR(50),
  updated_at VARCHAR(100) DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Crear la tabla tenants con la clave foránea user_id referenciando users(id)
CREATE TABLE IF NOT EXISTS tenants (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    dni_number VARCHAR(50) UNIQUE,
    city VARCHAR(50) NOT NULL,
    region VARCHAR(50) NOT NULL,
    address VARCHAR(50) NOT NULL,
    card_number VARCHAR(50) NOT NULL,
    card_deadline VARCHAR(50) NOT NULL,
    cvc_number VARCHAR(50) NOT NULL,
    updated_at VARCHAR(100) DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Crear la tabla properties para almacenar información de las propiedades
CREATE TABLE IF NOT EXISTS properties (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    owner_id INTEGER NOT NULL,
    title TEXT NOT NULL,
    city TEXT NOT NULL,
    country TEXT NOT NULL,
    price REAL NOT NULL,
    property_type TEXT NOT NULL,
    rooms INTEGER NOT NULL,
    max_people INTEGER NOT NULL,
    address TEXT NOT NULL,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES owners(id) ON DELETE CASCADE
);

-- Crear la tabla reservations para almacenar las reservas de propiedades
CREATE TABLE IF NOT EXISTS reservations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tenant_id INTEGER NOT NULL,
    owner_id INTEGER NOT NULL,
    property_id INTEGER NOT NULL,
    start_date TEXT NOT NULL,
    end_date TEXT NOT NULL,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id) ON DELETE CASCADE,
    FOREIGN KEY (owner_id) REFERENCES owners(id) ON DELETE CASCADE,
    FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE
);

```

## Dependencias
La aplicación utiliza las siguientes bibliotecas y dependencias:
- **Swing** - Para la interfaz gráfica.
- **SQL Connector** - Para la conexión con la base de datos SQL.
- **JOptionPane** - Para mostrar mensajes emergentes.
- **JTable** - Para la visualización de propiedades en tablas.
- **jbcrypt** - Para encriptación de contraseñas.

## Capturas de Pantalla

**Venta de Bienvenida**
![image](https://github.com/user-attachments/assets/dc7124af-9057-4050-8007-4eeaea6ece7b)


**Ventana de Registro de Usuarios**
![image](https://github.com/user-attachments/assets/fe8374d9-7306-4d09-8f1a-227ad182dce2)
![image](https://github.com/user-attachments/assets/504d47a4-3e9b-45d7-a883-8e1136344211)


**Ventana de Inicio de Sesion**
![image](https://github.com/user-attachments/assets/c7a21897-b6db-482b-93fa-34426b2d0c4a)
![image](https://github.com/user-attachments/assets/2a29b25f-e7cd-4930-a075-59431534abcd)


**Venta de Administración de Propiedades**
![image](https://github.com/user-attachments/assets/885f5ab9-f459-4440-baeb-74d1a88fcf8c)


**Registrar de una propiedad**
![image](https://github.com/user-attachments/assets/539a82b8-0daf-4017-acec-f278efa988f7)


**Opción de edición**
![image](https://github.com/user-attachments/assets/0a32873f-8f2d-410d-a8ef-f01d17a25fa1)
![image](https://github.com/user-attachments/assets/a69041e2-13f9-4e5f-8054-80bb004c2a3d)
![image](https://github.com/user-attachments/assets/77a689cc-6f65-4a92-86b5-57d79c37ba35)


**Panel de Cliente/Arrendatario**
![image](https://github.com/user-attachments/assets/ef1ca06e-7f04-4b4a-8a08-63113ebc7210)
![image](https://github.com/user-attachments/assets/15e04850-7393-43fa-822a-237c80820b09)

**Opción de búsqueda con filtros**
![image](https://github.com/user-attachments/assets/f138755c-f34a-4c75-a86c-0258607976b1)

![image](https://github.com/user-attachments/assets/d3d316b4-ca23-4c4b-bf7c-693e8b5375b3)

![image](https://github.com/user-attachments/assets/353d95d6-312e-4d55-97e5-7fc6437cf210)

![image](https://github.com/user-attachments/assets/172f7908-9908-4aa9-a46f-f1bf6f558b20)

**Reserva de Propiedades**

![image](https://github.com/user-attachments/assets/0e4b2bd7-3626-4d05-8217-efa34eed0a0c)

![image](https://github.com/user-attachments/assets/b99897cf-8160-4a83-992e-a9194d56e501)

![image](https://github.com/user-attachments/assets/45dfc92e-b9ba-4830-9f35-f4f0016218ad)

![image](https://github.com/user-attachments/assets/0f25e67a-f9d0-4fb6-af5f-e20a1791757d)

![image](https://github.com/user-attachments/assets/f7ba804e-c328-4ec1-a19c-0fa8c0a4dacd)



## Contribuciones
Las contribuciones son bienvenidas. Si deseas mejorar la aplicación, por favor realiza un fork del repositorio y envía un pull request con tus cambios.

## Licencia
Este proyecto está bajo la Licencia MIT. Para más información, consulta el archivo `LICENSE`.

## Contacto
Para cualquier consulta o sugerencia, por favor contacta a: [@jmvillanueva-dev](mailto:jhonny.villanueva@epn.edu.ec).

