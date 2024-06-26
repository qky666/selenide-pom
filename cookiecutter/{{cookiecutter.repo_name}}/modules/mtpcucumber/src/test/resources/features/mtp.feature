# encoding: utf-8
# language: es

@retry
# Add 'screenshot' tag to attach a screenshot to every step in the report
# @screenshot
#noinspection NonAsciiCharacters,CucumberTableInspection
Característica: Web de MTP
  Validación de la Web de MTP.
  Vamos a usarlo a modo de ejemplo.

  Antecedentes:
    * Se accede a la web de MTP
    * Se aceptan las cookies
    * Se establece el idioma

  @desktop @mobile
  Esquema del escenario: Acceso a Aseguramiento de la calidad. Navegador: <Navegador>; Modelo: <Modelo>; Idioma: <Idioma>
    * Se navega a Servicios -> Aseguramiento de la calidad
    * Se carga la página Aseguramiento de la calidad
    Ejemplos:
      | Navegador | Modelo  | Idioma |
      | chrome    | desktop | es     |
      | chrome    | desktop | en     |
      | chrome    | mobile  | es     |
      | chrome    | mobile  | en     |
      | firefox   | desktop | es     |
      | firefox   | desktop | en     |

  @desktop @mobile
  Esquema del escenario: Error forzado. Navegador: <Navegador>; Modelo: <Modelo>; Idioma: <Idioma>
    * Se carga la página Aseguramiento de la calidad
    Ejemplos:
      | Navegador | Modelo  | Idioma |
      | chrome    | desktop | es     |
      | chrome    | desktop | en     |
      | chrome    | mobile  | es     |
      | chrome    | mobile  | en     |
      | firefox   | desktop | es     |
      | firefox   | desktop | en     |

  @desktop @mobile
  Esquema del escenario: El mensaje de aviso de las cookies no debe reaparecer. Navegador: <Navegador>; Modelo: <Modelo>; Idioma: <Idioma>
    * Se navega a Servicios -> Aseguramiento de la calidad
    * Se carga la página Aseguramiento de la calidad
    * El mensaje de aviso de las cookies no se muestra
    Ejemplos:
      | Navegador | Modelo  | Idioma |
      | chrome    | desktop | es     |
      | chrome    | desktop | en     |
      | chrome    | mobile  | es     |
      | chrome    | mobile  | en     |
      | firefox   | desktop | es     |
      | firefox   | desktop | en     |

  @desktop
  Esquema del escenario: Búsqueda en la web de MTP '<Búsqueda>'. Navegador: <Navegador>; Modelo: <Modelo>; Idioma: <Idioma>
    * Se busca el término '<Búsqueda>'
    * El número de páginas de resultados para la búsqueda '<Búsqueda>' es <PáginasDeResultados>
    * Se navega a la página <PáginasDeResultados> de <PáginasDeResultados> de resultados de la búsqueda
    * La página mostrada es la última del total de <PáginasDeResultados> páginas de resultados de la búsqueda
    * El número de resultados para la búsqueda mostrados es <ResultadosEnÚltimaPágina>
    * Se muestra un resultado para la búsqueda con título '<TítuloDeResultadoEnÚltimaPágina>' y texto '<TextoDeResultadoEnÚltimaPágina>'
    Ejemplos:
      | Navegador | Modelo  | Idioma | Búsqueda | PáginasDeResultados | ResultadosEnÚltimaPágina | TítuloDeResultadoEnÚltimaPágina                                       | TextoDeResultadoEnÚltimaPágina                                    |
      | chrome    | desktop | es     | Mexico   | 2                   | 1                        | El valor de los principios de diseño UX                               | Recientemente he tenido la oportunidad de trabajar en un proyecto |
      | chrome    | desktop | es     | Viajero  | 1                   | 1                        | Siete aplicaciones para disfrutar de unas vacaciones por todo lo alto | Ha llegado la hora de desconectar unos días                       |
      | chrome    | desktop | en     | Mexico   | 1                   | 5                        | Contact us                                                            |                                                                   |
      | firefox   | desktop | es     | Mexico   | 2                   | 1                        | El valor de los principios de diseño UX                               | Recientemente he tenido la oportunidad de trabajar en un proyecto |
      | firefox   | desktop | es     | Viajero  | 1                   | 1                        | Siete aplicaciones para disfrutar de unas vacaciones por todo lo alto | Ha llegado la hora de desconectar unos días                       |
      | firefox   | desktop | en     | Mexico   | 1                   | 5                        | Contact us                                                            |                                                                   |

  @wip @desktop
  Esquema del escenario: WIP. Acceso a Aseguramiento de la calidad. Navegador: <Navegador>; Modelo: <Modelo>; Idioma: <Idioma>
    * Se navega a Servicios -> Aseguramiento de la calidad
    * Se carga la página Aseguramiento de la calidad
    Ejemplos:
      | Navegador | Modelo  | Idioma |
      | chrome    | desktop | es     |
      | chrome    | desktop | en     |
      | firefox   | desktop | es     |
      | firefox   | desktop | en     |
