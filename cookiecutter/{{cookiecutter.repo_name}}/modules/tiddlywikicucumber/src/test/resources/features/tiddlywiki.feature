# encoding: utf-8
# language: es

@retry
# Add 'screenshot' tag to attach a screenshot to every step in the report
# @screenshot
#noinspection NonAsciiCharacters,CucumberTableInspection
Característica: TiddlyWiki en español
  Validación de la Web TiddlyWiki en español.
  Vamos a usarlo a modo de ejemplo.

  Antecedentes:
    * Se accede a la web de TiddlyWiki en español

  @desktop @mobile
  Esquema del escenario: Verificar botón 'Abiertos: Cerrar todo'. Navegador: <Navegador>; Modelo: <Modelo>; Idioma: <Idioma>
    * Se pulsa en 'Abiertos: Cerrar todo'
    Ejemplos:
      | Navegador | Modelo  | Idioma |
      | chrome    | desktop | spa    |
      | chrome    | desktop | eng    |
      | chrome    | mobile  | spa    |
      | chrome    | mobile  | eng    |
      | firefox   | desktop | spa    |
      | firefox   | desktop | eng    |

  @desktop @mobile
  Esquema del escenario: Verificar pestañas del panel lateral. Navegador: <Navegador>; Modelo: <Modelo>; Idioma: <Idioma>
    * Se crea un nuevo tiddler
    * Se verifica que el número de tiddlers abiertos en modo edición es 1 y en modo vista es 1
    * Se recorren las pestañas del panel lateral
    * Se verifica que la pestaña Recientes tiene 1 elemento
    Ejemplos:
      | Navegador | Modelo  | Idioma |
      | chrome    | desktop | spa    |
      | chrome    | desktop | eng    |
      | chrome    | mobile  | spa    |
      | chrome    | mobile  | eng    |
      | firefox   | desktop | spa    |
      | firefox   | desktop | eng    |

  @desktop @mobile
  Esquema del escenario: Crear y buscar nuevo tiddler. Navegador: <Navegador>; Modelo: <Modelo>; Idioma: <Idioma>
    * Se crea un nuevo tiddler
    * Se verifica que el número de tiddlers abiertos en modo edición es 1 y en modo vista es 1
    * Se informan los datos del primer tiddler abierto en modo edición
      | Título   | Contenido   |
      | <Título> | <Contenido> |
    * Se verifica que el número de tiddlers abiertos en modo edición es 0 y en modo vista es 2
    * Se verifican los datos del primer tiddler abierto en modo vista
      | Título   | Contenido   |
      | <Título> | <Contenido> |
    * Se pulsa en 'Abiertos: Cerrar todo'
    * Se realiza una búsqueda por el término '<Título>', que obtiene 2 resultados y el texto '<Resultado de búsqueda>'
    * Se pulsa sobre el primer resultado de la búsqueda
    * Se verifica que el número de tiddlers abiertos en modo edición es 0 y en modo vista es 1
    * Se verifican los datos del primer tiddler abierto en modo vista
      | Título   | Contenido   |
      | <Título> | <Contenido> |
    * Se cierra el popup de búsqueda
    Ejemplos:
      | Navegador | Modelo  | Idioma | Título                     | Contenido                     | Resultado de búsqueda |
      | chrome    | desktop | spa    | Título de mi nuevo tiddler | Contenido de mi nuevo tiddler | 1 coincidencias       |
      | chrome    | desktop | eng    | My new tiddler title       | My new tiddler body           | 1 matches             |
      | chrome    | mobile  | spa    | Título de mi nuevo tiddler | Contenido de mi nuevo tiddler | 1 coincidencias       |
      | chrome    | mobile  | eng    | My new tiddler title       | My new tiddler body           | 1 matches             |
      | firefox   | desktop | spa    | Título de mi nuevo tiddler | Contenido de mi nuevo tiddler | 1 coincidencias       |
      | firefox   | desktop | eng    | My new tiddler title       | My new tiddler body           | 1 matches             |

  @desktop @mobile
  Esquema del escenario: Error forzado: Existe un tiddler abierto no esperado. Navegador: <Navegador>; Modelo: <Modelo>; Idioma: <Idioma>
    * Se verifica que el número de tiddlers abiertos en modo vista es 0
    Ejemplos:
      | Navegador | Modelo  | Idioma |
      | chrome    | desktop | spa    |
      | chrome    | desktop | eng    |
      | chrome    | mobile  | spa    |
      | chrome    | mobile  | eng    |
      | firefox   | desktop | spa    |
      | firefox   | desktop | eng    |

  @wip @desktop
  Esquema del escenario: WIP. Verificar botón Abiertos: Cerrar todo. Navegador: <Navegador>; Modelo: <Modelo>; Idioma: <Idioma>
    * Se pulsa en 'Abiertos: Cerrar todo'
    Ejemplos:
      | Navegador | Modelo  | Idioma |
      | chrome    | desktop | spa    |
      | chrome    | desktop | eng    |
      | firefox   | desktop | spa    |
      | firefox   | desktop | eng    |
