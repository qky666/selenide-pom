# encoding: utf-8
# language: es

#noinspection NonAsciiCharacters,CucumberTableInspection
@retry @screenshot
# Add 'screenshot' tag to attach a screenshot to every step in the report
# @screenshot
Característica: Windows Calculator
  Validación de la aplicación Calculator de Windows.
  Vamos a usarlo a modo de ejemplo.

  Antecedentes:
    * Se abre la aplicación Calculator

  Esquema del escenario: Suma en Calculadora Estándar
    * Se selecciona Calculadora Estándar
    * Se escribe el número <Numero1> en la Calculadora Estándar
    * Se pulsa en el botón + de la Calculadora Estándar
    * Se escribe el número <Numero2> en la Calculadora Estándar
    * Se pulsa en el botón = de la Calculadora Estándar
    * Se verifica que el resultado es '<Suma>' en la Calculadora Estándar
    * Se verifica que la expresión es '<Numero1> + <Numero2>=' en la Calculadora Estándar
    * Se cierra la ventana
    Ejemplos:
      | Numero1 | Numero2 | Suma    |
      | 18      | 57      | 75      |
      | 1254    | 368974  | 370.228 |
      | -27     | 35      | 8       |
