package mmmd.teammmmd_eksamensprojekt2sem.service;

import mmmd.teammmmd_eksamensprojekt2sem.model.Project;
import mmmd.teammmmd_eksamensprojekt2sem.model.Task;
import mmmd.teammmmd_eksamensprojekt2sem.repository.ProjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Dette Initialiser mocks og injecter mocks
        //Når vi benytter @MockBean sammen med @SpringBootTest, @WebMvcTest så behøver ovenstående ikke kaldes i setUp, da
        //Spring selv håndterer mocking (se test af Controller klassen / integrationstest)
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void tasksWithCalculatedEndDateLaterThanProjectDeadlineTest() {
        //Arrange
        int employeeID = 1;
        int projectID = 18;

        Project mockedProject = new Project();
        mockedProject.setDeliveryDate(Date.valueOf(LocalDate.of(2024,12,20)));

        Task task1 = new Task();
        task1.setPlannedStartDate(Date.valueOf(LocalDate.of(2024,12,10)));
        task1.setEstimatedTime(66.0);
        task1.setProjectID(projectID);

        Task task2 = new Task();
        task2.setPlannedStartDate(Date.valueOf(LocalDate.of(2024,12,10)));
        task2.setEstimatedTime(12.0);
        task2.setProjectID(projectID);

        Task unrelatedTask = new Task();
        unrelatedTask.setPlannedStartDate(Date.valueOf(LocalDate.of(2024, 12, 10)));
        unrelatedTask.setEstimatedTime(30.0);
        unrelatedTask.setProjectID(25); // Task hører til et andet projekt end de andre

        List<Task> mockTasks = List.of(task1, task2, unrelatedTask);

        when(projectRepository.fetchSpecificProject(projectID)).thenReturn(mockedProject);
        when(projectRepository.showAllTasksSpecificProjectManager(employeeID)).thenReturn(mockTasks);


        int ecpectedSizeOfList = 1;

        //Act
        List<Task> result = projectService.tasksWithCalculatedEndDateLaterThanProjectDeadline(employeeID, projectID);

        //Assert
        assertEquals(ecpectedSizeOfList,result.size()); //der er kun en task som burde være forsinket, nemlig task 1
        assertTrue(result.contains(task1)); //listen bør indeholde task1
        assertFalse(result.contains(task2)); //listen bør ikke indeholde task2
        assertFalse(result.contains(unrelatedTask)); //listen bør ikke indeholde unrelatedTask


    }

    @Test
    public void taskEndDateCalculationTest() {
        // Arrange
        LocalDate startDate = LocalDate.of(2025, 01, 21); // Startdato: Tirsdag 21. januar
        int estimatedDaysOfWork = 11; // 11 arbejdsdage

        // Forventet slutdato: Når man springer weekender over
        LocalDate expectedEndDate = LocalDate.of(2025, 02, 04); // Tirsdag 4. februar

        // Act
        LocalDate calculatedEndDate = projectService.calculateEndDateExcludingWeekends(startDate, estimatedDaysOfWork);
        System.out.println("Forventet slutdato: " + expectedEndDate);
        System.out.println("Beregnet slutdato: " + calculatedEndDate);

        // Assert
        assertEquals(expectedEndDate, calculatedEndDate); // Sammenlign forventet og beregnet dato

    }


}