package com.example.pa03.DTO;

public class resourceDTO {

        private Long id;
        private String name;
        private String category;
        private String location;
        private String url;
        private List<String> tags;

        // Default constructor - REQUIRED for JSON deserialization
        public class TaskDTO() {}

        // Full constructor - convenient for creating objects
        public class TaskDTO(Long id, String name, String category, String location, String url, String tags) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.location = location;
            this.url = url;
            this.tags = tags;
        }

        // Getters and Setters - REQUIRED for JSON conversion
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category ; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public Integer getUrl() { return url; }
        public void setUrl(Integer url) { this.url = url; }

        public Integer getTags() { return tags; }
        public void setTags(Integer tags) { this.tags = tags; }
    }

