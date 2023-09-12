CREATE TABLE Residents (
idResident INT AUTO_INCREMENT PRIMARY KEY,
first_name VARCHAR(45),
last_name VARCHAR(45),
email VARCHAR(45),
phone_number VARCHAR(45),
apartment_count INT,
lives_here TINYINT,
apartment_id INT,
FOREIGN KEY (apartment_id) REFERENCES Apartments(idApartments)
);

CREATE TABLE Apartments (
idApartments INT AUTO_INCREMENT PRIMARY KEY,
apartmentNumber INT,
floor INT,
roomCount INT,
area INT,
building_id INT,
FOREIGN KEY (building_id) REFERENCES buildings(idBuildings)
);

CREATE TABLE Buildings (
idBuildings INT AUTO_INCREMENT PRIMARY KEY,
address VARCHAR(45),
apartmentCount INT,
floorCount INT
);

CREATE TABLE ResidentsCar (
idResidentsCar INT AUTO_INCREMENT PRIMARY KEY,
carModel VARCHAR(45),
plateNumber VARCHAR(45),
entryPermit TINYINT,
resident_id INT,
FOREIGN KEY (resident_id) REFERENCES Residents(idResident)
);