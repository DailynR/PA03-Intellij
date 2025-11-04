package com.example.pa03.controller;

import com.example.demo.dto.TaskDTO;
import com.example.demo.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public ResponseEntity<List<ResourceDTO>> getAllTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer minPriority) {

        List<ResourceDTO> resources = resourceService.getAllResource(status, minPriority);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceDTO> getResourceById(@PathVariable Long id) {
        ResourceDTO resource = resourceService.getResourcesById(id);
        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity<ResourceDTO> createResource(@RequestBody ResourceDTO resourceDTO) {
        ResourceDTO createdResource = ResourceService.createResource(resourceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceDTO> updateResource(
            @PathVariable Long id,
            @RequestBody TaskDTO resourceDTO) {

        ResourceDTO updatedResource = resourceService.updateResource(id, resourceDTO);
        return ResponseEntity.ok(updatedResource);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResourceDTO> updateResourceStatus(
            @PathVariable Long id,
            @RequestParam String newStatus) {

        ResourceDTO updatedResource = resourceService.updateResourceStatus(id, newStatus);
        return ResponseEntity.ok(updatedResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }
}
