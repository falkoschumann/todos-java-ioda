package de.muspellheim.todos.frontend;

@FunctionalInterface
public interface Delegate<T> {
  void delegate(T t);
}
