CREATE DATABASE AlphaSolutionsDB DEFAULT CHARACTER SET utf8;
USE AlphaSolutionsDB;

DROP TABLE IF EXISTS EmployeeRole;
DROP TABLE IF EXISTS Employee;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Project;
DROP TABLE IF EXISTS SubProject;
DROP TABLE IF EXISTS Task;
DROP TABLE IF EXISTS Status;

CREATE TABLE EmployeeRole (
    roleID INT NOT NULL AUTO_INCREMENT,
    roleTitle VARCHAR(50) NOT NULL,
    isManager BOOLEAN DEFAULT false,
    PRIMARY KEY (roleID)
);

CREATE TABLE Employee (
	employeeID INT NOT NULL AUTO_INCREMENT,
    fullName VARCHAR(50) NOT NULL,
    username varchar(50) NOT NULL UNIQUE,
    password varchar(50) NOT NULL,
    role INT NULL,
    PRIMARY KEY (employeeID),
    FOREIGN KEY (role) REFERENCES EmployeeRole(roleID) ON DELETE SET NULL
);

CREATE TABLE Customer (
	customerID INT NOT NULL AUTO_INCREMENT,
    companyName VARCHAR(50) NOT NULL,
    repName varchar(50) NOT NULL,
    PRIMARY KEY (customerID)
);

CREATE TABLE Status (
	statusID INT NOT NULL AUTO_INCREMENT,
    status VARCHAR(50) NOT NULL,
    PRIMARY KEY (statusID)
);
  
  CREATE TABLE Project (
	projectID INT NOT NULL AUTO_INCREMENT,
    projectTitle VARCHAR(100) NOT NULL,
    projectDescription varchar(50) NULL,
    customer INT NOT NULL,
    orderDate DATE NULL,
    deliveryDate DATE NULL,
    linkAgreement varchar(500) NULL,
    companyRep INT NULL,
    status INT NOT NULL DEFAULT 1, 
    PRIMARY KEY (projectID),
    FOREIGN KEY (customer) REFERENCES Customer(customerID) ON DELETE CASCADE,
    FOREIGN KEY (companyRep) REFERENCES Employee(employeeID) ON DELETE SET NULL,
    FOREIGN KEY (status) REFERENCES Status(statusID) 
);
    
CREATE TABLE SubProject (
    subProjectID INT NOT NULL AUTO_INCREMENT,        
    subProjectTitle VARCHAR(100) NOT NULL,
    subProjectDescription VARCHAR(500) NULL,
	projectID INT NOT NULL,
    status INT NOT NULL DEFAULT 1, 
    PRIMARY KEY (subProjectID),
    FOREIGN KEY (projectID) REFERENCES Project(projectID) ON DELETE CASCADE,
    FOREIGN KEY (status) REFERENCES Status(statusID) 
);

CREATE TABLE Task (
    taskID INT NOT NULL AUTO_INCREMENT,
    taskTitle VARCHAR(100) NOT NULL,
    taskDescription VARCHAR(500) NULL,
    assignedEmployee INT NULL,
    estimatedTime DOUBLE NULL,
    actualTime DOUBLE NULL,
    plannedStartDate DATE NULL,
    dependingOnTask INT NULL,
    requiredRole INT NULL,
    subProjectID INT NOT NULL,
	status INT NOT NULL DEFAULT 1, 
    PRIMARY KEY (taskID),
    FOREIGN KEY (subProjectID) REFERENCES SubProject(subProjectID) ON DELETE CASCADE,
    FOREIGN KEY (assignedEmployee) REFERENCES Employee(employeeID) ON DELETE SET NULL,
    FOREIGN KEY (dependingOnTask) REFERENCES Task(taskID) ON DELETE SET NULL,
    FOREIGN KEY (requiredRole) REFERENCES EmployeeRole(roleID) ON DELETE SET NULL,
    FOREIGN KEY (status) REFERENCES Status(statusID) 
);

