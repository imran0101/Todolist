package is.uncommon.samples.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TodoActivity extends AppCompatActivity {

  @BindView(R.id.recycler_todo) RecyclerView recyclerTodo;
  @BindView(R.id.img_reset) ImageView imgReset;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_todo);

    ButterKnife.bind(this);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    setTitle("Today");
    final TodoAdapter adapter = new TodoAdapter();

    imgReset.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        adapter.reset();
      }
    });

    LinearLayoutManager manager =
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    recyclerTodo.setLayoutManager(manager);
    recyclerTodo.setAdapter(adapter);

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TodoItemTouchCallback(adapter));
    itemTouchHelper.attachToRecyclerView(recyclerTodo);
    recyclerTodo.setItemAnimator(new TodoItemAnimator());
  }
}
