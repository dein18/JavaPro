package ru.dse.java_pro.task1;

import ru.dse.java_pro.task1.annotation.*;

public class MyTests {

    @BeforeSuite
    public static void initAll() {
        System.out.println("--- Before Suite ---");
    }
    @AfterSuite
    public static void tearDownAll() {
        System.out.println("--- After Suite ---");
    }

    @BeforeTest
    public void beforeEach() {
        System.out.println("--> Before Test");
    }

    @AfterTest
    public void afterEach() {
        System.out.println("<-- After Test");
    }

    @Test(priority = 9)
    public void highPriorityTest() {
        System.out.println("Running high priority test");
    }

    @Test
    public void defaultPriorityTest() {
        System.out.println("Running default priority test");
    }

    @Test(priority = 3)
    @CsvSource("10, Java, 20, true")
    public void testWithCsv(int a, String b, int c, boolean d) {
        System.out.printf("CSV Test: a=%d, b=%s, c=%d, d=%b%n", a, b, c, d);
    }

    public static void main(String[] args) throws Exception {
        TestRunner.runTests(MyTests.class);
    }
}