FROM maven:3.8-openjdk-17 AS builder

WORKDIR /app

ARG GITHUB_USERNAME
ARG GITHUB_TOKEN

# 复制 Maven settings.xml 并替换环境变量
COPY docker/maven/settings.xml /root/.m2/settings.xml

# 替换 settings.xml 中的环境变量占位符
RUN sed -i "s|\${env.GITHUB_USERNAME}|$GITHUB_USERNAME|g" /root/.m2/settings.xml && \
    sed -i "s|\${env.GITHUB_TOKEN}|$GITHUB_TOKEN|g" /root/.m2/settings.xml

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk
WORKDIR /app

# 创建日志目录并设置权限
RUN mkdir -p /logs && \
    chmod 755 /logs && \
    chown -R 1000:1000 /logs

COPY --from=builder /app/web/target/*.jar app.jar
RUN mkdir -p /logs /app/upload
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]