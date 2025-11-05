package com.example.pa03.service;

import com.example.demo.dto.PaginatedResponse;
import com.example.demo.dto.TaskDTO;
import com.example.demo.exception.InvalidTaskStatusException;
import com.example.demo.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ResourceService {
    private final Map<Long, TaskDTO> tasks = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private static final Set<String> VALID_STATUSES =
            Set.of("PENDING", "IN_PROGRESS", "COMPLETED");

    /**
     * Get all tasks with optional filtering, pagination, and sorting
     *
     * @param status Filter by status (optional)
     * @param minPriority Filter by minimum priority (optional)
     * @param page Page number (0-indexed)
     * @param size Items per page
     * @param sortBy Field to sort by (id, title, priority, status)
     * @param sortDir Sort direction (asc or desc)
     * @return Paginated response with tasks
     */
    public PaginatedResponse<TaskDTO> getAllTasks(
            String status,
            Integer minPriority,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        // Step 1: Filter tasks
        List<TaskDTO> filteredTasks = new ArrayList<>(tasks.values());

        if (status != null) {
            validateStatus(status);
            filteredTasks = filteredTasks.stream()
                    .filter(task -> task.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }

        if (minPriority != null) {
            filteredTasks = filteredTasks.stream()
                    .filter(task -> task.getPriority() >= minPriority)
                    .collect(Collectors.toList());
        }

        // Step 2: Sort tasks
        Comparator<TaskDTO> comparator = getComparator(sortBy);
        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }
        filteredTasks.sort(comparator);

        // Step 3: Calculate pagination
        long totalElements = filteredTasks.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        // Validate page number
        if (page < 0) {
            page = 0;
        }
        if (page >= totalPages && totalElements > 0) {
            page = totalPages - 1;
        }

        // Step 4: Extract page content
        int start = page * size;
        int end = Math.min(start + size, filteredTasks.size());

        List<TaskDTO> pageContent = (start < filteredTasks.size())
                ? filteredTasks.subList(start, end)
                : new ArrayList<>();

        // Step 5: Build response
        return new PaginatedResponse<>(pageContent, page, size, totalElements);
    }

    /**
     * Get comparator based on sort field
     */
    private Comparator<TaskDTO> getComparator(String sortBy) {
        if (sortBy == null) {
            sortBy = "id"; // Default sort
        }

        switch (sortBy.toLowerCase()) {
            case "title":
                return Comparator.comparing(TaskDTO::getTitle,
                        String.CASE_INSENSITIVE_ORDER);
            case "priority":
                return Comparator.comparing(TaskDTO::getPriority);
            case "status":
                return Comparator.comparing(TaskDTO::getStatus,
                        String.CASE_INSENSITIVE_ORDER);
            case "id":
            default:
                return Comparator.comparing(TaskDTO::getId);
        }
    }

    // Keep existing methods below (getTaskById, createTask, etc.)

    public TaskDTO getTaskById(Long id) {
        TaskDTO task = tasks.get(id);
        if (task == null) {
            throw new TaskNotFoundException(id);
        }
        return task;
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        if (taskDTO.getStatus() != null) {
            validateStatus(taskDTO.getStatus());
        }
        if (taskDTO.getStatus() == null) {
            taskDTO.setStatus("PENDING");
        }
        if (taskDTO.getPriority() == null) {
            taskDTO.setPriority(1);
        }
        Long id = idCounter.getAndIncrement();
        taskDTO.setId(id);
        tasks.put(id, taskDTO);
        return taskDTO;
    }

    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        if (!tasks.containsKey(id)) {
            throw new TaskNotFoundException(id);
        }
        if (taskDTO.getStatus() != null) {
            validateStatus(taskDTO.getStatus());
        }
        taskDTO.setId(id);
        tasks.put(id, taskDTO);
        return taskDTO;
    }

    public TaskDTO updateTaskStatus(Long id, String newStatus) {
        TaskDTO task = getTaskById(id);
        validateStatus(newStatus);
        task.setStatus(newStatus.toUpperCase());
        return task;
    }

    public void deleteTask(Long id) {
        if (!tasks.containsKey(id)) {
            throw new TaskNotFoundException(id);
        }
        tasks.remove(id);
    }

    private void validateStatus(String status) {
        if (!VALID_STATUSES.contains(status.toUpperCase())) {
            throw new InvalidTaskStatusException(status);
        }
    }
}
