import org.jetbrains.kotlin.resolve.calls.checkers.shouldWarnAboutDeprecatedModFromBuiltIns

plugins {
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
	id("org.jetbrains.kotlin.plugin.noarg") version "2.0.21"
	id ("org.jetbrains.kotlin.plugin.jpa") version "2.0.21"
	id("org.openapi.generator") version "7.10.0"
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
}

group = "perso.api"
version = "0.0.1-SNAPSHOT"

var retrofitVersion = "2.11.0"
var converterGsonVersion = "2.11.0"
var loggingInterceptorVersion = "4.10.0"
var postgresqlVersion = "42.7.3"

var openStarterVersion = "2.5.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	val kotlinLogVersion = "3.0.5"

	// Open API
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$openStarterVersion")

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.4.2")

	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLogVersion")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
	implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.8.1")
	implementation ("com.google.code.gson:gson:2.10.1")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
	implementation("com.squareup.retrofit2:converter-gson:$converterGsonVersion")
	implementation ("com.squareup.okhttp3:logging-interceptor:$loggingInterceptorVersion")
	implementation("org.postgresql:postgresql:$postgresqlVersion")

	// Testing
	testImplementation("io.mockk:mockk:1.13.16")
	testImplementation("io.rest-assured:spring-mock-mvc:3.0.0")
	testImplementation("com.squareup.retrofit2:retrofit-mock:2.11.0")
	testImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")

}

tasks.withType<Test> {
	useJUnitPlatform()
}

