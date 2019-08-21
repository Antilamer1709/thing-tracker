# thing-tracker
Thing-tracker application

Live demo: https://thing-tracker.ga

# To run locally: 
1) Run Postgres https://github.com/Antilamer1709/thing-tracker-postgres
2) Run RabbitMQ https://github.com/Antilamer1709/thing-tracker-rabbitmq
3) Run Angular project

    3.1 Install npm libs for the front end --npm install
    
    3.2 Generate typescript classes --mvn compile (for the first time could be necessary to comment eirslett frontend-maven-plugin)
    
4) Run Spring Boot app


# To run from docker:
1) Run Postgres https://github.com/Antilamer1709/thing-tracker-postgres
2) Run RabbitMQ https://github.com/Antilamer1709/thing-tracker-rabbitmq
3) Create /usr/share/thing-tracker.properties file
4) Redefine datasource in properties file depending where you run DB
5) Create SSL cert

    5.1 Generate letsencrypt cert
    
    5.2 Create bundle: openssl pkcs12 -export -out /etc/letsencrypt/live/dev.soldi.io/bundle.pfx -inkey /etc/letsencrypt/live/dev.soldi.io/privkey.pem -in /etc/letsencrypt/live/dev.soldi.io/cert.pem -certfile /etc/letsencrypt/live/dev.soldi.io/chain.pem -password pass:apassword
    
    5.3 Copy it to  /etc/letsencrypt/live/thing-tracker.ga/bundle.pfx
    
6) Create logs folder /opt/logs/thing-tracker
7) Execute: docker run -v /etc/letsencrypt/live/thing-tracker.ga:/etc/letsencrypt/live/thing-tracker.ga -v /opt/logs/thing-tracker:/opt/logs/thing-tracker -e SPRING_DATASOURCE_URL=jdbc:postgresql://34.77.197.231:5432/thing_tracker -p 443:8443 antilamer/thing-tracker:ssl3