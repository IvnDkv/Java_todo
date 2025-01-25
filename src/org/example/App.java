package org.example;

import java.util.Scanner;

import org.example.task.TaskService;

public class App {
  public static void main(String[] args) throws Exception {
    System.out.println("Hello, Todo!");

    Scanner sc = new Scanner(System.in);
    boolean isExit = false;
    String command;
    String payload;
    TaskService taskService = new TaskService();
    taskService.loadFromFile();

    while (true) {

      if (isExit) {
        break;
      }

      System.out.println("**********");
      taskService.printCommands();
      System.out.println("**********");
      command = sc.next();
      payload = sc.nextLine().trim();

      switch (command) {
        case "add":
          try {
            if (payload.isEmpty()) {
              throw new IllegalArgumentException("Нельзя добавить задачу без описания!");
            }
            if (payload.contains(";")) {
              throw new IllegalArgumentException("Нельзя использовать символ ';' в описании задачи!");
            }
            taskService.addTask(payload);
            System.out.println("Задача добавлена!");
            
          } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
          }
          break;
        case "done":
          try {
            int id = Integer.parseInt(payload);
            taskService.markTaskAsDone(id);
          } catch (NumberFormatException e) {
            System.out.println("Необходимо указать числовой id задачи!");
          }
          break;
        case "search":
          taskService.searchTasks(payload);
          break;
        case "list":
          taskService.listTasks();
          break;
        case "delete":
          taskService.deleteCompletedTasks();
          System.out.println("Все выполненные задачи удалены!");
          break;
        case "exit":
          isExit = true;
          break;
        default:
          System.out.println("Не существующая команда");
          break;
      }

    }
    taskService.saveToFile();
    System.out.println("Exit...");
    sc.close();
  }
}
