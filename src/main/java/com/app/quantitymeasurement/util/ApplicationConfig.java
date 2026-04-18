package com.app.quantitymeasurement.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * slf4j = (Simple Logging Facade for Java -It is a wrapper (interface) that allows you to use logging without depending on a specific library.
 * “SLF4J is a common API that lets you plug in different logging frameworks (like Logback, Log4j) without changing your code.”
 * Loads database configuration from application.properties.
 * Supports system-property overrides for environment-specific settings.
 * In Java, a System Property is a key–value pair provided to the JVM (Java Virtual Machine) at runtime.
 
 *Logger is an object used to print messages about your program execution

“Logger is used to track what is happening inside your application.”
 */
public class ApplicationConfig {
	// used for logging messages (info , error , warning )
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    private static final String PROPERTIES_FILE = "application.properties";

    private final Properties props = new Properties();
//    Java’s built-in class to store key-value pairs
//    Example:
//
//    db.url=jdbc:mysql://localhost:3306/test
//    db.username=root

    // Singleton
    private static class Holder {
        private static final ApplicationConfig INSTANCE = new ApplicationConfig();
    }

    public static ApplicationConfig getInstance() {
        return Holder.INSTANCE;
    }
    
//    Only one instance is created
//    Thread-safe
//    Lazy loading (created only when needed)

    private ApplicationConfig() {
//    	Load a file (application.properties) from the project’s classpath as a stream
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (is == null) {
                logger.warn("{} not found on classpath – using defaults", PROPERTIES_FILE);
            } else {
                props.load(is);
                logger.info("Loaded configuration from {}", PROPERTIES_FILE);
            }
        } catch (IOException e) {
            logger.error("Failed to load {}: {}", PROPERTIES_FILE, e.getMessage());
        }
    }

    /** Returns a property value; system property takes precedence over file value. */
//    It is a common utility method used to fetch configuration values.
    public String get(String key, String defaultValue) {
        String sysProp = System.getProperty(key);
        if (sysProp != null) return sysProp;
        return props.getProperty(key, defaultValue);
    }

    public String  getDbUrl()            { return get("db.url",      "jdbc:h2:mem:quantitydb;DB_CLOSE_DELAY=-1"); }
    public String  getDbDriver()         { return get("db.driver",   "org.h2.Driver"); }
    public String  getDbUsername()       { return get("db.username", "sa"); }
    public String  getDbPassword()       { return get("db.password", ""); }
    public int     getPoolSize()         { return Integer.parseInt(get("pool.size", "10")); }
    public long    getConnectionTimeout(){ return Long.parseLong(get("pool.connection.timeout", "30000")); }
    public String  getRepositoryType()   { return get("repository.type", "cache"); }
}
