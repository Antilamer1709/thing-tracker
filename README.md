# thing-tracker
Thing-tracker application

Live demo: https://thing-tracker.ga

# To run locally: 
1) Run Postgres https://github.com/Antilamer1709/thing-tracker-postgres
2) Run RabbitMQ https://github.com/Antilamer1709/thing-tracker-rabbitmq
3) Run Angular project

    3.1) Install npm libs for the front end --npm install
    
    3.2) Generate typescript classes --mvn compile (for the first time could be necessary to comment eirslett frontend-maven-plugin)
    
4) Run Spring Boot app


# To run from docker:
1) Run Postgres https://github.com/Antilamer1709/thing-tracker-postgres
2) Run RabbitMQ https://github.com/Antilamer1709/thing-tracker-rabbitmq
3) Create /usr/share/thing-tracker.properties file
4) Redefine datasource in properties file depending where you run DB
5) Execute: docker run -v /home/antilamer1709:/home/antilamer1709 -p 80:8080 antilamer/thing-tracker:ssl2