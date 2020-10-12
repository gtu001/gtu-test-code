---
#jpa
spring:
  jpa:
    show-sql: true
    generate-ddl: true
    hibernate:
      ddl-auto: create 
  h2.console.enabled: true
  datasource:
    platform: h2
    url: jdbc:h2:file:/D:\workstuff\gtu-test-code\_SpringBootTestProject\h2\testDB;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE;AUTO_SERVER=TRUE
    username: sa
    password:
    driverClassName: org.h2.Driver
    testWhileIdle: true
    validationQuery: SELECT 1

