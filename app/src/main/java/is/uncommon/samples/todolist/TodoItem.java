package is.uncommon.samples.todolist;

public class TodoItem {

  private int index;
  private String task;
  private boolean isDone;
  private boolean isRemoved;

  private TodoItem() {

  }

  public static TodoItem create(int index, String task) {
    TodoItem item = new TodoItem();
    item.index(index);
    item.task(task);
    return item;
  }

  public void task(String task) {
    this.task = task;
  }

  public String task() {
    return task;
  }

  public void index(int index) {
    this.index = index;
  }

  public int index() {
    return index;
  }

  public boolean isDone() {
    return isDone;
  }

  public void isDone(boolean isDone) {
    this.isDone = isDone;
  }

  public boolean isRemoved() {
    return isRemoved;
  }

  public void isRemoved(boolean isRemoved) {
    this.isRemoved = isRemoved;
  }

  public int compare(TodoItem item) {

    if (!isRemoved()) {
      if (isDone() && item.isDone()) {
        return -1;
      } else if (isDone() != item.isDone()) {
        return isDone() ? 1 : -1;
      }
    }

    if (index() < item.index()) {
      return -1;
    } else if (index() > item.index()) {
      return 1;
    }
    return 0;
  }

  public boolean areContentsTheSame(TodoItem item) {
    return equals(item);
  }

  public boolean areItemsTheSame(TodoItem item) {
    return index() == item.index();
  }

  @Override public boolean equals(Object obj) {
    if (!(obj instanceof TodoItem)) {
      return false;
    }
    TodoItem item = (TodoItem) obj;
    return index() == item.index() && task().equals(item.task());
  }

  @Override public String toString() {
    return "Index : " + index() + " Task : " + task();
  }
}
