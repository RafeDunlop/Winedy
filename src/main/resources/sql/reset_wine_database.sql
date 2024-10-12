PRAGMA FOREIGN_KEYS = ON;
DELETE FROM wine;
DELETE FROM grape;
DELETE FROM award;
--Split
CREATE TABLE wine (
    id INTEGER PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES wineSuper (id) ON DELETE CASCADE
);
--Split
CREATE TABLE grape (
    wineId INTEGER,
    name TEXT,
    PRIMARY KEY (wineId, name),
    FOREIGN KEY (wineId) REFERENCES wineSuper (id) ON DELETE CASCADE
);
--Split
CREATE TABLE award (
    wineId INTEGER,
    name TEXT,
    PRIMARY KEY (wineId, name),
    Foreign KEY (wineId) REFERENCES wineSuper (id) ON DELETE CASCADE
);