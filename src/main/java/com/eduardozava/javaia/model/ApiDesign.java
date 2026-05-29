package com.eduardozava.javaia.model;

import java.util.List;

public record ApiDesign(
        List<GeneratedArtifact> artifacts,
        String summary
) {
}
