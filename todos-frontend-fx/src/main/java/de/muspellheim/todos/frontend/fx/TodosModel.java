package de.muspellheim.todos.frontend.fx;

import de.muspellheim.todos.contract.data.Todo;
import java.util.List;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

class TodosModel {
  private final ReadOnlyBooleanWrapper existsTodos = new ReadOnlyBooleanWrapper();
  private final ObservableList<Todo> shownTodos = FXCollections.observableArrayList();
  private final ReadOnlyBooleanWrapper allCompleted = new ReadOnlyBooleanWrapper();
  private final ReadOnlyIntegerWrapper activeCount = new ReadOnlyIntegerWrapper();
  private final ReadOnlyBooleanWrapper existsCompleted = new ReadOnlyBooleanWrapper();

  private List<Todo> allTodos = List.of();
  private Filter filter = Filter.ALL;

  final ReadOnlyBooleanProperty existsTodosProperty() {
    return existsTodos.getReadOnlyProperty();
  }

  final boolean isExistsTodos() {
    return existsTodos.get();
  }

  final ObservableList<Todo> getShownTodos() {
    return shownTodos;
  }

  final ReadOnlyBooleanProperty allCompletedProperty() {
    return allCompleted.getReadOnlyProperty();
  }

  final boolean isAllCompleted() {
    return allCompleted.get();
  }

  final ReadOnlyIntegerProperty activeCountProperty() {
    return activeCount.getReadOnlyProperty();
  }

  final int getActiveCount() {
    return activeCount.get();
  }

  final ReadOnlyBooleanProperty existsCompletedProperty() {
    return existsCompleted.getReadOnlyProperty();
  }

  final boolean isExistsCompleted() {
    return existsCompleted.get();
  }

  final void setTodos(List<Todo> value) {
    allTodos = List.copyOf(value);
    updateState();
  }

  final void setFilter(Filter value) {
    filter = value;
    updateState();
  }

  private void updateState() {
    var activeTodos = allTodos.stream().filter(t -> !t.completed()).toList();
    var completedTodos = allTodos.stream().filter(Todo::completed).toList();
    existsTodos.set(!allTodos.isEmpty());
    shownTodos.setAll(
        switch (filter) {
          case ACTIVE -> activeTodos;
          case COMPLETED -> completedTodos;
          default -> allTodos;
        });
    allCompleted.set(!allTodos.isEmpty() && allTodos.size() == completedTodos.size());
    activeCount.set(activeTodos.size());
    existsCompleted.set(!completedTodos.isEmpty());
  }
}
