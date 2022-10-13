package com.example.suggestionsexample.models;

public class FullClick {
    private Integer id;
    private String uri;

    public FullClick() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public static FullClick create(Integer id, String uri) {
        FullClick fullClick = new FullClick();
        fullClick.setId(id);
        fullClick.setUri(uri);

        return fullClick;
    }

    @Override
    public String toString() {
        return "CategoryChange{" +
                "id=" + id +
                ", uri='" + uri + '\'' +
                '}';
    }
}
