package ru.alexander.taskmanager.task;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task create(CreateTaskRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body must not be empty");
        }
        validateRequiredFields(request.title(), request.description());
        return repository.save(request.title().trim(), request.description().trim(), Instant.now());
    }

    public List<Task> findAll(Boolean completed) {
        return repository.findAll(completed);
    }

    public Task findById(long id) {
        return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task update(long id, UpdateTaskRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body must not be empty");
        }
        Task existing = findById(id);
        String title = request.title() == null ? existing.title() : request.title().trim();
        String description = request.description() == null ? existing.description() : request.description().trim();
        boolean completed = request.completed() == null ? existing.completed() : request.completed();
        validateRequiredFields(title, description);
        Task updated = new Task(id, title, description, completed, existing.createdAt());
        repository.update(updated);
        return updated;
    }

    public void delete(long id) {
        if (!repository.deleteById(id)) {
            throw new TaskNotFoundException(id);
        }
    }

    private void validateRequiredFields(String title, String description) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description must not be blank");
        }
        if (title.length() > 255) {
            throw new IllegalArgumentException("Title must be at most 255 characters");
        }
        if (description.length() > 2000) {
            throw new IllegalArgumentException("Description must be at most 2000 characters");
        }
    }
}
