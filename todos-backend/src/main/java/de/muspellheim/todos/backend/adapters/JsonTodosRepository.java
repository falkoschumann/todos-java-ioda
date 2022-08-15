package de.muspellheim.todos.backend.adapters;

import com.google.gson.Gson;
import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.ports.TodosRepository;
import de.muspellheim.todos.contract.ports.TodosRepositoryException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class JsonTodosRepository implements TodosRepository {
  private final Path file;

  public JsonTodosRepository(Path file) {
    this.file = file;
  }

  @Override
  public List<Todo> load() throws TodosRepositoryException {
    if (!Files.exists(file)) {
      return List.of();
    }

    try {
      var gson = new Gson();
      var json = Files.readString(file);
      var dto = gson.fromJson(json, TodoDto[].class);
      return Arrays.stream(dto)
          .map(e -> new Todo(e.getId(), e.getTitle(), e.isCompleted()))
          .toList();
    } catch (IOException e) {
      throw new TodosRepositoryException("Can not read todos from file: " + file, e);
    }
  }

  @Override
  public void store(List<Todo> todos) throws TodosRepositoryException {
    try {
      var gson = new Gson();
      var dto = todos.stream().map(e -> new TodoDto(e.id(), e.title(), e.completed())).toArray();
      var json = gson.toJson(dto);
      Files.writeString(file, json);
    } catch (IOException e) {
      throw new TodosRepositoryException("Can not write todos to file: " + file, e);
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TodoDto {
    private int id;
    private String title;
    private boolean completed;
  }
}
