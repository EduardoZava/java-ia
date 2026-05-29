package com.eduardozava.javaia.model;

import java.util.List;

public record ServiceArchitecture(
        String basePackage,
        List<String> modules,
        String rationale
) {
}
