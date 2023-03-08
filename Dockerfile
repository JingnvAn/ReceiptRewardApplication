FROM openjdk:17
WORKDIR /api-server
COPY target/receipt.processor-0.0.1.jar receipt-server-0.0.1.jar
ENTRYPOINT ["java","-jar","receipt-server-0.0.1.jar"]