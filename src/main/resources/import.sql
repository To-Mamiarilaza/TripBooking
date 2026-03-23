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
