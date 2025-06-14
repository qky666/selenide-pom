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
    testImplementation 'com.github.qky666:selenide-pom:<selenide-pom version>'
    // should add 'selenide' or 'selenide-appium' too
    // testImplementation("com.codeborne:selenide:<selenide version>")
    // testImplementation("com.codeborne:selenide-appium:<selenide version>")
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
    <version>[selenide-pom version]</version>
</dependency>

<!-- should add 'selenide' or 'selenide-appium' too
<dependency>
    <groupId>com.codeborne</groupId>
    <artifactId>selenide</artifactId>
    <version>[selenide version]</version>
</dependency>

<dependency>
    <groupId>com.codeborne</groupId>
    <artifactId>selenide-appium</artifactId>
    <version>[selenide version]</version>
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

Versions used to test selenide-pom:

| selenide-pom version | selenide version | Gradle version | Java version |
|----------------------|------------------|----------------|--------------|
| 0.28.1               | 7.3.3            | 8.8            | 21           |
| 0.28.2               | 7.3.3            | 8.8            | 21           |
| 0.28.3               | 7.5.1            | 8.10.2         | 21           |
| 0.28.4               | 7.5.1            | 8.10.2         | 21           |
| 0.29.0               | 7.5.1            | 8.10.2         | 21           |
| 0.30.0               | 7.7.3            | 8.13           | 21           |

# Extra: Easy Windows setup (without Admin rights)

## Install scoop

See [scoop homepage](https://scoop.sh/) for details.

To install scoop, open a PowerShell terminal and run:

    Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
    Invoke-RestMethod -Uri https://get.scoop.sh | Invoke-Expression

## Post install

Some basic scoop configuration and packages:

    scoop install git sudo
    C:\Users\<user>\scoop\apps\7zip\current\install-context.reg
    git config --global credential.helper manager
    C:\Users\<user>\scoop\apps\git\current\install-context.reg
    C:\Users\<user>\scoop\apps\git\current\install-file-associations.reg
    scoop update
    # The next 3 commands may need Admin rights:
    sudo Set-ItemProperty 'HKLM:\SYSTEM\CurrentControlSet\Control\FileSystem' -Name 'LongPathsEnabled' -Value 1
    scoop install innounp
    scoop install dark
    # It is also recommended to activate developer mode in Windows configuration.

## Install packages

Some basic packages for Selenium/Selenide (and OCR):

    scoop bucket add extras
    scoop bucket add java
    scoop install openjdk python cookiecutter allure idea tesseract tesseract-languages

## Check scoop

To check potential problems in `scoop`:

    scoop checkup

After executing this command, scoop will suggest you actions to fix problems found.

## Update packages

To update `scoop`:

    scoop update

To know if any package needs update:

    scoop status

To update all installed packages:

    scoop update -a

To clean cache and old apps:

    scoop cleanup -k -a
