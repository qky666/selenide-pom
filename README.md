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
    testImplementation 'com.github.qky666:selenide-pom:0.18.5'
    // should add 'selenide' or 'selenide-appium' too
    // testImplementation("com.codeborne:selenide:6.15.0")
    // testImplementation("com.codeborne:selenide-appium:6.15.0")
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
    <version>0.18.5</version>
</dependency>

<!-- should add 'selenide' or 'selenide-appium' too
<dependency>
    <groupId>com.codeborne</groupId>
    <artifactId>selenide</artifactId>
    <version>6.15.0</version>
</dependency>

<dependency>
    <groupId>com.codeborne</groupId>
    <artifactId>selenide-appium</artifactId>
    <version>6.15.0</version>
</dependency>
-->
```

# Javadoc
https://jitpack.io/com/github/qky666/selenide-pom/latest/javadoc/

# Build status
[![Release](https://jitpack.io/v/qky666/selenide-pom.svg)]
(https://jitpack.io/#qky666/selenide-pom)
