FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# 安裝 Maven
RUN apk add --no-cache maven

# 複製 Maven wrapper 和 pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# 下載依賴（這層會被快取）
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# 複製源碼
COPY src src

# 暴露端口
EXPOSE 8080 5005

# 使用 spring-boot:run 啟動，支援 devtools hot-reload
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"]
