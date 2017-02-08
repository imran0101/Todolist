package is.uncommon.samples.todolist;

public interface TodoItemAdapterCallback {
  void remove(int position, TodoItem item);

  void done(int position, TodoItem item);
}
