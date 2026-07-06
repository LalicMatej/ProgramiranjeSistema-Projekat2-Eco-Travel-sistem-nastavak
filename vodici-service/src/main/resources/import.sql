-- 1. Prvo popunjavamo jezike (Languages) jer oni ne zavise ni od koga
INSERT INTO languages (language_name, language_code) VALUES ('Srpski', 'SRB');
INSERT INTO languages (language_name, language_code) VALUES ('Engleski', 'ENG');
INSERT INTO languages (language_name, language_code) VALUES ('Nemački', 'GER');
INSERT INTO languages (language_name, language_code) VALUES ('Francuski', 'FRA');

-- 2. Zatim popunjavamo vodiče (Guides)
INSERT INTO guides (first_name, last_name, bio, rating) VALUES ('Marko', 'Marković', 'Iskusni planinar sa preko 10 godina iskustva na Alpima.', 4.9);
INSERT INTO guides (first_name, last_name, bio, rating) VALUES ('Jelena', 'Jovanović', 'Specijalista za kanjoning i vodene sportove.', 4.8);
INSERT INTO guides (first_name, last_name, bio, rating) VALUES ('Nikola', 'Nikolić', 'Ljubitelj ekstremnih sportova i licencirani paraglajder.', 4.5);

-- 3. Popunjavanje Many-to-Many veze (Join tabela: guides_languages)
-- Marko (ID 1) govori Srpski (1) i Engleski (2)
INSERT INTO guides_languages (guides_id, languages_id) VALUES (1, 1);
INSERT INTO guides_languages (guides_id, languages_id) VALUES (1, 2);

-- Jelena (ID 2) govori Srpski (1), Engleski (2) i Nemački (3)
INSERT INTO guides_languages (guides_id, languages_id) VALUES (2, 1);
INSERT INTO guides_languages (guides_id, languages_id) VALUES (2, 2);
INSERT INTO guides_languages (guides_id, languages_id) VALUES (2, 3);

-- Nikola (ID 3) govori Srpski (1) i Francuski (4)
INSERT INTO guides_languages (guides_id, languages_id) VALUES (3, 1);
INSERT INTO guides_languages (guides_id, languages_id) VALUES (3, 4);

-- 4. Sertifikati (Certifications) - vezani za vodiče
INSERT INTO certifications (name, issuing_body,certification_level, expiry_date, guides_id) VALUES ('Gorska Služba Spasavanja', 'GSS Srbija',4, '2027-12-31', 1);
INSERT INTO certifications (name, issuing_body,certification_level, expiry_date, guides_id) VALUES ('International Rafting Federation L3', 'IRF',5, '2026-05-20', 2);
INSERT INTO certifications (name, issuing_body,certification_level, expiry_date, guides_id) VALUES ('Pro Paragliding License', 'FAA',4, '2028-01-15', 3);