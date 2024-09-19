CREATE TABLE wineDrinker (
    username TEXT PRIMARY KEY,
    password TEXT,
    countryPreference TEXT,
    colourPreference TEXT,
    fullnessPreference TEXT,
    grapePreference TEXT,
    abvLimit DOUBLE
);
--Split
CREATE TABLE wineSuper (
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
    year INT
);
--Split
CREATE TABLE wine (
    id INTEGER PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES wineSuper (id)
);
--Split
CREATE TABLE personalWine (
    id INTEGER PRIMARY KEY,
    wineDrinker TEXT,
    FOREIGN KEY (id) REFERENCES wineSuper (id),
    FOREIGN KEY (wineDrinker) REFERENCES wineDrinker (username)
);
--Split
CREATE TABLE wineList (
    name TEXT,
    username TEXT,
    description TEXT,
    PRIMARY KEY (name, username),
    FOREIGN KEY (username) REFERENCES wineDrinker (username)
);
--Split
CREATE TABLE contains (
    note TEXT,
    wineId INTEGER,
    listName TEXT,
    wineDrinker TEXT,
    PRIMARY KEY (wineId, listName, wineDrinker),
    FOREIGN KEY (wineId) REFERENCES wine (id),
    FOREIGN KEY (listName, wineDrinker) REFERENCES wineList (name, username)
);
--Split
CREATE TABLE writesNoteAbout (
    wineDrinker TEXT,
    wineId INTEGER,
    note TEXT,
    PRIMARY KEY (wineDrinker, wineId),
    FOREIGN KEY (wineDrinker) REFERENCES wineDrinker (username),
    FOREIGN KEY (wineId) REFERENCES wineSuper (id)
);
--Split
CREATE TABLE logs (
    wineDrinker TEXT,
    wineId INTEGER,
    logEntry TEXT,
    date DATE,
    time TIME,
    quantity FLOAT,
    PRIMARY KEY (wineDrinker, wineId),
    FOREIGN KEY (wineDrinker) REFERENCES wineDrinker (username),
    FOREIGN KEY (wineId) REFERENCES wineSuper (id)
);
--Split
CREATE TABLE grape (
    wineId INTEGER,
    name TEXT,
    PRIMARY KEY (wineId, name),
    FOREIGN KEY (wineId) REFERENCES wineSuper (id)
);
--Split
CREATE TABLE award (
    wineId INTEGER,
    name TEXT,
    PRIMARY KEY (wineId, name),
    Foreign KEY (wineId) REFERENCES wineSuper (id)
);
