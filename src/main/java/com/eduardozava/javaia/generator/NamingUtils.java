package com.eduardozava.javaia.generator;

public final class NamingUtils {

    private NamingUtils() {
    }

    public static String toPascalCase(String value) {
        if (value == null || value.isBlank()) {
            return "Generated";
        }
        String[] parts = value.trim().split("[^a-zA-Z0-9]+");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (!part.isBlank()) {
                builder.append(Character.toUpperCase(part.charAt(0)));
                if (part.length() > 1) {
                    builder.append(part.substring(1).toLowerCase());
                }
            }
        }
        return builder.isEmpty() ? "Generated" : builder.toString();
    }
}
