package tbb.x4.api.model;

public record ElementName(String name) {
    private static final String VALID_NAME_REGEX = "^[a-zA-Z_][a-zA-Z0-9_-]*$";

    public ElementName {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        name = name.toLowerCase();
        if (!name.matches(VALID_NAME_REGEX)) {
            throw new IllegalArgumentException("Invalid name: " + name + ". Must match XML tag/attribute naming rules.");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
