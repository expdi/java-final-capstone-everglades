package com.expeditors.trackservice.repository.taskmanagement;

public interface Task {

    boolean process();
    boolean revert();
}
