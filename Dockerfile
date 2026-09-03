# Dockerfile
# -------------------------
# 2단계: Spring 빌드
# -------------------------
FROM gradle:8-jdk21 AS build-gradle

WORKDIR /backend
COPY . .

# gradlew 파일의 실행 권한을 부여
RUN chmod +x gradlew
# gradlew로 스프링 프로젝트를 빌드 => 프로젝트.jar 생성 
RUN ./gradlew build -x test --no-daemon


# -------------------------
# 실행용 이미지
# -------------------------
FROM openjdk:21-ea-jdk-slim

WORKDIR /backend
COPY --from=build-gradle /backend/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Xmx512m","-jar", "app.jar"]