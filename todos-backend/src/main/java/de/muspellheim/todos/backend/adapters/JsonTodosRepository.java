package de.muspellheim.todos.backend.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.ports.TodosRepository;
import de.muspellheim.todos.contract.ports.TodosRepositoryException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
    var json = readFile();
    var dto = parseJson(json);
    return mapTodos(dto);
  }

  private String readFile() {
    if (!Files.exists(file)) {
      // Return empty JSON.
      return "[]";
    }

    try {
      return Files.readString(file);
    } catch (IOException e) {
      throw new TodosRepositoryException("could not read file: " + file, e);
    }
  }

  private TodoDto[] parseJson(String json) {
    try {
      return new Gson().fromJson(json, TodoDto[].class);
    } catch (JsonSyntaxException e) {
      throw new TodosRepositoryException("not a valid JSON file: " + file, e);
    }
  }

  private List<Todo> mapTodos(TodoDto[] dto) {
    var todos = new ArrayList<Todo>();
    for (var i = 0; i < dto.length; i++) {
      TodoDto e = dto[i];
      try {
        var todo = new Todo(e.getId(), e.getTitle(), e.isCompleted());
        todos.add(todo);
      } catch (NullPointerException | IllegalArgumentException ex) {
        throw new TodosRepositoryException(
            "todo " + (i + 1) + " read from file " + file + " is not valid: " + e, ex);
      }
    }
    return todos;
  }

  @Override
  public void store(List<Todo> todos) throws TodosRepositoryException {
    var dto = createDto(todos);
    var json = createJson(dto);
    writeFile(json);
  }

  private static Object[] createDto(List<Todo> todos) {
    return todos.stream().map(e -> new TodoDto(e.id(), e.title(), e.completed())).toArray();
  }

  private static String createJson(Object[] dto) {
    var gson = new Gson();
    return gson.toJson(dto);
  }

  private void writeFile(String json) {
    try {
      Files.writeString(file, json);
    } catch (IOException e) {
      throw new TodosRepositoryException("could not write todos to file: " + file, e);
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TodoDto {
    public int id;
    public String title;
    public boolean completed;
  }
}
