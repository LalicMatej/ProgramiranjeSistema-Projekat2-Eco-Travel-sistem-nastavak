Vodici Service - Tehnička Dokumentacija
**Opis projekta i poslovna logika**

#### Vodici Service
je mikroservis zadužen za upravljanje podacima o turističkim/planinarskim vodičima, njihovim
jezicima i sertifikatima koje poseduju.
Ključne funkcionalnosti:
Upravljanje vodičima: Evidencija osnovnih podataka o vodičima (ime, prezime, biografija)
i praćenje njihovog prosečnog rejtinga.
Sertifikacija: Praćenje licenci i sertifikata (naziv, izdavač, datum isteka) povezanih sa konkretnim vodičima.
Jezička podrška:Administracija jezika koje vodiči govore (Many-to-Many relacija).
**Tehnologije i Port**
Tehnologije: Java, Spring Boot, Spring Data JPA, H2 Database, Lombok.
Mrežni port: Mikroservis operiše na portu 8085.
**API Endpoints**
1. Vodiči(/api/vodici)
   Metoda-Endpoint-Opis
   POST-/guide/add-Dodavanje novog vodiča u sistem.
   GET-/guide/all-Vraća listu svih vodiča.
   GET-/guide/guideid/{id}-Vraća detalje vodiča na osnovu ID-ja.
   PUT-/guide/update/{id}-Ažuriranje podataka o vodiču.
   GET-/guide/averagerating-Računa i vraća prosečnu ocenu svih vodiča u sistemu.
   DELETE-/guide/delete/{id}-Brisanje vodiča iz baze.
2. Sertifikati (/api/vodici)
   Metoda-Endpoint-Opis
   POST-/certifications/add-Dodavanje sertifikata.
   GET-/certifications/all-Pregled svih izdatih sertifikata.
   GET-/certifications/{id}-Detalji sertifikata po ID-ju.
   PUT-/certifications/update/{id}-Ažuriranje podataka o sertifikatu.
   DELETE-/certifications/delete/{id}-Brisanje sertifikata.
   GET-/certifications/{ime}/{prezime}-Pretraga sertifikata na osnovu imena i prezimena vodiča.
3. Jezici (/api/vodici)
   Metoda-Endpoint-Opis
   POST-/languages/add-Unos novog jezika u šifarnik.
   GET-/languages/all-Lista svih podržanih jezika.
   GET-/languages/{id}-Detalji o jeziku po ID-ju.
   PUT-/languages/update/{id}-Ažuriranje podataka o jeziku.
   DELETE-/languages/delete/{id}-Brisanje jezika iz baze.
   **Instalacija i lokalno pokretanje**
   Preduslovi:
1. Java 17 ili novija.
2. Maven.
   Koraci:
1. Klonirajte projekat.
2. Pozicionirajte se u koren direktorijuma servisa.
3. Pokrenite aplikaciju komandom:Bashmvn spring-boot:run
4. Aplikacija će biti dostupna na adresi: http://localhost:8085.
   Testiranje:
1. H2 Konzola: Bazi možete pristupiti putem browsera na http://localhost:8085/h2-console
   (JDBC URL: jdbc:h2:mem:vodicidb,Username: ml, Password: prazno).
2. Swagger UI: Dokumentaciju i testiranje endpointa možete vršiti
   putem Swagger interfejsa na: http://localhost:8085/swagger-ui.html.
3. Inicijalni podaci: Prilikom pokretanja, baza se automatski popunjava testnim podacima iz import.sql fajla.