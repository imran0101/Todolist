package is.uncommon.samples.todolist;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import timber.log.Timber;

public class TodoAdapter extends RecyclerView.Adapter<TodoItemHolder>
    implements ItemTouchHelperAdapter, AdapterItemCallback {

  TodoList todoList = new TodoList(this);

  private String[] todoArray = new String[] {
      "Send that Package", "Call dad", "Finish design work", "Pick up laundry",
      "Pay electricity bill", "Book flight tickets"
  };

  public TodoAdapter() {
    todoList.addAll(todoArray);
  }

  @Override public TodoItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return TodoItemHolder.create(parent);
  }

  @Override public void onBindViewHolder(TodoItemHolder holder, int position) {
    holder.bind(todoList.get(position), this);
  }

  @Override public int getItemCount() {
    return todoList.size();
  }

  @Override public void onItemMove(int fromPosition, int toPosition) {
    //move items in the list.
    Timber.d("onItemMove: %s %s", fromPosition, toPosition);
    todoList.items().get(fromPosition).index(toPosition);
    todoList.items().get(toPosition).index(fromPosition);
    todoList.items().recalculatePositionOfItemAt(fromPosition);
    todoList.items().recalculatePositionOfItemAt(toPosition);
  }

  @Override public void onItemDismiss(int position) {

  }

  @Override public void remove(TodoItem item) {
    if (item == null) {
      return;
    }
    item.isRemoved(true);
    todoList.remove(item);
  }

  @Override public void done(int position) {

    if (position == RecyclerView.NO_POSITION) {
      return;
    }
    todoList.items().get(position).isDone(true);
    todoList.items().recalculatePositionOfItemAt(position);
    todoList.items().get(todoList.size() - 1).index(todoList.size() - 1);
  }

  public void reset() {
    todoList.items().clear();
    todoList.addAll(todoArray);
  }
}