plugins {
    val springBootVersion = "3.0.8"
    java
    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version "1.1.4"

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
    implementation ("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor ("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor ("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor ("jakarta.persistence:jakarta.persistence-api")

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

}

tasks {

    test {
        useJUnitPlatform()
    }

}
