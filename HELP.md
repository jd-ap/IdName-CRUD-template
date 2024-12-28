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
# replaces literals with regex 
# > metadata_file=target/generated-sources/archetype/src/main/resources/META-INF/maven/archetype-metadata.xml
cp archetype-metadata.xml target/generated-sources/archetype/src/main/resources/META-INF/maven/archetype-metadata.xml
````

To install the archetype in local execute the following commands:

````shell
cd target/generated-sources/archetype
mvn clean install
````

Create a project from a local repository

````shell
mvn archetype:generate -DarchetypeCatalog=local -DarchetypeGroupId=crud.tech.proof -DarchetypeArtifactId=techProof-crud-template
````