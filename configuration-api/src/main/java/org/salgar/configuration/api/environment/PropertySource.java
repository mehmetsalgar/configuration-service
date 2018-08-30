package org.salgar.configuration.api.environment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class PropertySource {

    private String name;

    private Map<?, ?> source;

    @JsonCreator
    public PropertySource(@JsonProperty("name") String name,
                          @JsonProperty("source") Map<?, ?> source) {
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public Map<?, ?> getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "PropertySource [name=" + name + "]";
    }

}