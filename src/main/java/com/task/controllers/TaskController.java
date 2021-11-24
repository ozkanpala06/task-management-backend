package com.task.controllers;

import com.task.dto.CountType;
import com.task.model.Task;
import com.task.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
@AllArgsConstructor
public class TaskController {
    private TaskService taskService;

    @GetMapping("/task/vData/percentcounttype")
    public List<CountType> getPercentageGroupByType() {
        return taskService.getPercentageGroupByType();
    }

    @GetMapping("/task")
    public List<Task> getTask() {
        return taskService.getTasks();
    }

    @PostMapping("/task")
    public Task save(@RequestBody Task task) {
        return taskService.save(task);
    }

    @GetMapping("/task/{id}")
    public Task getById(@PathVariable Long id) {
        return taskService.getTaskById(id).orElseThrow(() -> new EntityNotFoundException("Requested Task not found"));
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<?> updateTask(@RequestBody Task taskPara,@PathVariable Long id) {
        if(taskService.existById(id)) {
            Task task = taskService.getTaskById(id).orElseThrow(() -> new EntityNotFoundException("Requested Task not found"));
            task.setTitle(taskPara.getTitle());
            task.setDueDate(taskPara.getDueDate());
            task.setType(taskPara.getType());
            task.setDescription(taskPara.getDescription());

            taskService.save(task);
            return ResponseEntity.ok().body(task);
        }
        else {
            HashMap<String,String> message = new HashMap<>();
            message.put("message",id + " task not found or matced");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        if(taskService.existById(id)) {
            taskService.delete(id);
            HashMap<String,String> message = new HashMap<>();
            message.put("message",id + " task removed");
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
        else {
            HashMap<String,String> message = new HashMap<>();
            message.put("message",id + " task not found or matced");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }
}
