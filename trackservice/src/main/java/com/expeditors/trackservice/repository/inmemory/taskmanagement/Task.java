package com.expeditors.trackservice.repository.inmemory.taskmanagement;

public interface Task {

    boolean process();
    boolean revert();
}
