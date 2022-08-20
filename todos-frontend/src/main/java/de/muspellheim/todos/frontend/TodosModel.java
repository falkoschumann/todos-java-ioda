package de.muspellheim.todos.frontend;

import de.muspellheim.todos.contract.data.Todo;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

class TodosModel {
  private final EventListenerList listenerList = new EventListenerList();

  private List<Todo> activeTodos;
  private List<Todo> completedTodos;
  private List<Todo> allTodos = List.of();
  private Filter filter = Filter.ALL;

  boolean isExistsTodos() {
    return !allTodos.isEmpty();
  }

  List<Todo> shownTodos() {
    return switch (filter) {
      case ACTIVE -> activeTodos;
      case COMPLETED -> completedTodos;
      default -> allTodos;
    };
  }

  boolean isAllCompleted() {
    return !allTodos.isEmpty() && allTodos.size() == completedTodos.size();
  }

  int getActiveCount() {
    return activeTodos.size();
  }

  boolean isExistsCompleted() {
    return !completedTodos.isEmpty();
  }

  void setTodos(List<Todo> value) {
    activeTodos = value.stream().filter(t -> !t.completed()).toList();
    completedTodos = value.stream().filter(Todo::completed).toList();
    allTodos = List.copyOf(value);
    fireStateChanged();
  }

  void setFilter(Filter value) {
    filter = value;
    fireStateChanged();
  }

  void addChangeListener(ChangeListener l) {
    listenerList.add(ChangeListener.class, l);
  }

  void removeChangeListener(ChangeListener l) {
    listenerList.remove(ChangeListener.class, l);
  }

  protected void fireStateChanged() {
    var e = new ChangeEvent(this);
    for (var l : listenerList.getListeners(ChangeListener.class)) {
      l.stateChanged(e);
    }
  }
}
