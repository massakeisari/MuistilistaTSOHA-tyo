CREATE TABLE Kayttaja(
    id SERIAL PRIMARY KEY,
    nimi varchar(30) NOT NULL,
    salasana varchar(50) NOT NULL
);

CREATE TABLE Muistettava(
    id SERIAL PRIMARY KEY,
    kayttaja_id INTEGER REFERENCES Kayttaja(id),
    nimi varchar(50) NOT NULL,
    tehty boolean DEFAULT FALSE,
    kuvaus varchar(500),
);