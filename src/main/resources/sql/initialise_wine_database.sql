CREATE TABLE wine (
    id INTEGER PRIMARY KEY,
    name TEXT,
    country TEXT,
    colour TEXT,
    style TEXT,
    fullness TEXT,
    longDescription TEXT,
    pricePerBottle FLOAT,
    alcoholByVolume FLOAT,
    volumeInML FLOAT,
    year INT);
--Split
CREATE TABLE wineDrinker (
    username TEXT PRIMARY KEY,
    password TEXT,
    countryPreference TEXT,
    colourPreference TEXT,
    fullnessPreference TEXT,
    grapePreference TEXT);
--Split
CREATE TABLE wineList (
    name TEXT,
    userId INTEGER,
    PRIMARY KEY(name, userId),
    FOREIGN KEY(userId) REFERENCES wineDrinker(id) );
--Split
CREATE TABLE contains (
    note TEXT,
    wineId INTEGER,
    listName TEXT,
    wineDrinkerId INTEGER,
    PRIMARY KEY(wineId, listName, wineDrinkerId),
    FOREIGN KEY(wineId) REFERENCES wine(id),
    FOREIGN KEY(listName, wineDrinkerId) REFERENCES wineList(name, userId) );
--Split
CREATE TABLE grape (
    wineId INTEGER,
    name TEXT,
    PRIMARY KEY (wineId, name),
    FOREIGN KEY (wineId) REFERENCES wine
);
--Split
CREATE TABLE award (
    wineId INTEGER,
    name TEXT,
    PRIMARY KEY (wineId, name),
    Foreign KEY (wineId) REFERENCES wine
);
