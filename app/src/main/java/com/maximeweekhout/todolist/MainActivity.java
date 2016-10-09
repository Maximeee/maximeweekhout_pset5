package com.maximeweekhout.todolist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBHelper database;
    private ListView ToDolist;
    Context context;

    List<ListItem> dataList = new ArrayList<ListItem>();
    List<String> dataStringList = new ArrayList<String>();
    ListAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.context = getApplicationContext();

        database = new DBHelper(context);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogfragment dialog = new dialogfragment();
                dialog.show(getSupportFragmentManager(), "Name your to do list");
            }
        });

        ToDolist = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ListAdapter(this, dataList);

        ToDolist.setAdapter(arrayAdapter);

        updateDataList();

        ToDolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ToDoListActivity.class);

                int listid = dataList.get(position).getId();
                intent.putExtra("titleid", listid);
                startActivity(intent);
            }
        });

        ToDolist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                database.removeList(dataList.get(position).getId());
                updateDataList();

                Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        updateDataList();
    }

    public static class dialogfragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            final LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialogfragment, null));

            builder.setMessage(R.string.pop_up_dialog)
                    .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            EditText editText = (EditText) getDialog().findViewById(R.id.todolisttitle);

                            // edittext != leeg

                            Intent intent  = new Intent(getContext(), ToDoListActivity.class);
                            intent.putExtra("listTitle", editText.getText().toString());
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    private void updateDataList() {

        dataList.clear();
        dataList.addAll(database.getLists());

        dataStringList.clear();
        for (ListItem item : dataList) {
            dataStringList.add(item.getValue());
        }
        arrayAdapter.notifyDataSetChanged();
    }
}
