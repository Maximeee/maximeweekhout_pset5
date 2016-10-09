package com.maximeweekhout.todolist;

/**
 * Created by Maxime Weekhout on 02-10-16.
 */
public class ListItem {

    private int id;
    private String value;
    private boolean checked;

    ListItem(int id, String value, int checked) {
        this.id = id;
        this.value = value;
        this.checked = (checked == 1);
    }

    ListItem(int id, String value) {
        this.id = id;
        this.value = value;
        this.checked = false;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public boolean getChecked() {
        return checked;
    }
}
