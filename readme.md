# Invoice Service
### Running the service
* All commands should be executed in the main directory of the maven project
* Compile and execute unit test
    *     mvn clean install -f pom.xml
* Run service using Maven
    *     mvn spring-boot:run 
* Run service with java -jar command
    *     java -jar target/invoice-service-0.0.1-SNAPSHOT.jar

### Invoice API
* Add Invoice -> POST http://localhost:8080/invoices
* View All invoices -> GET http://localhost:8080/invoices
* View Invoice -> GET http://localhost:8080/invoices/{invoiceId}

### Useful URLs

* http://localhost:8080/swagger-ui.html
* http://localhost:8080/h2 
* JDBC URL: jdbc:h2:mem:invoice_db (username: sa, password: empty)

