CREATE TABLE wine (
      id INTEGER PRIMARY KEY,
      name TEXT );
--Split
CREATE TABLE wineDrinker (
     id INTEGER PRIMARY KEY,
     username TEXT UNIQUE,
     password TEXT );
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
