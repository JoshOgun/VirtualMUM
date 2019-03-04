package AllocationAlgorithm;

import java.util.Date;

public class Tester {
    /*
        This is just to quickly test adding tasks to the list and manipulating them using
        the taskHandler, this should be deleted soon as the front end can input tasks
    */
    public static void main(String args[]) {
        Tester t = new Tester();
        User u = new User();
        t.populateEvents(u);
        u.addTask("Task 1", 1, 3, new Date());
        u.addTask("Task 2", 2, 4, new Date());
        u.addTask("Task 3", 4, 3, new Date());
        u.addTask("Task 4", 5, 5, new Date());
        u.printTimetable();
    }

    private void populateEvents(User u){
        u.addEvent("AI Lab", new Date(119, 2, 18, 9, 0), new Date(119, 2, 18, 11, 0), "weekly");
        u.addEvent("Functional Lab", new Date(119, 2, 18, 11, 0), new Date(119, 2, 18, 12, 0), "weekly");
        u.addEvent("Analysis 1", new Date(119, 2, 18, 16, 0), new Date(119, 2, 18, 17, 0), "weekly");
        u.addEvent("Analysis 2", new Date(119, 2, 19, 10, 0), new Date(119, 2, 19, 11, 0), "weekly");
        u.addEvent("Functional 1", new Date(119, 2, 19, 14, 0), new Date(119, 2, 18, 15, 0), "weekly");
        u.addEvent("AI 1", new Date(119, 2, 19, 18, 0), new Date(119, 2, 19, 19, 0), "weekly");
        u.addEvent("Algebra 1", new Date(119, 2, 20, 10, 0), new Date(119, 2, 20, 11, 0), "weekly");
        u.addEvent("Analysis 3", new Date(119, 2, 20, 11, 0), new Date(119, 2, 20, 12, 0), "weekly");
        u.addEvent("Comparative 1", new Date(119, 2, 20, 12, 0), new Date(119, 2, 20, 13, 0), "weekly");
        u.addEvent("Comparative 2", new Date(119, 2, 21, 10, 0), new Date(119, 2, 21, 11, 0), "weekly");
        u.addEvent("Algebra 2", new Date(119, 2, 21, 11, 0), new Date(119, 2, 21, 12, 0), "weekly");
        u.addEvent("Algebra 3", new Date(119, 2, 21, 12, 0), new Date(119, 2, 21, 13, 0), "weekly");
        u.addEvent("IP Lab", new Date(119, 2, 21, 14, 0), new Date(119, 2, 18, 16, 0), "weekly");
        u.addEvent("Functional 2", new Date(119, 2, 21, 16, 0), new Date(119, 2, 21, 17, 0), "weekly");
        u.addEvent("Comparative 3", new Date(119, 2, 22, 11, 0), new Date(119, 2, 22, 12, 0), "weekly");
        u.addEvent("AI 2", new Date(119, 2, 22, 14, 0), new Date(119, 2, 22, 15, 0), "weekly");
        u.addEvent("AI Tut", new Date(119, 2, 22, 15, 0), new Date(119, 2, 22, 16, 0), "weekly");
    }

}