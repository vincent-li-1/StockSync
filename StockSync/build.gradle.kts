plugins {
	java
	id("org.springframework.boot") version "3.1.9"
	id("io.spring.dependency-management") version "1.1.4"
	application
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass = "stocksync.StockSyncSecurityApplication"
}

repositories {
	mavenCentral()
}

dependencies {
	runtimeOnly("com.mysql:mysql-connector-j")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:3.0.3")
	testImplementation("org.mockito:mockito-core:3.+")
	testImplementation ("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.8.1")
	testImplementation ("org.springframework.boot:spring-boot-starter-test")
	implementation ("org.springframework.boot:spring-boot-starter-security")

	implementation ("org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.1.RELEASE")
	testImplementation ("org.springframework.security:spring-security-test")


}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootBuildImage {
	builder.set("paketobuildpacks/builder-jammy-base:latest")
}
