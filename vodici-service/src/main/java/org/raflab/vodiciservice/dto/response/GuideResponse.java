package org.raflab.vodiciservice.dto.response;

import lombok.Data;


@Data
public class GuideResponse {
    private Long id;
    private String first_name;
    private String last_name;
    private String bio;
    private Double rating;

    private GuideResponse(Builder builder) {
        this.id = builder.id;
        this.first_name = builder.first_name;
        this.last_name = builder.last_name;
        this.bio = builder.bio;
        this.rating = builder.rating;
    }

    public Long getId() { return id; }
    public String getFirst_name() { return first_name; }
    public String getLast_name() { return last_name; }
    public String getBio() { return bio; }
    public Double getRating() { return rating; }

    public static class Builder {
        private Long id;
        private String first_name;
        private String last_name;
        private String bio;
        private Double rating;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder first_name(String first_name) {
            this.first_name = first_name;
            return this;
        }

        public Builder last_name(String last_name) {
            this.last_name = last_name;
            return this;
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder rating(Double rating) {
            this.rating = rating;
            return this;
        }

        public GuideResponse build() {
            return new GuideResponse(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}


