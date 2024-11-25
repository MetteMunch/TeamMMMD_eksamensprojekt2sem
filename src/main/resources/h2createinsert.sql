CREATE TABLE EmployeeRole
(
    roleID    INT         NOT NULL AUTO_INCREMENT,
    roleTitle VARCHAR(50) NOT NULL,
    isManager BOOLEAN DEFAULT false,
    PRIMARY KEY (roleID)
);

CREATE TABLE Employee
(
    employeeID INT         NOT NULL AUTO_INCREMENT,
    fullName   VARCHAR(50) NOT NULL,
    username   varchar(50) NOT NULL UNIQUE,
    password   varchar(50) NOT NULL,
    role       INT         NULL,
    PRIMARY KEY (employeeID),
    FOREIGN KEY (role) REFERENCES EmployeeRole (roleID) ON DELETE SET NULL
);

CREATE TABLE Customer
(
    customerID  INT         NOT NULL AUTO_INCREMENT,
    companyName VARCHAR(50) NOT NULL,
    repName     varchar(50) NOT NULL,
    PRIMARY KEY (customerID)
);

CREATE TABLE Status
(
    statusID INT         NOT NULL AUTO_INCREMENT,
    status   VARCHAR(50) NOT NULL,
    PRIMARY KEY (statusID)
);

CREATE TABLE Project
(
    projectID          INT          NOT NULL AUTO_INCREMENT,
    projectTitle       VARCHAR(100) NOT NULL,
    projectDescription varchar(50)  NULL,
    customer           INT          NOT NULL,
    orderDate          DATE         NULL,
    deliveryDate       DATE         NULL,
    linkAgreement      varchar(500) NULL,
    companyRep         INT          NULL,
    status             INT          NOT NULL DEFAULT 1,
    PRIMARY KEY (projectID),
    FOREIGN KEY (customer) REFERENCES Customer (customerID) ON DELETE CASCADE,
    FOREIGN KEY (companyRep) REFERENCES Employee (employeeID) ON DELETE SET NULL,
    FOREIGN KEY (status) REFERENCES Status (statusID)
);

CREATE TABLE SubProject
(
    subProjectID          INT          NOT NULL AUTO_INCREMENT,
    subProjectTitle       VARCHAR(100) NOT NULL,
    subProjectDescription VARCHAR(500) NULL,
    projectID             INT          NOT NULL,
    status                INT          NOT NULL DEFAULT 1,
    PRIMARY KEY (subProjectID),
    FOREIGN KEY (projectID) REFERENCES Project (projectID) ON DELETE CASCADE,
    FOREIGN KEY (status) REFERENCES Status (statusID)
);

CREATE TABLE Task
(
    taskID           INT          NOT NULL AUTO_INCREMENT,
    taskTitle        VARCHAR(100) NOT NULL,
    taskDescription  VARCHAR(500) NULL,
    assignedEmployee INT          NULL,
    estimatedTime    DOUBLE       NULL,
    actualTime       DOUBLE       NULL,
    plannedStartDate DATE         NULL,
    dependingOnTask  INT          NULL,
    requiredRole     INT          NULL,
    subProjectID     INT          NOT NULL,
    status           INT          NOT NULL DEFAULT 1,
    PRIMARY KEY (taskID),
    FOREIGN KEY (subProjectID) REFERENCES SubProject (subProjectID) ON DELETE CASCADE,
    FOREIGN KEY (assignedEmployee) REFERENCES Employee (employeeID) ON DELETE SET NULL,
    FOREIGN KEY (dependingOnTask) REFERENCES Task (taskID) ON DELETE SET NULL,
    FOREIGN KEY (requiredRole) REFERENCES EmployeeRole (roleID) ON DELETE SET NULL,
    FOREIGN KEY (status) REFERENCES Status (statusID)
);

INSERT INTO EmployeeRole (roleTitle, isManager)
VALUES ('Project Manager', true),
       ('Business Consultant', true),
       ('Backend developer', false),
       ('Frontend developer', false);

INSERT INTO Employee (fullName, username, password, role)
VALUES ('John Wales', 'johnwa', 'password123', 1),        -- Project Manager
       ('Jane Smith', 'janesm', 'password123', 1),        -- Project Manager
       ('Dorothy Parker', 'dorothypa', 'password123', 2), -- Business Consultant
       ('James Hansson', 'jamesha', 'password123', 2),    -- Business Consultant
       ('Alice Johnson', 'alicejo', 'password123', 3),    -- Backend developer
       ('Peter Brown', 'peterbr', 'password123', 3),      -- Backend developer
       ('Bob Clifford', 'bobcl', 'password123', 4),       -- Frontend developer
       ('Susan Scott', 'susansc', 'password123', 4); -- Frontend developer

INSERT INTO Customer (companyName, repName)
VALUES ('TechCorp', 'Emily White'),
       ('Innovate Solutions', 'David Green');

INSERT INTO Status (status)
VALUES ('Pending'),
       ('In progress'),
       ('Avaiting approval'),
       ('Done');

INSERT INTO Project (projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep,
                     status)
VALUES ('Website Redesign', 'Redesign of company website', 1, '2024-12-05', '2025-01-15',
        'https://kea.dk/',
        3, 1),
       ('CRM Implementation', 'Setup of CRM system', 2, '2024-09-15', '2024-12-30',
        'https://kea.dk/',
        4, 2);

INSERT INTO SubProject (subProjectTitle, subProjectDescription, projectID, status)
VALUES ('Frontend Development', 'Develop the frontend for the new website', 1, 1),
       ('Backend Integration', 'Integrate backend systems for CRM', 2, 1),
       ('Kravspec', 'prepare requirements specification', 2, 2);

INSERT INTO Task (taskTitle, taskDescription, assignedEmployee, estimatedTime, actualTime, plannedStartDate,
                  dependingOnTask, requiredRole, subProjectID, status)
VALUES ('Design Mockups', 'Create initial design mockups', 4, 40, NULL, '2024-12-15', NULL, 4, 1, 1),
       ('Outline to approval', 'Finish outline with requirements together with customer', 4, 15, NULL, '2024-12-05',
        NULL, 4, 1, 1),
       ('API Development', 'Develop the necessary APIs', 3, 80, NULL, '2024-02-01', NULL, 3, 2, 1),
       ('Testing', 'Test the system functionality', NULL, 60, NULL, '2024-03-01', 2, 2, 2, 1);
