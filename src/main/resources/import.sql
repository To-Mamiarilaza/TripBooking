-- Insert Users
INSERT INTO users (email, password, role) VALUES ('admin@trip.com', 'admin123', 'ADMIN');
INSERT INTO users (email, password, role) VALUES ('customer@trip.com', 'customer123', 'CUSTOMER');

-- Insert Lieu (3 cities)
INSERT INTO lieu (nom) VALUES ('Antananarivo');
INSERT INTO lieu (nom) VALUES ('Antsirabe');
INSERT INTO lieu (nom) VALUES ('Fianarantsoa');

-- Insert Tarif (for all routes between cities)
INSERT INTO tarif (id_origine, id_destination, prix) VALUES (1, 2, 50000.0);
INSERT INTO tarif (id_origine, id_destination, prix) VALUES (1, 3, 75000.0);
INSERT INTO tarif (id_origine, id_destination, prix) VALUES (2, 1, 50000.0);
INSERT INTO tarif (id_origine, id_destination, prix) VALUES (2, 3, 45000.0);
INSERT INTO tarif (id_origine, id_destination, prix) VALUES (3, 1, 75000.0);
INSERT INTO tarif (id_origine, id_destination, prix) VALUES (3, 2, 45000.0);

-- Insert Chauffeur (2)
INSERT INTO chauffeur (nom, telephone, etat) VALUES ('Jean Dupont', '+261321234567', 1);
INSERT INTO chauffeur (nom, telephone, etat) VALUES ('Marie Martin', '+261322345678', 1);

-- Insert Voiture (2)
INSERT INTO voiture (numero, marque, etat) VALUES ('AA-001-AA', 'Toyota', 1);
INSERT INTO voiture (numero, marque, etat) VALUES ('AA-002-AA', 'Mercedes', 1);

-- Insert PlaceVoiture for Voiture 1 (18 places)
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 1, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 2, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 3, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 4, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 5, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 6, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 7, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 8, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 9, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 10, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 11, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 12, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 13, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 14, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 15, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 16, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 17, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (1, 18, 1);

-- Insert PlaceVoiture for Voiture 2 (18 places)
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 1, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 2, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 3, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 4, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 5, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 6, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 7, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 8, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 9, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 10, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 11, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 12, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 13, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 14, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 15, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 16, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 17, 1);
INSERT INTO place_voiture (id_voiture, numero, etat) VALUES (2, 18, 1);

-- Create View: voyage_seat_states
-- Displays seat availability and reservation status for all voyages
DROP VIEW IF EXISTS v_voyage_seat_states;
CREATE VIEW v_voyage_seat_states AS SELECT v.id AS voyage_id, v.depart AS departure_time, l_orig.nom AS origin, l_dest.nom AS destination, v.prix AS price, vo.numero AS car_number, vo.marque AS car_brand, pv.numero AS seat_number, pv.id AS seat_id, CASE WHEN rp.id IS NOT NULL THEN 'RESERVED' ELSE 'AVAILABLE' END AS seat_status, COALESCE(r.nom_voyageur, '') AS passenger_name, COALESCE(r.telephone, '') AS passenger_phone, CASE WHEN rp.id IS NULL THEN 'NONE' WHEN rp.etat = 1 THEN 'ACTIVE' WHEN rp.etat = 0 THEN 'CANCELLED' ELSE 'UNKNOWN' END AS reservation_state, v.etat AS voyage_state, pv.etat AS seat_state FROM voyage v INNER JOIN lieu l_orig ON v.id_origine = l_orig.id INNER JOIN lieu l_dest ON v.id_destination = l_dest.id INNER JOIN voiture vo ON v.id_voiture = vo.id INNER JOIN place_voiture pv ON vo.id = pv.id_voiture LEFT JOIN reservation_place rp ON rp.id_place = pv.id AND rp.etat = 1 LEFT JOIN reservation r ON r.id = rp.id_reservation AND r.id_voyage = v.id ORDER BY v.depart, vo.numero, pv.numero;

-- Create View: revenue_by_month
-- Displays total revenue for each month/year based on confirmed reservations
DROP VIEW IF EXISTS v_revenue_by_month;
CREATE VIEW v_revenue_by_month AS SELECT EXTRACT(YEAR FROM v.depart) AS year, EXTRACT(MONTH FROM v.depart) AS month, TO_CHAR(v.depart, 'YYYY-MM') AS year_month, COUNT(DISTINCT res.id) AS total_reservations, COUNT(DISTINCT rp.id) AS total_seats_booked, SUM(v.prix) AS total_revenue FROM voyage v INNER JOIN reservation res ON res.id_voyage = v.id AND res.etat = 2 INNER JOIN reservation_place rp ON rp.id_reservation = res.id AND rp.etat = 1 GROUP BY EXTRACT(YEAR FROM v.depart), EXTRACT(MONTH FROM v.depart), TO_CHAR(v.depart, 'YYYY-MM');

-- Create View: revenue_by_vehicle
-- Displays total revenue for each vehicle based on confirmed reservations
DROP VIEW IF EXISTS v_revenue_by_vehicle;
CREATE VIEW v_revenue_by_vehicle AS SELECT vo.id AS vehicle_id, vo.numero AS vehicle_number, vo.marque AS vehicle_brand, COUNT(DISTINCT res.id) AS total_reservations, COUNT(DISTINCT rp.id) AS total_seats_booked, SUM(v.prix) AS total_revenue, COUNT(DISTINCT v.id) AS total_voyages FROM voiture vo INNER JOIN voyage v ON v.id_voiture = vo.id INNER JOIN reservation res ON res.id_voyage = v.id AND res.etat = 2 INNER JOIN reservation_place rp ON rp.id_reservation = res.id AND rp.etat = 1 GROUP BY vo.id, vo.numero, vo.marque;

-- Create View: revenue_by_destination
-- Displays total revenue for each destination based on confirmed reservations with number of trips
DROP VIEW IF EXISTS v_revenue_by_destination;
CREATE VIEW v_revenue_by_destination AS SELECT l.id AS destination_id, l.nom AS destination_name, COUNT(DISTINCT v.id) AS total_trips, COUNT(DISTINCT res.id) AS total_reservations, COUNT(DISTINCT rp.id) AS total_seats_booked, SUM(v.prix) AS total_revenue FROM lieu l INNER JOIN voyage v ON v.id_destination = l.id INNER JOIN reservation res ON res.id_voyage = v.id AND res.etat = 2 INNER JOIN reservation_place rp ON rp.id_reservation = res.id AND rp.etat = 1 GROUP BY l.id, l.nom;