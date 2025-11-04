// src/main/java/edu/famu/cop3060/resources/store/InMemoryResourceStore.java
package edu.famu.cop3060.resources.store;

import edu.famu.cop3060.resources.dto.ResourceDTO;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.*;

@Component
public class InMemoryResourceStore {

    private final Map<String, ResourceDTO> byId = new HashMap<>();
    private final List<ResourceDTO> all = new ArrayList<>();

    @PostConstruct
    public void init() {
        // Seed 6 example resources (use List.of for immutable tags)
        add(new ResourceDTO("r1", "Math Tutoring Center", "Tutoring", "Building A, Rm 101", "http://example.edu/tutoring/math", List.of("math","tutoring","undergrad")));
        add(new ResourceDTO("r2", "Writing Lab", "Tutoring", "Library 2nd Floor", "http://example.edu/tutoring/writing", List.of("writing","editing")));
        add(new ResourceDTO("r3", "Computer Lab", "Lab", "Engineering Bldg Rm 210", "http://example.edu/labs/computer", List.of("computers","linux","windows")));
        add(new ResourceDTO("r4", "Career Advising", "Advising", "Student Center", "http://example.edu/advising/career", List.of("career","resume")));
        add(new ResourceDTO("r5", "Counseling Center", "Advising", "Health Center", "http://example.edu/advising/counsel", List.of("mental-health","support")));
        add(new ResourceDTO("r6", "Physics Help Desk", "Tutoring", "Science Bldg", "http://example.edu/tutoring/physics", List.of("physics","lab-help")));
    }

    private void add(ResourceDTO r) {
        byId.put(r.id(), r);
        all.add(r);
    }

    public List<ResourceDTO> findAll() {
        return List.copyOf(all);
    }

    public Optional<ResourceDTO> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    public List<ResourceDTO> findByFilters(Optional<String> categoryOpt, Optional<String> qOpt) {
        return all.stream()
                .filter(r -> categoryOpt.map(c -> r.category().equalsIgnoreCase(c)).orElse(true))
                .filter(r -> qOpt.map(q -> {
                    String qLower = q.toLowerCase(Locale.ROOT);
                    if (r.name().toLowerCase(Locale.ROOT).contains(qLower)) return true;
                    return r.tags().stream().anyMatch(t -> t.toLowerCase(Locale.ROOT).contains(qLower));
                }).orElse(true))
                .toList();
    }
}
