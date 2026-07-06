# ps-project2-2025-26-tim_5


------- PORETANJE SA ENVIRONMENT VARIJABLAMA ----------------

build

docker build -t configservice:local .\ConfigService\
docker build -t authservice:local   .\AuthService\
docker build -t smestaj:local       .\smestaj\
docker build -t odrzavanje:local    .\odrzavanje\
docker build -t gateway:local       .\gatewayService\


 ---1. ConfigService (no DB vars needed here) --- 

docker run --rm --name configservice `
-p 8888:8888 `
configservice:local

--- 2. smestaj ---

docker run --rm --name smestaj `
-p 8081:8081 `
-e SPRING_CONFIG_IMPORT="optional:configserver:http://host.docker.internal:8888" `
-e DB_HOST=host.docker.internal `
-e DB_USERNAME=root `
-e DB_PASSWORD=root `
smestaj:local

--- 3. odrzavanje---

docker run --rm --name odrzavanje `
-p 8082:8082 `
-e SPRING_CONFIG_IMPORT="optional:configserver:http://host.docker.internal:8888" `
-e DB_HOST=host.docker.internal `
-e DB_USERNAME=root `
-e DB_PASSWORD=root `
odrzavanje:local

 --- 4. authservice ---

docker run --rm --name authservice `
-p 8085:8085 `
-e SPRING_CONFIG_IMPORT="optional:configserver:http://host.docker.internal:8888" `
-e DB_HOST=host.docker.internal `
-e DB_USERNAME=root `
-e DB_PASSWORD=root `
authservice:local

 --- 5. gateway ---

docker run --rm --name gateway `
-p 8083:8083 `
-e SPRING_CONFIG_IMPORT="optional:configserver:http://host.docker.internal:8888" `
gateway:local

# Stop all containers

docker stop configservice authservice smestaj odrzavanje gateway


# Docker compose file

  Build all images:
  docker compose build

  Start everything (one command):
  docker compose up

  Or build + start in one shot:
  docker compose up --build

  Stop everything:
  docker compose down

  Run in background (detached):
  docker compose up -d
"# ProgramiranjeSistema-Projekat2-Eco-Travel-sistem-nastavak" 
