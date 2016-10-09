package com.maximeweekhout.todolist;

/**
 * Created by Maxime on 3-10-2016.
 */
public class ToDoManager {

    private static ToDoManager ourInstance = new ToDoManager();

    public static ToDoManager getInstance() {
        return ourInstance;
    }

    private ToDoManager() {

    }
}
