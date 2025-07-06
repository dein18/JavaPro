package ru.dse.java_pro.task2;

import java.util.*;
import java.util.stream.Collectors;

import static ru.dse.java_pro.task2.Position.*;

public class Main {
    public static void main(String[] args) {
        Employee employee1 = new Employee("Иван", 30, ENGINEER);
        Employee employee2 = new Employee("Петр", 35, ENGINEER);
        Employee employee3 = new Employee("Виктор", 28, ENGINEER);
        Employee employee4 = new Employee("Николай", 48, ENGINEER);
        Employee employee5 = new Employee("Алексей", 23, ENGINEER);
        Employee employee6 = new Employee("Василий", 45, DRIVER);
        Employee employee7 = new Employee("Ольга", 52, COOK);

        List<Employee> employees = Arrays.asList(employee1, employee2, employee3, employee4, employee5, employee6, employee7);

        // 1) список имен 3 самых старших сотрудников с должностью «Инженер», в порядке убывания возраста
        List<String> names = employees.stream()
                .filter(employee -> employee.getPosition().equals(ENGINEER))
                .sorted(Comparator.comparing(Employee::getAge).reversed())
                .limit(3)
                .map(Employee::getName)
                .toList();
        System.out.println("1) - " + names);

        // 2) Имеется список объектов типа Сотрудник (имя, возраст, должность), посчитайте средний возраст
        // сотрудников с должностью «Инженер»
        double age = employees.stream()
                .filter(employee -> employee.getPosition().equals(ENGINEER))
                .mapToInt(Employee::getAge)
                .average()
                .orElse(0.0);
        System.out.println("2) - " + age);


        // 3) Найдите в списке слов самое длинное
        List<String> stringList = List.of("dog", "house", "language", "breakfast");

        String maxLenght = stringList.stream()
                .max(Comparator.comparingInt(String::length))
                .get();

        System.out.println("3) - " + maxLenght);


        //  4) Имеется строка с набором слов в нижнем регистре, разделенных пробелом. Постройте хеш-мапы,
        // в которой будут храниться пары: слово - сколько раз оно встречается во входной строке
        String s = "hot cat cat cat cat dog dog dog air air air air air air";

        Map<String, Long> hashMap = Arrays.stream(s.split(" "))
                .collect(Collectors.groupingBy(str -> str, Collectors.counting()));

        System.out.println("4) - " + hashMap);


        // 5) Отпечатайте в консоль строки из списка в порядке увеличения длины слова, если слова имеют одинаковую длины,
        // то должен быть сохранен алфавитный порядок
        List<String> sortedList = stringList.stream()
                .sorted(Comparator.comparingInt(String::length)
                        .thenComparing(Comparator.naturalOrder()))
                .toList();
        System.out.println("5) - " + sortedList);


        // 6) Имеется массив строк, в каждой из которых лежит набор из 5 слов, разделенных пробелом,
        // найдите среди всех слов самое длинное, если таких слов несколько, получите любое из них
        String[] arr = {
                "cat dog bird fish mouse",
                "apple orange banana grape peach",
                "car bus train plane truck",
                "red blue green yellow white",
                "house tree river mountain sky"
        };
        String maxL = Arrays.stream(arr)
                .flatMap(r -> Arrays.stream(r.split(" ")))
                .max(Comparator.comparingInt(String::length))
                .orElse("");
        System.out.println("6) - " + maxL);
    }
}
