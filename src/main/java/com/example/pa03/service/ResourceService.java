package com.example.pa03.service;

import com.example.demo.dto.TaskDTO;
import com.example.demo.exception.InvalidTaskStatusException;
import com.example.demo.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
        import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ResourceService  {

    // In-memory storage (would be a database in production)
    private final Map<Long, ResourceDTO> resources = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    private static final Set<String> VALID_STATUSES =
            Set.of("PENDING", "IN_PROGRESS", "COMPLETED");

    public List<ResourceDTO> getAllResources(String status, Integer minPriority) {
        List<ResourceDTO> result = new ArrayList<>(resources.values());

        if (status != null) {
            validateStatus(status);
            result = result.stream()
                    .filter(resource -> resource.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }

        if (minPriority != null) {
            result = result.stream()
                    .filter(resource -> resource.getPriority() >= minPriority)
                    .collect(Collectors.toList());
        }

        return result;
    }

    public ResourceDTO getResourceById(Long id) {
        ResourceDTO resource = resources.get(id);
        if (resource == null) {
            throw new ResourceNotFoundException(id);
        }
        return resource;
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        if (resourceDTO.getStatus() != null) {
            validateStatus(resourceDTO.getStatus());
        }

        if (resourceDTO.getStatus() == null) {
            resourceDTO.setStatus("PENDING");
        }
        if (resourceDTO.getPriority() == null) {
            resourceDTO.setPriority(1);
        }

        Long id = idCounter.getAndIncrement();
        resourceDTO.setId(id);
        resources.put(id, resourceDTO);

        return resourceDTO;
    }

    public ResourceDTO updateResource(Long id, ResourceDTO resourceDTO) {
        if (!resources.containsKey(id)) {
            throw new ResourceNotFoundException(id);
        }

        if (resourceDTO.getStatus() != null) {
            validateStatus(resourceDTO.getStatus());
        }

        resourceDTO.setId(id);
        resources.put(id, resourceDTO);

        return resourceDTO;
    }

    public ResourceDTO updateResourceStatus(Long id, String newStatus) {
        ResourceDTO task = getResourceById(id);
        validateStatus(newStatus);
        resource.setStatus(newStatus.toUpperCase());
        return resource;
    }

    public void deleteResource(Long id) {
        if (!resources.containsKey(id)) {
            throw new ResourceNotFoundException(id);
        }
        resources.remove(id);
    }

    private void validateStatus(String status) {
        if (!VALID_STATUSES.contains(status.toUpperCase())) {
            throw new InvalidResourceStatusException(status);
        }
    }
}
