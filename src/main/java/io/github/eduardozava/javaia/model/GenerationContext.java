package io.github.eduardozava.javaia.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GenerationContext {

    private final MicroserviceSpec spec;
    private final List<String> warnings = new ArrayList<>();
    private final Map<String, String> artifacts = new LinkedHashMap<>();
    private final Map<String, String> metadata = new LinkedHashMap<>();

    public GenerationContext(MicroserviceSpec spec) {
        this.spec = spec;
    }

    public MicroserviceSpec spec() {
        return spec;
    }

    public void addWarning(String warning) {
        warnings.add(warning);
    }

    public List<String> warnings() {
        return warnings;
    }

    public void addArtifact(String path, String content) {
        artifacts.put(path, content);
    }

    public Map<String, String> artifacts() {
        return artifacts;
    }

    public void putMetadata(String key, String value) {
        metadata.put(key, value);
    }

    public Map<String, String> metadata() {
        return metadata;
    }
}
