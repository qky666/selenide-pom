# selenide-pom

Page Object Model utilities for Selenide.

# Install

## Gradle

```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    testImplementation 'com.github.qky666:selenide-pom:0.27.1'
    // should add 'selenide' or 'selenide-appium' too
    // testImplementation("com.codeborne:selenide:7.1.0")
    // testImplementation("com.codeborne:selenide-appium:7.1.0")
}
```

## Maven

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.qky666</groupId>
    <artifactId>selenide-pom</artifactId>
    <version>0.27.1</version>
</dependency>

<!-- should add 'selenide' or 'selenide-appium' too
<dependency>
    <groupId>com.codeborne</groupId>
    <artifactId>selenide</artifactId>
    <version>7.1.0</version>
</dependency>

<dependency>
    <groupId>com.codeborne</groupId>
    <artifactId>selenide-appium</artifactId>
    <version>7.1.0</version>
</dependency>
-->
```

# Javadoc

https://jitpack.io/com/github/qky666/selenide-pom/latest/javadoc/

# Build status

[![Release](https://jitpack.io/v/qky666/selenide-pom.svg)]
(https://jitpack.io/#qky666/selenide-pom)

# cookiecutter template

Template for a Selenium Kotlin automation project using selenide-pom.

## Usage

See [cookiecutter documentation](https://cookiecutter.readthedocs.io/en/stable/index.html).

    cookiecutter gh:qky666/selenide-pom --directory cookiecutter

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

## BDD

Cucumber: https://cucumber.io/

## Selenium

Selenide: https://selenide.org/index.html

Selenide-POM: https://github.com/qky666/selenide-pom

# Test Matrix

selenide version used to test selenide-pom:

| selenide-pom version | selenide version |
|----------------------|------------------|
| 0.28.1               | 7.3.3            |


# Extra: Easy Windows setup (without Admin rights)

## Install scoop

See [scoop homepage](https://scoop.sh/) for details.

Open a PowerShell terminal and run:

    Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
    irm get.scoop.sh | iex

## Install packages

    scoop bucket add extras    
    scoop bucket add java
    scoop install oraclejdk-lts jetbrains-toolbox python cookiecutter allure chromedriver geckodriver

## Update packages

To update scoop:

    scoop update

To know if any package needs update:

    scoop status

To update all installed packages:

    scoop update *
