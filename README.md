# stock-project
Challenge App to demonstrates a stocks manager using Spring Boot, JPA, Thymeleaf and H2 in-memory database.

###Installation
This app requires Maven to build, run and deploy.

Clone this app, install the dependencies and start the server.
```sh
$ git clone https://github.com/farshadfalaki/stock-project.git
$ cd stocks-manager
$ mvn spring-boot:run
```
Open in your browser (http://localhost:8080/dashboard/stocks) to see all the stocks.
While application starts load data from import.sql file to database including five test records.

###Stocks API
If you want to access the API resources, use the mapping listed below.

Retrieve all stocks
URL: http://localhost:8080/api/stocks
Method: GET

Get a stock by id
URL: http://localhost:8080/api/stocks/{id}
Method: GET

Create Stock
URL: http://localhost:8080/api/stocks
Method: POST
Body:``` { "name":"TestCompany", "currentPrice": 23.4 }```

Update Stock Price by Id
URL: http://localhost:8080/api/stocks/{id}
Method: PUT
Body: ```{ "currentPrice": 12.3 }```

Delete Stock by Id
URL: http://localhost:8080/api/stocks/{id}
Method: DELETE
