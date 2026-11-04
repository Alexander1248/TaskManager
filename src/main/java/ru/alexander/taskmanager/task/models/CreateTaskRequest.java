package ru.alexander.taskmanager.task;

/** Data accepted when a task is created. */
public record CreateTaskRequest(String title, String description) {
}
