INSERT INTO client(name) VALUES
 ('Alice Johnson'), ('Bob Smith'), ('Carol Danvers'), ('Diana Prince'), ('Ethan Hunt'),
 ('Frodo Baggins'), ('Gandalf Grey'), ('Hermione Granger'), ('Taras Shevchenko'), ('John Wick');

INSERT INTO planet(id, name) VALUES
 ('MERCURY', 'Mercury'),
 ('VENUS',   'Venus'),
 ('EARTH',   'Earth'),
 ('MARS',    'Mars'),
 ('JUP1T3R', 'Jupiter');

INSERT INTO ticket(client_id, from_planet_id, to_planet_id) VALUES
 (1,'EARTH','MARS'),
 (2,'MARS','EARTH'),
 (3,'EARTH','VENUS'),
 (4,'VENUS','EARTH'),
 (5,'EARTH','MERCURY'),
 (6,'MERCURY','EARTH'),
 (7,'EARTH','JUP1T3R'),
 (8,'JUP1T3R','EARTH'),
 (9,'MARS','VENUS'),
 (10,'VENUS','MARS');
