package io.github.eduardozava.javaia.infra;

import java.util.Optional;

public interface DeepSeekClient {

    Optional<String> complete(String prompt);

    String configurationStatus();
}
