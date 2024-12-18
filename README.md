# **Team MMMDs Eksamensprojekt**

## :denmark: Hvad er projektet?
Projektet er udviklet i forbindelse med repo-ejer og tilhørende collaborators' 2. semestereksamen på Datamatiker.
  
Det nærværende github-repository er et projektkalkulationsværktøj, der er designet mod projektledere og deres ansatte
for at give et tilstrækkeligt overblik af, hvor et givent projekt befinder sig i dets livscyklus.
  
Eksamensprojektet indeholder mulighed for at kunne logge sig ind som bruger enten som projektleder eller medarbejder. 

### Projektleder
En projektleder har mulighed for at oprette projekter, subprojekter og tasks. Projektlederen kan herefter bestemme, 
hvornår et givent projekt skal have opstart og leverance. Derudover kan en projektleder tildele tasks til bestemte 
medarbejdere.
  
Derudover er der lavet et specifikt html view til både en project manager og employee. Projektlederen har en oversigt over
tasks, der kan forsinke projektet udover den aftalte leverancedato. 
  
Projektledere har endvidere en oversigt af, hvor mange estimerede timer, der befinder sig på et givent projekt; og hvor 
mange aktuelle timer, der er registreret på et projekt. Afslutningsvist har de en oversigt af tasks, der ikke er 
blevet tildelt en medarbejder.
  
### Medarbejder
En medarbejder har en oversigt over, hvilke tasks som de er blevet tildelt. De har desuden mulighed for at indsende
den mængde af timer som de har brugt på en given task.
  
Derudover har de adgang til at kunne se detaljerne på de enkelte projekter og subprojekter, hvor deres task(s) er tilknyttet.
Dog, uden de samme muligheder for at redigere eller slette som en projektleder har.
  
## :uk: What is the project?
The project has been developed by the owner of the GitHub repository and its accompanying collaborators in order to 
fulfill the requirements posed by the 2nd semester Computer Science exam.
  
The GitHub repository is a project calculation tool that has been designed towards project managers and their employees 
to provide an overview of the life cycle of a given project.
  
The exam project provides the opportunity to either login as a project manager or an employee.
  
### Project Manager
The project manager is able to create projects, subprojects and tasks. Furthermore, the project manager can define
the date when the project starts and when the delivery is due. 
  
The project manager is provided an overview of the amount of estimated hours a project has been allocated and the amount of
actual logged hours provided by the employees. Lastly, the project managers can view unassigned tasks.
  
### Employee
An employee is provided with an overview of the tasks they have been assigned. Furthermore, they have the option of submitting
the hours they have worked on a given task.
  
Employees are also given access to view projects and subprojects if their assigned tasks are associated.
Employees are, however, not given the same privileges as project managers in regard to deleting / updating projects or
subprojects.
  
# Software Requirements / Software Krav
Software brugt til udvikling af applikation: 
* IntelliJ IDEA Ultimate Edition 2023.3.7
  * Java Development Kit(JDK) 21
* Spring Boot 3.4.0
  * Thymeleaf(Dependency)
  * Spring Web(Dependency)
* MySQL Connector/J 9.0.0
* MySQL Workbench 8.0
* Mockito 5.2.0(testing)
* Maven
* H2 Database