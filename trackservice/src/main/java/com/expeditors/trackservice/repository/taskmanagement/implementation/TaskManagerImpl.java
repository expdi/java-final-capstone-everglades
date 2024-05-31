package com.expeditors.trackservice.repository.taskmanagement.implementation;

import com.expeditors.trackservice.repository.taskmanagement.Task;
import com.expeditors.trackservice.repository.taskmanagement.TaskManager;
import com.expeditors.trackservice.repository.taskmanagement.TaskManagerRollbackException;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TaskManagerImpl implements TaskManager {

    private final Queue<Task> taskToPerform;
    private final Queue<Task> completedTasks;

    public TaskManagerImpl(
            Queue<Task> taskToPerform,
            Queue<Task> completedTasks) {

        this.taskToPerform = taskToPerform;
        this.completedTasks = completedTasks;
    }

    public boolean processTasks(){
        boolean isSuccessfuly = true;

        completedTasks.clear();
        while (!taskToPerform.isEmpty()){

            var eTask = taskToPerform.poll();
            isSuccessfuly =  eTask.process();

            if(!isSuccessfuly){
                break;
            }

            completedTasks.add(eTask);
        }

        if(isSuccessfuly){
            completedTasks.clear();
            return true;
        }

        taskToPerform.clear();
        while (!completedTasks.isEmpty()){

            var eTask = completedTasks.poll();
            isSuccessfuly =  eTask.revert();

            if(!isSuccessfuly){
                throw new TaskManagerRollbackException("Not possible rollback");
            }
        }
        return false;
    }


    public void addTask(Task task){
        taskToPerform.add(task);
    }
}
