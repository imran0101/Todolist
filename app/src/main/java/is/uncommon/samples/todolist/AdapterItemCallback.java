package is.uncommon.samples.todolist;

public interface AdapterItemCallback {
  void remove(TodoItem item);

  void done(int position);
}
