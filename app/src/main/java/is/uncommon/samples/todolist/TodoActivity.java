package is.uncommon.samples.todolist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class TodoActivity extends AppCompatActivity {

  TodoAdapter adapter;
  TodoItemTouchCallback todoItemTouchCallback;

  @BindView(R.id.recycler_todo) RecyclerView recyclerTodo;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_todo);

    Timber.plant(new Timber.DebugTree());

    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    setTitle("Today");
    toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
    toolbar.setTitleTextColor(Color.BLACK);
    toolbar.setBackgroundColor(Color.WHITE);

    setupRecyclerView();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_todo, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == R.id.menu_refresh) {
      //reset adapter.
      resetRecyclerView();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Setup recyclerView.
   */
  private void setupRecyclerView() {
    LinearLayoutManager manager =
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    recyclerTodo.setLayoutManager(manager);
    adapter = new TodoAdapter();
    todoItemTouchCallback = new TodoItemTouchCallback(adapter);
    recyclerTodo.setAdapter(adapter);

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(todoItemTouchCallback);
    itemTouchHelper.attachToRecyclerView(recyclerTodo);
    recyclerTodo.setItemAnimator(new TodoItemAnimator());
  }

  /**
   * Reset recyclerView.
   */
  private void resetRecyclerView() {
    adapter = new TodoAdapter();
    todoItemTouchCallback.adapter(adapter);
    recyclerTodo.setAdapter(adapter);
  }
}
