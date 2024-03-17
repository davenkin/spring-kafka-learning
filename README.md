 # Introduction
- This is a demo project use setting up both pure kafka client and Spring Kafka.
- All uses cases are stored in `test/java` folder.

# Tech stack
- Java 17
- Springboot 3
- Junit 5
- Spring Kafka

# How to use this project
1. Make sure you have Java 17+ installed
2. Run `./idea.sh` to open the project in IntelliJ IDEA
3. Follow `Local Kafka setup` to set up local Kafka environment
4. Run test cases under `test/java` directly in IDE

# Local Kafka setup
Before running any test case, a local Kafka environment must be set up. Currently, we are using Kafka with Zookeeper.
- To start `confluentinc/kafka`, go to `docker` folder and run `docker compose -f docker-compose-confluentinc.yml up`.
- To shut down `confluentinc/kafka`, go to `docker` folder and run `docker compose -f docker-compose-confluentinc.yml down`
- To start `bitnami/kafka`, go to `docker` folder and run `docker compose -f docker-compose-bitnami.yml up`.
- To shut down `bitnami/kafka`, go to `docker` folder and run `docker compose -f docker-compose-bitnami.yml down`.


# Other commands
3. Run `./run.sh` to start the application with remote debug port 5005, then you can  visit `http://localhost:8080/about`
4. Run `./build.sh` to build the project
