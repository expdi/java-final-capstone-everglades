package com.expeditors.trackservice.repository.inmemory.taskmanagement;

public interface TaskManager {

    void addTask(Task task);
    boolean processTasks();
}


