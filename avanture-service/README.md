                            Avanture Service - Tehnička Dokumentacija
                              **Opis projekta i poslovna logika**  
#### Avanture service
je mikroservis za upravljanje katalogom turističkih i adrenalinskih  
aktivnosti. Servis omogućava potpunu administraciju avantura, njihovih kategorija, termina  
i neophodne opreme.
Ključne funkcionalnosti:
Upravljanje avanturama: Kreiranje i ažuriranje avantura sa definisanim nivoima težine (difficulty_level)
i osnovnim cenama.
Termini (TimeSlots): Evidencija dostupnih termina sa praćenjem maksimalnog kapaciteta i trenutne
popunjenosti.
Oprema (Gear Requirements): Definisanje obavezne i opcione opreme potrebne za svaku
pojedinačnu avanturu.
Recenzije: Sistem za ostavljanje ocena i komentara korisnika na realizovane
avanture.
Kategorizacija: Grupisanje avantura (npr. planinarenje, vodeni sportovi) radi lakše pretrage.
**Tehnologije i Port**
Tehnologije: Java 17, Spring Boot, Spring Data JPA, H2 Database,Lombok.
Mrežni port: Mikroservis operiše na portu 8084.
**API Endpoints**
1. Avanture (/api/avanture)
   Metoda-Endpoint-Opis
   POST-/adventures/add-Kreira avanturu.
   GET-/adventures/all-Lista svih avantura.
   GET-/adventures/{id}-Detalji specifične avanture.
   PUT-/adventures/update/{id}-Ažuriranje podataka o avanturi.
   GET-/adventures/totalprice-Vraća prosečnu cenu svih avantura u sistemu.
   DELETE-/adventures/delete/{id}-Uklanjanje avanture iz sistema.
2. Termini i Kapaciteti (/api/avanture)
   Metoda-Endpoint-Opis
   POST-/timeslots/add-Dodavanje novog vremenskog slota za avanturu.
   GET-/timeslots/all-Pregled svih termina i popunjenosti.
   GET-/timeslots/{id}-Detalji specifičnog termina
   PUT-/timeslots/update/{id}-Ažuriranje termina.
   DELETE-/timeslots/delete/{id}-Brisanje termina.
3. Kategorije Avantura (/api/avanture)
   Metoda-Endpoint-Opis
   POST-/adventurecategories/add-Kreiranje nove kategorije avanture.
   GET-/adventurecategories/all-Dobavljanje liste svih kategorija.
   GET-/adventurecategories/{id}-Dobavljanje određene kategorije putem ID-ja.
   PUT-/adventurecategories/update/{id}-Ažuriranje postojeće kategorije na osnovu ID-ja.
   DELETE-/adventurecategories/delete/{id}-Brisanje kategorije iz baze podataka.
4. Recenzije Avantura (/api/avanture)
   Metoda-Endpoint-Opis
   POST-/adventurereviews/add-Dodavanje nove recenzije za avanturu.
   GET-/adventurereviews/all-Dobavljanje svih recenzija iz sistema.
   GET-/adventurereviews/{id}-Pregled specifične recenzije putem ID-ja.
   PUT-/adventurereviews/update/{id}-Ažuriranje sadržaja recenzije ili ocene.
   DELETE-/adventurereviews/delete/{id}-Uklanjanje recenzije.
5. Zahtevi za Opremu (/api/avanture)
   Metoda-Endpoint-Opis
   POST-/gearrequirements/add-Kreiranje novog komada opreme.
   GET-/gearrequirements/all-Lista sve opreme koja se nalazi u sistemu.
   GET-/gearrequirements/{id}-Prikaz detalja opreme putem ID-ja.
   PUT-/gearrequirements/update/{id}-Ažuriranje podataka o opremi.
   DELETE-/gearrequirements/delete/{id}-Brisanje stavke opreme.
   **Instalacija i lokalno pokretanje**
   Preduslovi:
1. Java 17 ili novija.
2. Maven.
   Koraci:
1. Klonirajte projekat.
2. Pozicionirajte se u koren direktorijuma avanture-service.
3. Pokrenite aplikaciju komandom:Bashmvn spring-boot:run
4. Aplikacija je dostupna na adresi: http://localhost:8084.
   Testiranje i Baza:
1. H2 Konzola:http://localhost:8084/h2-console (JDBC URL: jdbc:h2:mem:avanturedb, Username: ml).
2. Swagger UI:Dokumentacija endpointa na http://localhost:8084/swagger-ui.html.
3. Podaci: Prilikom startovanja,izvršava se import.sql koji popunjava bazu početnim podacima.
