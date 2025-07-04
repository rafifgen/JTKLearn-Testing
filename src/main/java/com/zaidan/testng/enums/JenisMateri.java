package com.zaidan.testng.enums;

public enum JenisMateri {
    TEKS("teks"),
    VIDEO("video");

    private final String dbValue;

    JenisMateri(String dbValue) {
        this.dbValue = dbValue;
    }

    /**
     * Returns the string representation of the enum value as stored in the database.
     * @return The database string value.
     */
    public String getDbValue() {
        return dbValue;
    }

    /**
     * Converts a database string value back to its corresponding MateriJenisMateri enum constant.
     * This is useful when reading data from the database.
     *
     * @param dbString The string value from the database (e.g., "teks", "video").
     * @return The corresponding MateriJenisMateri enum constant.
     * @throws IllegalArgumentException If the provided string does not match any known enum value.
     */
    public static JenisMateri fromDbValue(String dbString) {
        for (JenisMateri type : JenisMateri.values()) {
            if (type.getDbValue().equalsIgnoreCase(dbString)) { // Use equalsIgnoreCase for robustness
                return type;
            }
        }
        // Handle cases where an unknown value might come from the database
        // You can throw an exception, return a default/UNKNOWN value, or log a warning.
        throw new IllegalArgumentException("Unknown MateriJenisMateri database value: " + dbString);
    }
}
