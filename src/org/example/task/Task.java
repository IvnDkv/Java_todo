package org.example.task;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
  private int id;
  private String description;
  private boolean completed;
  private LocalDateTime dateTime;

  public Task(int id, String description, boolean completed, LocalDateTime dateTime) {
    this.id = id;
    this.description = description;
    this.completed = completed;
    this.dateTime = dateTime;
  }

  public int getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public String getDateTimeToString() {
    return dateTime.toString();
  }
  
  @Override
  public String toString() {
    String formattedDateTime = DateTimeFormatter.ofPattern("(дата: yyyy/MM/dd время: HH:mm:ss)")
      .format(dateTime);
    String completedToString = completed ? "Выполнена" : "Не выполнена";
    return "[%d] %s - %s %s".formatted(id, description, completedToString, formattedDateTime);
  }

}
