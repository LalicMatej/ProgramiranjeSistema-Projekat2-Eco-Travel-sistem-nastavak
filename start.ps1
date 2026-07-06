Set-Location $PSScriptRoot

Write-Host "1. Ciscenje starog okruzenja..."
docker-compose down --remove-orphans
docker system prune -f

Write-Host "2. Maven build..."
Set-Location shared-events;  mvn clean install -DskipTests -q;   Set-Location ..
Set-Location ConfigService;  mvn clean package -DskipTests -q;   Set-Location ..
Set-Location discovery-server; mvn clean package -DskipTests -q;   Set-Location ..
Set-Location AuthService;    mvn clean package -DskipTests -q;   Set-Location ..
Set-Location gatewayService; mvn clean package -DskipTests -q;   Set-Location ..
Set-Location smestaj;        mvn clean package -DskipTests -q;   Set-Location ..
Set-Location odrzavanje;     mvn clean package -DskipTests -q;   Set-Location ..
Set-Location booking-service;  mvn clean package -DskipTests -q; Set-Location ..
Set-Location finance-service;  mvn clean package -DskipTests -q; Set-Location ..
Set-Location avanture-service; mvn clean package -DskipTests -q; Set-Location ..
Set-Location vodici-service;   mvn clean package -DskipTests -q; Set-Location ..

Write-Host "3. Pokretanje sistema..."
docker-compose up --build -d --scale avanture-service=3 --scale vodici-service=3

Write-Host "4. Verifikacija statusa..."
Start-Sleep -Seconds 30
docker-compose ps

Write-Host "5. Provera registry i observability infrastrukture..."
docker-compose ps discovery-server loki grafana zipkin
