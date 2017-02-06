package is.uncommon.samples.todolist;

public interface ItemTouchHelperAdapter {

  void onItemMove(int fromPosition, int toPosition);

  void onItemDismiss(int position);
}
