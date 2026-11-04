package ru.alexander.taskmanager.task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class TaskRepository {
    private static final RowMapper<Task> TASK_ROW_MAPPER = TaskRepository::mapTask;

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Task save(String title, String description, Instant createdAt) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var statement = connection.prepareStatement(
                    "INSERT INTO tasks (title, description, completed, created_at) VALUES (?, ?, ?, ?)",
                    new String[]{"id"});
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setBoolean(3, false);
            statement.setTimestamp(4, Timestamp.from(createdAt));
            return statement;
        }, keyHolder);
        Number id = keyHolder.getKey();
        if (id == null) {
            throw new IllegalStateException("Database did not return an id for the new task");
        }
        return new Task(id.longValue(), title, description, false, createdAt);
    }

    public List<Task> findAll(Boolean completed) {
        if (completed == null) {
            return jdbcTemplate.query("SELECT * FROM tasks ORDER BY id", TASK_ROW_MAPPER);
        }
        return jdbcTemplate.query("SELECT * FROM tasks WHERE completed = ? ORDER BY id", TASK_ROW_MAPPER, completed);
    }

    public Optional<Task> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM tasks WHERE id = ?", TASK_ROW_MAPPER, id).stream().findFirst();
    }

    public boolean update(Task task) {
        return jdbcTemplate.update("UPDATE tasks SET title = ?, description = ?, completed = ? WHERE id = ?",
                task.title(), task.description(), task.completed(), task.id()) == 1;
    }

    public boolean deleteById(long id) {
        return jdbcTemplate.update("DELETE FROM tasks WHERE id = ?", id) == 1;
    }

    private static Task mapTask(ResultSet resultSet, int rowNum) throws SQLException {
        return new Task(
                resultSet.getLong("id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getBoolean("completed"),
                resultSet.getTimestamp("created_at").toInstant());
    }
}
