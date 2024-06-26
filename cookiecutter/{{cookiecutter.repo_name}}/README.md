# {{ cookiecutter.name }}

A Selenium Kotlin automation project

## Requirements

### Kotlin

Documentation: https://kotlinlang.org/

Installed with IntelliJ IDEA

### Python

Documentation: https://www.python.org/

Required by allure-combine.

## Core technologies

### Test framework

TestNG: https://testng.org/doc/index.html

### Logs

Log4j: https://logging.apache.org/log4j/2.x/#

### Report

Allure: https://docs.qameta.io/allure-report/

allure-combine: https://github.com/MihanEntalpo/allure-single-html-file

## Selenium

Selenide: https://selenide.org/index.html

Selenide-POM: https://github.com/qky666/selenide-pom

# Extra: Easy Windows setup (without Admin rights)

## Install scoop

See [scoop homepage](https://scoop.sh/) for details.

Open a PowerShell terminal and run:

    Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
    Invoke-RestMethod -Uri https://get.scoop.sh | Invoke-Expression

## Install packages

    scoop bucket add extras    
    scoop bucket add java
    scoop install openjdk python idea cookiecutter allure chromedriver geckodriver

## Update packages

To update scoop:

    scoop update

To know if any package needs update:

    scoop status

To update all installed packages:

    scoop update -a

To clean scoop cache and old apps:

    scoop cleanup -k -a

To identify problems with scoop:

    scoop checkup
