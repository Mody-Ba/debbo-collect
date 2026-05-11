-- liquibase formatted sql

-- changeset Mody:insert-utilisateur-1
INSERT INTO utilisateur (nom, prenom, email)
VALUES ('Ba', 'Mody', 'mody@gmail.com');

-- changeset Mody:insert-utilisateur-2
INSERT INTO utilisateur (nom, prenom, email)
VALUES ('Diallo', 'Ali', 'ali@gmail.com');