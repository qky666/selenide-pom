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

## FlaUI.WebDriver (Windows automation only)

Download FlaUI.WebDriver from https://github.com/FlaUI/FlaUI.WebDriver/releases.

In order to run `calculatorwincucumber` module tests, you need to run `FlaUI.WebDriver.exe` on a terminal.

Note: `FlaUI.WebDriver.exe` needs `dotnet-sdk` to run (`scoop install dotnet-sdk`).

## Selenium Grid with Docker

### Docker Desktop (Windows host)

To run a Selenium Grid using Docker, install Docker Desktop.
An easy way to install Docker Desktop:

    winget install --id Docker.DockerDesktop

See comments in `docker/docker-compose.yml` in order to set up the application to work with dynamic grids.
To start the grid, run (from `docker` folder):

    docker-compose up -d
    docker-compose down

If you want to start the grid with Android (appium) support, try:

    docker-compose -f docker-compose.yml -f docker-compose.android.yml up -d
    docker-compose -f docker-compose.yml -f docker-compose.android.yml down

If you want to start the grid with Windows support, try:

    docker-compose -f docker-compose.yml -f docker-compose.windows.yml up -d
    docker-compose -f docker-compose.yml -f docker-compose.windows.yml down

Note that you will need to install all required software in the Windows container.
See [FlaUI.WebDriver (Windows automation only)](#flauiwebdriver-windows-automation-only).

If you want both:

    docker-compose -f docker-compose.yml -f docker-compose.android.yml -f docker-compose.windows.yml up -d
    docker-compose -f docker-compose.yml -f docker-compose.android.yml -f docker-compose.windows.yml down

## Known bugs

- FlaUI.Webdriver not responding when used inside docker container.
- Appium `images` plugin is not working with `geckodriver` (appium version 2.12.0).
