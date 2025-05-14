# Cocina con Catarina
App Android de recetas de cocina en las que el usuario puede guardar sus favoritas para una más rápida consulta.
Al tiempo que preparas una receta puedes escuchar música directamente desde la app.

## Funcionalidades principales:
  - Autenticación de Usuarios para registrarse o acceder mediante correo electrónico y contraseña
  - Listado y filtrado de recetas por categoría (dulce o salada)
  - Radio integrada (con rockFM en streaming)
  - Navegación mediante varios menús
  - Soporte multilenguaje
    
## Estructura:
La app tiene una arquitectura modular que se divide en 4 carpetas:
- db: Contiene todos los datos referentes/necesarios para la BBDD local, mediante `Room`:
    - DAOs: Interfaces para interactuar con la BBDD.
    - AppDataBase: Clase principal para trabajar con la BBDD. Contiene `anotations`, un listado de `entities` que conforman la BBDD local, y los métodos de acceso a los DAos
    - FavoritosItemListener: Interfaz para manejas los eventos de click en los items que muestra el RecyclerView `Favoritos`
    - RecetasDataSource: Clase que lee el archivo recetas.json mediante la librería Gson y convierte su contenido en objetos de tipo `Receta`
- model: Lógica de negocio
    - POJOs (Ingrediente, Paso, Receta y usuario): Representan las estructuras de los diferentes datos a utilizar en la app
    - Repositorios (Usuario y Receta): Abstracciones del acceso a los datos para `Usuario` y `Recetas`
- repository.room: (Debería estar dentro del paquete db)
    - Entitys: Entidades de la BBDD. Definen sus tablas por medio de clases con anotaciones
- ui: Interfaz de usuario.
    - Adapters: Para los RecyclerView (`Favoritos` y `Receta`). Gestionan como se mostrarán un listado de items de forma eficiente.
    - Fragmentos (`Favoritos`, `Home`, `ItemReceta`, `ListadoRecetas`, `LoginUsuario` y `RegistroUsuario`): Pantallas de la app.
    - MainActivity: Actividad principal (y única, single activity) que contendrá todos los fragmentos
    
## Contiene:
 1. Arquitectura SingleActivity:
    Al estar construída toda la app desde una única Activity que gestiona la navegación entre los diferentes fragmetns.
    Gestionará el ciclo de vida de toda la app
 2. Navigation Component: (`nav_graph`)
    Gestiona la navegación entre fragmentos mediante en archivo `nav_graph.xml`.
    Y también gestionará el paso de argumentos entre fragments por medio de safe args
 4. Persistencia mediante Room:
    Para persistencia de los datos he usado Room Persistence Library, que permite gestionar de forma local con SQL los datos de mi app.
 6. Doc JSON para repositorio de recetas: (`recetas.json`)
    Guardado en la carpeta assets, donde se recopilan todos los datos a mostrar sobre las mismas, de forma estructurada
 7. Multilenguaje:
    Archivo `strings.xml` en tres idiomas: castellano, gallego e inglés
 9. Hoja de colores y estilos personalizado:
    `colors.xml`, `styles.xml` y `themes.xml` para la configuración de colores de toda la app.
 11. Reproducción de música en streaming con ExoPlayer:
     Que permite que se pueda escuchar la emisora RockFM desde la app.
 13. ViewBinding:
     Para inflar las views
 15. Persistencia de datos con SharedPreferences:
     Almacena pequeños datos como el correo de usuario en el loging, o si se ha iniciado la sesión, para mejorar su experiencia
 16. Recursos de menu ( res> menu ):
     Para facilitar la navegación entre fragmentos se ha implementado un navigation drawer y un bottom navigation menu


##
### Opciones a implementar en un futuro (mejoras)
- Mover las clases Entity a la carpeta db!!
- Viewmodel
- Dark mode
- Barra de búsqueda
- Fragmento ModificarReceta, para que el usuario pueda modificar la cantidad de cada ingrediente en sus recetas
- Fragmento Configuración, para incluír el cambio de modo, idioma, emisora de radio, nombre de usuario
- Imágenes de cada receta en ItemReceta ( o en toda la app)
- Utilizar Compose en vez de views XML
- Incluír un temporizador
- Incluír una opción de envío de recetas por whatssapp o correo
- Modificar la radio para conectarse a Spotify
