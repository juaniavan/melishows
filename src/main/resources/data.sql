INSERT INTO location (id, city, province, country) VALUES (1, 'Buenos Aires', 'Buenos Aires', 'Argentina');
INSERT INTO location (id, city, province, country) VALUES (2, 'Capital Federal', 'CABA', 'Argentina');
INSERT INTO location (id, city, province, country) VALUES (3, 'Rosario', 'Santa Fe', 'Argentina');
    
INSERT INTO theater (id, name, description, address, location_id) VALUES (1, 'Teatro Opera', 'El Teatro Ópera está situado en Avenida Corrientes 860 de la ciudad de Buenos Aires, Argentina, a 200 metros del Obelisco de esa ciudad.', 'Corrientes 860', 2);
INSERT INTO theater (id, name, description, address, location_id) VALUES (2, 'Teatro Gran Rex', 'Es el teatro de los grandes espectáculos musicales y recibe a los artistas más consagrados, nacional e internacionalmente. Su capacidad es para 3.262 espectadores.', 'Corrientes 857', 2);

INSERT INTO room (id, name, description, theater_id) VALUES (1, 'Sala Única', 'Sala Única', 1);
INSERT INTO room (id, name, description, theater_id) VALUES (2, 'Sala Única', 'Sala Única', 2);

INSERT INTO seat (id, name, disabled, room_id) VALUES (1, 'A1', false, 1);
INSERT INTO seat (id, name, disabled, room_id) VALUES (2, 'A2', false, 1);
INSERT INTO seat (id, name, disabled, room_id) VALUES (3, 'A3', false, 1);
INSERT INTO seat (id, name, disabled, room_id) VALUES (4, 'A4', false, 1);
INSERT INTO seat (id, name, disabled, room_id) VALUES (5, 'A5', false, 1);
INSERT INTO seat (id, name, disabled, room_id) VALUES (6, 'B1', false, 1);
INSERT INTO seat (id, name, disabled, room_id) VALUES (7, 'B2', false, 1);
INSERT INTO seat (id, name, disabled, room_id) VALUES (8, 'B3', false, 1);
INSERT INTO seat (id, name, disabled, room_id) VALUES (9, 'B4', false, 1);
INSERT INTO seat (id, name, disabled, room_id) VALUES (10, 'B5', false, 1);

INSERT INTO seat (id, name, disabled, room_id) VALUES (11, 'G1', false, 2);
INSERT INTO seat (id, name, disabled, room_id) VALUES (12, 'G2', false, 2);
INSERT INTO seat (id, name, disabled, room_id) VALUES (13, 'G3', false, 2);
INSERT INTO seat (id, name, disabled, room_id) VALUES (14, 'G4', false, 2);
INSERT INTO seat (id, name, disabled, room_id) VALUES (15, 'G5', false, 2);
INSERT INTO seat (id, name, disabled, room_id) VALUES (16, 'H1', false, 2);
INSERT INTO seat (id, name, disabled, room_id) VALUES (17, 'H2', false, 2);
INSERT INTO seat (id, name, disabled, room_id) VALUES (18, 'H3', false, 2);
INSERT INTO seat (id, name, disabled, room_id) VALUES (19, 'H4', false, 2);
INSERT INTO seat (id, name, disabled, room_id) VALUES (20, 'H5', false, 2);

INSERT INTO show (id, name, description, artist, theater_id) VALUES (1, 'Conociendo Rusia', 'Conociendo Rusia', 'Mateo Sujatovich', 2);
INSERT INTO show (id, name, description, artist, theater_id) VALUES (2, 'Ciro y los persas en el Opera', 'Ciro y los persas en el Opera', 'Andres Ciro Martinez', 1);
INSERT INTO show (id, name, description, artist, theater_id) VALUES (3, 'Luciano Pereyra en Buenos Aires desde el Teatro Opera', 'Luciano Pereyra en Buenos Aires desde el Teatro Opera', 'Luciano Pereyra', 1);

INSERT INTO showing (id, name, schedule, show_id, room_id) VALUES (1, 'Función 24 de abril 20:30hs', '2021-04-24 20:30', 1, 2);
INSERT INTO showing (id, name, schedule, show_id, room_id) VALUES (2, 'Función 25 de abril 20:30hs', '2021-04-25 20:30', 1, 2);
INSERT INTO showing (id, name, schedule, show_id, room_id) VALUES (3, 'Función Extra 5 de Mayo 19:30hs', '2021-05-05 19:30', 2, 1);
INSERT INTO showing (id, name, schedule, show_id, room_id) VALUES (4, 'Función Final 30 de abril 21:00hs', '2021-04-30 21:00', 3, 1);

INSERT INTO sector (id, name, price, showing_id) VALUES (1, 'VIP M&G', 10000, 1);
INSERT INTO sector (id, name, price, showing_id) VALUES (2, 'Platea Preferencial', 7500, 1);
INSERT INTO sector (id, name, price, showing_id) VALUES (3, 'Platea Baja', 4500, 1);
INSERT INTO sector (id, name, price, showing_id) VALUES (4, 'Platea Alta', 2500, 1);

INSERT INTO sector (id, name, price, showing_id) VALUES (5, 'VIP M&G', 10000, 2);
INSERT INTO sector (id, name, price, showing_id) VALUES (6, 'Platea Preferencial', 7500, 2);
INSERT INTO sector (id, name, price, showing_id) VALUES (7, 'Platea Baja', 4500, 2);
INSERT INTO sector (id, name, price, showing_id) VALUES (8, 'Platea Alta', 2500, 2);

INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (1, false, 'VIP-A1', 1, 1);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (2, false, 'VIP-A2', 2, 1);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (3, false, 'VIP-A3', 3, 1);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (4, false, 'PREF-A4', 4, 2);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (5, false, 'PREF-A5', 5, 2);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (6, false, 'PREF-A6', 6, 2);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (7, false, 'PB-A7', 7, 3);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (8, false, 'PB-A8', 8, 3);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (9, false, 'PB-A9', 9, 4);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (10, false, 'PB-A10', 10, 4);

INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (11, false, 'VIP-A1', 1, 5);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (12, false, 'VIP-A2', 2, 5);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (13, false, 'VIP-A3', 3, 5);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (14, false, 'PREF-A4', 4, 6);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (15, false, 'PREF-A5', 5, 6);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (16, false, 'PREF-A6', 6, 6);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (17, false, 'PB-A7', 7, 7);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (18, false, 'PB-A8', 8, 7);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (19, false, 'PB-A9', 9, 8);
INSERT INTO availability (id, reserved, name, seat_id, sector_id) VALUES (20, false, 'PB-A10', 10, 8);
