plugins {
    val springBootVersion = "3.0.8"
    val kotlinVersion = "1.9.20"

    java
    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version "1.1.4"
    // kotlin jvm 플러그인
    kotlin("jvm") version kotlinVersion
    // kotlin spring 호환성(open class) 플러그인
    kotlin("plugin.spring") version kotlinVersion
    // Kotlin Annotation Processing Tool
    kotlin("kapt") version kotlinVersion
    // kotlin jpa 호환성(entity class에 매개변수 없는 기본 생성자 생성)
    kotlin("plugin.jpa") version kotlinVersion
    // kotlin에서 lombok 사용이 가능해지게 만들어주는 플러그인
    kotlin("plugin.lombok") version kotlinVersion
    id("io.freefair.lombok") version "8.1.0"
}

group = "com"
version = "0.0.2-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.session:spring-session-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

//    runtimeOnly 'com.h2database:h2'
    runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    //mockito
    testImplementation("org.mockito:mockito-inline:3.11.2")

    //swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    //QueryDSL
    val querydslVersion = "5.0.0"
    implementation("com.querydsl:querydsl-jpa:$querydslVersion:jakarta")
    kapt("com.querydsl:querydsl-apt:$querydslVersion:jakarta")

    // OpenFeign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.0.3")

    //jjwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    //s3
    implementation(platform("com.amazonaws:aws-java-sdk-bom:1.11.1000"))
    implementation("com.amazonaws:aws-java-sdk-s3")

    //Spring Aop
    implementation("org.springframework.boot:spring-boot-starter-aop")

    //actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    //prometheus
    implementation("io.micrometer:micrometer-registry-prometheus:1.11.2")
    //loki
    implementation("com.github.loki4j:loki-logback-appender:1.4.0")

    // For Property Binding
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // ehcache
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.ehcache:ehcache:3.8.1")

    // JSR-107 API를 사용하기 위함
    implementation("javax.cache:cache-api:1.1.1")

    //hibernate-spatial
    implementation("org.hibernate:hibernate-spatial:6.4.2.Final")

    //kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    //FCM admin SDK
    implementation("com.google.firebase:firebase-admin:9.2.0")

    //kotlin coroutine
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    testImplementation("io.projectreactor:reactor-test")
}

kapt {
    keepJavacAnnotationProcessors = true
}

tasks {
    test {
        useJUnitPlatform()
    }
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    jar {
        enabled = false
    }
}
