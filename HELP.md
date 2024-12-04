# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.5/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.5/maven-plugin/build-image.html)

## how using it

### Generate archetype from a project template

The content inside `archetype.properties` replace literals by variables and assign the values as default.
In the project root directory, run:

````shell
mvn clean archetype:create-from-project -Darchetype.properties=archetype.properties
````

To install the archetype in local execute the following commands:

````shell
cd web-service-archetype/target/generated-sources/archetype
mvn clean install
````
