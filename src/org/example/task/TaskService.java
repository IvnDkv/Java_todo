package org.example.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskService {

  private List<Task> taskList;
  private int id;
  private final String fieldSeparator;
  private final String path;
  private final List<String> commands;

  public TaskService() {
    this.taskList = new ArrayList<>();
    this.id = 0;
    this.fieldSeparator = ";";
    this.path = "./tasks.txt";
    this.commands = List.of(
      "add <описание> — добавить новую задачу",
      "list — вывести список всех задач",
      "done <id> — отметить задачу с указанным идентификатором как выполненную",
      "delete — удалить все выполненные задачи",
      "search <текст> — найти задачи, содержащие указанный текст",
      "exit — завершение работы"
    );
  }

  private void setId(int id) {
    this.id = id;
  }

  public void saveToFile() {
    StringBuilder sb = new StringBuilder();
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
      for (Task task : taskList) {
        sb.append(task.getId())
          .append(fieldSeparator)
          .append(task.getDescription())
          .append(fieldSeparator)
          .append(task.isCompleted())
          .append(fieldSeparator)
          .append(task.getDateTimeToString());
        bw.write(sb.toString());
        bw.newLine();
        sb.setLength(0);
      }
    } catch (IOException e) {
      System.out.println("Ошибка записи списка дел в файл");
    }
  }

  public void loadFromFile() {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      String line;
      int loadedId = 0;
      while ((line = br.readLine()) != null) {
        String[] fields = line.split(fieldSeparator);
        loadedId = Integer.parseInt(fields[0]);
        taskList.add(
          new Task(
            loadedId,
            fields[1],
            Boolean.valueOf(fields[2]),
            LocalDateTime.parse(fields[3])
          )
        );
      }
      setId(loadedId);
    } catch (FileNotFoundException e) {
      createFile();
    } catch (IOException e) {
      System.out.println("Ошибка чтения файла со списком дел");
    }
  }

  public void createFile() {
    try {
      File file = new File(path);
      if (file.createNewFile()) {
        System.out.println("Файл для хранения списка дел создан");
      }
    } catch (IOException e) {
      System.out.println("Ошибка при создании файла");
    }
  }

  public void addTask(String description) {
    id++;
    taskList.add(new Task(id, description, false, LocalDateTime.now()));
  }

  public void listTasks() {
    taskList.stream().forEach(System.out::println);
  }

  public void printCommands() {
    commands.stream().forEach(System.out::println);
  }

  public void markTaskAsDone(int id) {
    try {
      Task task = taskList.stream().filter(t -> t.getId() == id).findFirst()
      .orElseThrow(() -> new IllegalArgumentException("Нет задачи с id=%d".formatted(id)));
      task.setCompleted(true);
      System.out.println("Задача с id=%d отмечена как выполненная!".formatted(id));
    } catch(IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  public void deleteCompletedTasks() {
    taskList = taskList.stream().filter(t -> !t.isCompleted()).toList();
  }

  public void searchTasks(String keyword) {
      List<Task> tasks =  taskList.stream().filter(t -> t.getDescription().contains(keyword)).toList();
      if (!tasks.isEmpty()) {
        System.out.println("По запросу '%s' найдены задачи:".formatted(keyword));
        tasks.stream().forEach(System.out::println);
      } else {
        System.out.println("По запросу '%s' не найдены задачи".formatted(keyword));
      }
  }

}
