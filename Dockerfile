FROM maven:3.8.5-openjdk-17-slim

WORKDIR /picpay
COPY . .

ENV DATABASE_URL_TEST=jdbc:h2:mem:picpay
ENV DATABASE_USER_TEST=admin
ENV DATABASE_PASS_TEST=admin

RUN mvn clean install

CMD mvn spring-boot:run