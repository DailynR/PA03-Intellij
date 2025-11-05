package com.example.pa03.controller;

import com.example.demo.dto.PaginatedResponse;
import com.example.demo.dto.TaskDTO;
import com.example.demo.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Get all tasks with pagination, sorting, and filtering
     *
     * @param status Filter by status (optional)
     * @param minPriority Filter by minimum priority (optional)
     * @param page Page number, 0-indexed (default: 0)
     * @param size Items per page (default: 10)
     * @param sortBy Field to sort by (default: id)
     * @param sortDir Sort direction: asc or desc (default: asc)
     * @return Paginated list of tasks
     */
    @GetMapping
    public ResponseEntity<PaginatedResponse<TaskDTO>> getAllTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer minPriority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        // Validate page size (prevent abuse)
        if (size > 100) {
            size = 100; // Max 100 items per page
        }
        if (size < 1) {
            size = 10; // Min 1 item per page
        }

        PaginatedResponse<TaskDTO> response = taskService.getAllTasks(
                status, minPriority, page, size, sortBy, sortDir
        );

        return ResponseEntity.ok(response);
    }

    // Keep all existing methods below

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable Long id,
            @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.updateTask(id, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(
            @PathVariable Long id,
            @RequestParam String newStatus) {
        TaskDTO updatedTask = taskService.updateTaskStatus(id, newStatus);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
