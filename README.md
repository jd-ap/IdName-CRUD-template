# IdName CRUD template

Project demo

[go to statement](./STATEMENT.md)

## Getting started

### Installation

1. Clone this repository

````shell
git clone url-repository.git
````

2. Build service into container

````shell
docker build -f containerfile -t proof/idname-crud-webapp .
docker run -d --rm -p 9000:9000 --name idname-crud-webapp proof/idname-crud-webapp --server.port=9000
docker system prune 
````

### Paths

* [swagger ui](http://localhost:9000/api/v1)
* [h2 console](http://localhost:9000/api/v1/h2-console)
    - **JDBC url**: jdbc:h2:mem:tech_proof_db
    - **User name**: guest
    - **Password**: changeIt

