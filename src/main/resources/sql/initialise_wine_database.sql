CREATE TABLE wine (
      id INTEGER PRIMARY KEY,
      name TEXT,
      type TEXT,
      country TEXT,
      year INT,
      shortDescription TEXT,
      longDescription TEXT,
      awards TEXT,
      pricePerBottle FLOAT,
      alcoholByVolume FLOAT,
      volumeInML FLOAT );
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
