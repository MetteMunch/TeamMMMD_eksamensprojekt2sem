//package mmmd.teammmmd_eksamensprojekt2sem.controller;
//
//import mmmd.teammmmd_eksamensprojekt2sem.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//@WebMvcTest(UserController.class)
////Når vi skal teste i en Controller, så vil vi benytte en Mock (Mockito) for at lave en simuleret version
////af en afhængighed (Mocking), i dette tilfælde Service klassen. På den måde kan vi "kontrollere" den falske version af
//// Service objektet... hvordan den skal opføre sig, og hvad den skal returnere (Stubbing), uden at køre
//// Service klassens egentlige kode.
//
//
//public class UserControllerTest {
//
//    @Autowired // med denne annotation fortæller vi Spring, at den automatisk skal indsætte (injecte) en instans
//    //af denne afhængighed. Dvs vi skal ikke oprette instansen manuelt med new. Spring håndterer instansieringen.
//    private MockMvc mockMvc;
//
//    @MockBean //med denne annotation instruere vi Spring Boot i at oprette en Mock-version af UserService, som
//    //vi kan manipulere med under testen
//    private UserService userService;
//
//
//    @BeforeEach
//    public void setup() {
//    }
//
//
//}
