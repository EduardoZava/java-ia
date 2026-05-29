package io.github.eduardozava.javaia.model;

import java.util.List;

public record MicroserviceSpec(String serviceName, String description, List<EndpointSpec> endpoints) {
}
