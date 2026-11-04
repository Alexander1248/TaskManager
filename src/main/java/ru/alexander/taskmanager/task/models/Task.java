package ru.alexander.taskmanager.task;

import java.time.Instant;

public record Task(Long id, String title, String description, boolean completed, Instant createdAt) {
}
