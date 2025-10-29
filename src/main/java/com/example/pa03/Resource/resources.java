package com.example.pa03.Resource;

import java.util.List;

public class ResourceDTO {
    private final String id;
    private final String name;
    private final String category;
    private final String location;
    private final String url;
    private final List<String> tags;

    public ResourceDTO(String id, String name, String category, String location, String url, List<String> tags) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.location = location;
        this.url = url;
        this.tags = List.copyOf(tags); // defensive copy to keep immutable
    }

    public String id() { return id; }
    public String name() { return name; }
    public String category() { return category; }
    public String location() { return location; }
    public String url() { return url; }
    public List<String> tags() { return tags; }
}

