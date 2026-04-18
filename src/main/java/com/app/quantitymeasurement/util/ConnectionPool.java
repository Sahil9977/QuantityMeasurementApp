package com.app.quantitymeasurement.util;

import com.app.quantitymeasurement.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//This class manages a pool of database connections so that your app can reuse them instead of creating a new connection every time.
// Why Connection Pool is needed?
//
// Without pool:
//
//Request → Create connection → Use → Close
//
//❌ Slow (connection creation is expensive)
//❌ High DB load
//
//👉 With pool:
//
//Request → Take connection → Use → Return to pool
//
//✔ Fast
//✔ Efficient

/**
 * A simple thread-safe JDBC connection pool.
 *
 * On creation it pre-opens {@code poolSize} connections and places them in a
 * BlockingQueue.  Callers acquire() a connection, use it, and must release() it
 * when done.  Connections are never closed mid-session; the pool is shut down
 * via close() when the application exits.
 */
public class ConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);

    private final BlockingQueue<Connection> pool;
    private final List<Connection>          allConnections;
    private final ApplicationConfig         config;
//    👉 Tracks how many connections are currently in use
    private final AtomicInteger             activeCount = new AtomicInteger(0);
//    Prevents usage after shutdown
    private volatile boolean                closed      = false;

    // Singleton
    private static class Holder {
        private static final ConnectionPool INSTANCE = new ConnectionPool(ApplicationConfig.getInstance());
    }

    public static ConnectionPool getInstance() {
        return Holder.INSTANCE;
    }

    public ConnectionPool(ApplicationConfig config) {
        this.config         = config;
        int size            = config.getPoolSize();
        this.pool           = new ArrayBlockingQueue<>(size);
        this.allConnections = new ArrayList<>(size);

        try {
            Class.forName(config.getDbDriver());
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("JDBC driver not found: " + config.getDbDriver(), e);
        }
        
//        ✔ Opens DB connection
//        ✔ Adds to pool

        for (int i = 0; i < size; i++) {
            try {
                Connection conn = DriverManager.getConnection(
                        config.getDbUrl(), config.getDbUsername(), config.getDbPassword());
                pool.offer(conn);
                allConnections.add(conn);
            } catch (SQLException e) {
                throw new DatabaseException("Failed to create connection #" + (i + 1), e);
            }
        }

        logger.info("ConnectionPool initialised – {} connections, URL: {}", size, config.getDbUrl());
    }

    /**
     * Acquires a connection from the pool.
     * Blocks up to the configured timeout before throwing DatabaseException.
     */
    public Connection acquire() {
        if (closed) throw new DatabaseException("ConnectionPool is closed");
        try {
            long timeout = config.getConnectionTimeout();
            Connection conn = pool.poll(timeout, TimeUnit.MILLISECONDS);
            if (conn == null)
                throw new DatabaseException("Connection pool exhausted – no connection available after "
                        + timeout + " ms");
            activeCount.incrementAndGet();
            logger.debug("Connection acquired – active={}", activeCount.get());
            return conn;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DatabaseException("Interrupted while waiting for connection", e);
        }
    }

    /**
     * Returns a connection to the pool.
     * If the connection is unusable (closed externally) a replacement is created.
     */
    public void release(Connection conn) {
        if (conn == null) return;
        try {
            if (!conn.isClosed()) {
                conn.setAutoCommit(true);   // reset any transaction state
                pool.offer(conn);
                activeCount.decrementAndGet();
                logger.debug("Connection released – active={}", activeCount.get());
                return;
            }
        } catch (SQLException ignored) { /* fall through to replacement */ }

        // Connection is broken – create a replacement
        logger.warn("Replacing broken connection");
        try {
            Connection replacement = DriverManager.getConnection(
                    config.getDbUrl(), config.getDbUsername(), config.getDbPassword());
            pool.offer(replacement);
            activeCount.decrementAndGet();
        } catch (SQLException e) {
            logger.error("Failed to create replacement connection: {}", e.getMessage());
            activeCount.decrementAndGet();
        }
    }

    /** Closes all connections and marks the pool as shut down. */
    public void close() {
        closed = true;
        for (Connection conn : allConnections) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
        allConnections.clear();
        pool.clear();
        logger.info("ConnectionPool closed");
    }

    /** Human-readable statistics for monitoring / logging. */
    public String getStatistics() {
        return String.format("ConnectionPool[total=%d, active=%d, idle=%d, closed=%b]",
                allConnections.size(), activeCount.get(),
                pool.size(), closed);
    }

    public int  getTotalSize()  { return allConnections.size(); }
    public int  getActiveCount(){ return activeCount.get(); }
    public int  getIdleCount()  { return pool.size(); }
    public boolean isClosed()   { return closed; }
}
