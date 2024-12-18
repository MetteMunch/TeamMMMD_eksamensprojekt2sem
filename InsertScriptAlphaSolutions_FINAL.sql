USE AlphaSolutionsDB;
INSERT INTO EmployeeRole (roleTitle, isManager) VALUES 
('Project Manager', true),
('Business Consultant', true),
('Backend developer', false),
('Frontend developer', false);

INSERT INTO Employee (fullName, username, password, role) VALUES 
('John Wales', 'johnwa', 'password123', 1), -- Project Manager
('Jane Smith', 'janesm', 'password123', 1), -- Project Manager
('Dorothy Parker', 'dorothypa', 'password123', 2), -- Business Consultant
('James Hansson', 'jamesha', 'password123', 2), -- Business Consultant
('Alice Johnson', 'alicejo', 'password123', 3), -- Backend developer
('Peter Brown', 'peterbr', 'password123', 3), -- Backend developer
('Bob Clifford', 'bobcl', 'password123', 4), -- Frontend developer
('Susan Scott', 'susansc', 'password123', 4); -- Frontend developer

INSERT INTO Customer (companyName, repName) VALUES 
('Internal Project','Internal'),
('New Customer','New Customer'),
('TechCorp', 'Emily White'),
('Innovate Solutions', 'David Green'),
('OutdoorTravel', 'Susan Jones'),
('Mr and sons', 'Tristan Brown'),
('UI Technologies', 'Hannah Jameson');

INSERT INTO Status (status) VALUES 
('Pending'),
('In progress'),
('Awaiting approval'),
('Done');

INSERT INTO Project (projectTitle, projectDescription, customer, orderDate, deliveryDate, linkAgreement, companyRep, status) VALUES 
('Website Redesign', 'Redesign of company website', 3, '2024-12-05', '2025-01-15', 'https://example.com/order/WebsiteRedesign', 1, 2),
('CRM Implementation', 'Setup of CRM system', 4, '2024-09-15', '2024-12-30', 'https://example.com/order/CRM', 2, 3),
('E-commerce Platform', 'Development of an online shopping platform', 5, '2024-10-01', '2025-02-15', 'https://example.com/order/EcommercePlatform', 2, 2),
('Mobile App', 'Creation of a mobile app for customer engagement', 6, '2024-08-20', '2025-02-28', 'https://example.com/order/MobileApp', 1, 1),
('Data Migration', 'Migrate legacy system data to new CRM', 7, '2024-11-01', '2025-01-15', 'https://example.com/order/DataMigration', 2, 2),
('AI Chatbot Development', 'Development of a custom AI chatbot', 1, '2024-12-10', '2025-06-15', 'https://example.com/order/AIChatbot', 1, 1),
('Cybersecurity Audit', 'Comprehensive security audit', 1, '2024-11-05', '2025-01-30', 'https://example.com/order/CybersecurityAudit', 3, 2);


INSERT INTO SubProject (subProjectTitle, subProjectDescription, projectID, status) VALUES 
('Frontend Development', 'Develop the frontend for the new website', 1, 1),
('Wireframes', 'outline sketches wireframes', 1, 2),
('Backend Integration', 'Integrate backend systems for CRM', 2, 1),
('Kravspec', 'prepare requirements specification', 2, 2),
('Payment Gateway Integration', 'Integrate a secure payment system into the platform', 3, 1),
('User Interface Design', 'Design the user interface for the mobile app', 4, 2),
('Data Cleanup', 'Clean and validate legacy data before migration', 5, 1),
('User Training', 'Train customer employees to use the new CRM', 2, 4),
('Natural Language Processing', 'Implement NLP capabilities for chatbot', 6, 1),
('Dialog Flow Design', 'Design conversational flows for the chatbot', 6, 2),
('Vulnerability Assessment', 'Assess vulnerabilities in current systems', 7, 1),
('Risk Mitigation Plan', 'Develop a plan to mitigate identified risks', 7, 2);

INSERT INTO Task (taskTitle, taskDescription, assignedEmployee, estimatedTime, actualTime, plannedStartDate, dependingOnTask, requiredRole, subProjectID, status) VALUES 
('Design Mockups', 'Create initial design mockups', 4, 40, NULL, '2024-12-15', NULL, 4, 1, 1),
('Outline to approval', 'Finish outline with requirements together with customer', 4, 15, 7.75, '2024-12-05', NULL, 4, 2, 2),
('API Development', 'Develop the necessary APIs', 3, 80, 81.5, '2024-02-01', NULL, 3, 3, 2),
('Testing', 'Test the system functionality', 6, 60, 36.75, '2024-11-01', 2, 2, 3, 2),
('Setup Hosting Environment', 'Configure cloud hosting for the platform', 6, 30, 45.5, '2024-10-15', NULL, 3, 5, 2),
('Build Responsive Layout', 'Create a responsive layout for the app', 8, 50, 4.75, '2024-12-10', NULL, 4, 6, 2),
('Data Validation Scripts', 'Write scripts to validate data consistency', 5, 40, NULL, '2025-01-05', NULL, 3, 7, 1),
('End-User Documentation', 'Prepare user guides and manuals', 7, 25, 18.75, '2024-12-01', NULL, 2, 8, 2),
('Performance Testing', 'Test the platform under load conditions', NULL, 60, NULL, '2025-03-01', NULL, 3, 5, 1),
('Push Notifications', 'Implement push notification functionality', 8, 30, 27.5, '2024-10-25', 2, 4, 4, 3),
('Feedback Collection', 'Gather feedback from beta users', NULL, 20, NULL, '2025-02-01', 4, 2, 4, 1),
('NLP Model Training', 'Train the chatbot using sample datasets', 5, 100, NULL, '2024-12-20', NULL, 3, 9, 1),
('Text Preprocessing', 'Prepare data for training the NLP model', NULL, 40, NULL, '2024-12-15', NULL, 3, 9, 1),
('Entity Recognition', 'Implement entity recognition for user inputs', NULL, 50, NULL, '2025-01-10', NULL, 3, 9, 1),
('Draft Conversational Paths', 'Create initial conversational flows', 7, 30, NULL, '2024-12-25', NULL, 2, 10, 1),
('User Testing', 'Conduct tests with users to refine conversations', 8, 40, NULL, '2025-02-01', 4, 4, 10, 1),
('Feedback Analysis', 'Analyze feedback from testing and refine flows', NULL, 25, NULL, '2025-02-15', NULL, 2, 10, 1),
('System Scanning', 'Perform scans to identify vulnerabilities', 6, 50, 61.75, '2024-11-10', NULL, 3, 11, 2),
('Report Findings', 'Document vulnerabilities and their impact', 5, 30, 8.5, '2024-12-01', 7, 3, 11, 2),
('Presentation to Stakeholders', 'Present findings to stakeholders for review', 7, 20, NULL, '2024-12-10', NULL, 2, 11, 2),
('Mitigation Strategy Draft', 'Draft strategies to address vulnerabilities', 6, 40, NULL, '2024-12-15', NULL, 2, 12, 1),
('Implementation Plan', 'Develop a detailed plan for implementing fixes', NULL, 50, NULL, '2024-12-20', 10, 2, 12, 1),
('Compliance Verification', 'Ensure fixes meet compliance standards', NULL, 30, NULL, '2025-01-20', NULL, 2, 12, 1);

