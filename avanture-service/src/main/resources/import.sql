-- 1. AdventureCategories
INSERT INTO adventure_categories (name, description) VALUES ('Planinarenje', 'Aktivnosti na visokim nadmorskim visinama.');
INSERT INTO adventure_categories (name, description) VALUES ('Vodeni sportovi', 'Avanture na rekama i jezerima.');
INSERT INTO adventure_categories (name, description) VALUES ('Ekstremni sportovi', 'Adrenalinske aktivnosti.');

-- 2. Adventures
INSERT INTO adventures (title, description, difficulty_level, base_price, guide_level_required, adventure_categories_id, guide_id) VALUES ('Uspon na Midžor', 'Celodnevna tura na najviši vrh.', 'MEDIUM', 3500.0, 3, 1, 1);
INSERT INTO adventures (title, description, difficulty_level, base_price, guide_level_required, adventure_categories_id, guide_id) VALUES ('Rafting Tarom', 'Spust kanjonom.', 'HARD', 12000.0, 4, 2, 2);
INSERT INTO adventures (title, description, difficulty_level, base_price, guide_level_required, adventure_categories_id, guide_id) VALUES ('Paraglajding', 'Let iznad Zlatibora.', 'EASY', 8500.0, 2, 3, 3);

-- 3. TimeSlots
INSERT INTO time_slots (term_mark, start_time, end_time, max_capacity, current_occupancy, adventures_id) VALUES ('MAJ-2026-01', '2026-05-15', '2026-05-15', 15, 5, 1);
INSERT INTO time_slots (term_mark, start_time, end_time, max_capacity, current_occupancy, adventures_id) VALUES ('JUN-2026-02', '2026-06-10', '2026-06-10', 20, 0, 2);

-- 4. Announecements
INSERT INTO announcements (title, description,  time_slots_id) VALUES ('Nova Avantura', 'Spremite se za uspon na Midžor!',  1);
INSERT INTO announcements (title, description,  time_slots_id) VALUES ('Promena termina', 'Rafting je pomeren zbog nivoa vode.',2);

-- 5. GearRequirements
INSERT INTO gear_requirements (item_name, is_mandatory, adventures_id) VALUES ('Planinarske cipele', true, 1);
INSERT INTO gear_requirements (item_name, is_mandatory, adventures_id) VALUES ('Prsluk za spasavanje', true, 2);
INSERT INTO gear_requirements (item_name, is_mandatory, adventures_id) VALUES ('Kaciga', true, 2);
INSERT INTO gear_requirements (item_name, is_mandatory, adventures_id) VALUES ('Vetrobrana jakna', false, 3);

-- 6. AdventureReviews
INSERT INTO adventure_reviews (rating, comment, created_at, adventures_id) VALUES (5, 'Sjajno!', '2026-03-01', 1);
INSERT INTO adventure_reviews (rating, comment, created_at, adventures_id) VALUES (4, 'Naporno ali lepo.', '2026-03-10', 2);