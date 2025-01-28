# encoding: utf-8
# language: es

#noinspection NonAsciiCharacters,CucumberTableInspection
@retry @screenshot
# Add 'screenshot' tag to attach a screenshot to every step in the report
# @screenshot
Característica: ApiDemos app
  Validación de la aplicación ApiDemos de Android.
  Vamos a usarlo a modo de ejemplo.

  Antecedentes:
    * Se abre la app ApiDemos

  Escenario: Arrastrar y soltar
    * Se selecciona Views -> Drag And Drop
    * Se verifica que se muestra el texto exacto '' en Drag And Drop
    * Se arrastra el Punto1 sobre el Punto2 en Drag & Drop
    * Se verifica que se muestra el texto 'Dot' en Drag And Drop
    * Se verifica que se muestra el texto 'DraggableDot' en Drag And Drop
    * Se cierra la app
