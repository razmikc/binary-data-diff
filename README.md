# Data Comparision

* Provide 2 http endpoints that accepts JSON base64 encoded binary data on both
endpoints
   <host>/v1/diff/<ID>/left and <host>/v1/diff/<ID>/right
* The provided data needs to be diff-ed and the results shall be available on a third end
point
  <host>/v1/diff/<ID>
*  The results shall provide the following info in JSON format
  1. If equal return that
  2. If not of equal size just return that
  3. If of same size provide insight in where the diffs are, actual diffs are not needed.
      So mainly offsets + length in the data
  
### Used technologies
* Java 11
* Spring Boot
* Spring MVC
* Spring Data JPA
* H2 database
* Swagger for Api Documentation
* Maven

### Requirements 

At least java 11 and Maven 3.5 are needed!

### Installation 

```sh
$ cd binary-data-diff
$ mvn clean package
$ java -jar target/data-diff-1.0.jar 
```

### Running tests

```sh
$ cd binary-data-diff
$ mvn test
```

### Api Documentation UI

[Swagger UI](http://localhost:8080/swagger-ui.html#)
