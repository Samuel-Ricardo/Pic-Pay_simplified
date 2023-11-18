FROM maven:3.8.5-openjdk-17-slim

WORKDIR /picpay
COPY . .

RUN mvn clean install

CMD mvn spring-boot:run