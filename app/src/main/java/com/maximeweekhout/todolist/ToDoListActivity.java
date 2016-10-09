package com.maximeweekhout.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.OnItemLongClickListener;

public class ToDoListActivity extends AppCompatActivity {

    private EditText AddItem;
    private ListView ToDolist;

    private DBHelper database;

    Context context;

    List<ListItem> dataList = new ArrayList<ListItem>();
    List<String> dataStringList = new ArrayList<String>();
    ListAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        context = getApplicationContext();

        int id = getIntent().getExtras().getInt("titleid", 99999);
        String title = getIntent().getExtras().getString("listTitle");
        if (title != null) {
            database = new DBHelper(context, title);
        } else if (id != 99999) {
            System.out.println("Hello "+ id);
            database = new DBHelper(context, id);
        }

        AddItem = (EditText) findViewById(R.id.editText);
        ToDolist = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ListAdapter(this, dataList);

        ToDolist.setAdapter(arrayAdapter);

        updateDataList();

        ToDolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                database.update(dataList.get(position).getId(), !dataList.get(position).getChecked());

                updateDataList();
            }
        });

        ToDolist.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

                database.remove(dataList.get(pos).getId());
                updateDataList();

                Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

    public void OnButtonClicked(View view) {

        String content = AddItem.getText().toString();

        if (!content.equals("")) {

            database.add(content);

            updateDataList();

            AddItem.setText("");

        } else {
            Toast.makeText(context, "Please provide input", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDataList() {

        dataList.clear();
        dataList.addAll(database.read());

        dataStringList.clear();
        for (ListItem item : dataList) {
            dataStringList.add(item.getValue());
        }
        arrayAdapter.notifyDataSetChanged();
    }
}
