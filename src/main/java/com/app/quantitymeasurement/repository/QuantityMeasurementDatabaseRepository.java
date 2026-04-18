package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.DatabaseException;
import com.app.quantitymeasurement.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * UC16 – JDBC-backed repository.
 *
 * Design decisions:
 *  - Uses ConnectionPool for efficient connection reuse.
 *  - All SQL uses PreparedStatement to prevent SQL injection.
 *  - Schema is initialised on first use (createTableIfNotExists).
 *  - Every method acquires and releases a connection inside a try-finally block
 *    to guarantee no connection leaks even when exceptions are thrown.
 *  - findByMeasurementType uses a LIKE query against the operand columns where
 *    QuantityDTO.toString() embeds the measurement type, e.g. "[LENGTH]".
 */
public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementDatabaseRepository.class);

    private static final String TABLE = "quantity_measurement_entity";

    private static final String SQL_INSERT =
            "INSERT INTO " + TABLE +
            " (operation_type, operand1, operand2, result, has_error, error_message, created_at) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_ALL =
            "SELECT id, operation_type, operand1, operand2, result, has_error, error_message, created_at " +
            "FROM " + TABLE + " ORDER BY created_at ASC";

    private static final String SQL_SELECT_BY_OP =
            "SELECT id, operation_type, operand1, operand2, result, has_error, error_message, created_at " +
            "FROM " + TABLE + " WHERE UPPER(operation_type) = UPPER(?) ORDER BY created_at ASC";

    private static final String SQL_SELECT_BY_TYPE =
            "SELECT id, operation_type, operand1, operand2, result, has_error, error_message, created_at " +
            "FROM " + TABLE +
            " WHERE UPPER(operand1) LIKE ? OR UPPER(operand2) LIKE ? ORDER BY created_at ASC";

    private static final String SQL_COUNT =
            "SELECT COUNT(*) FROM " + TABLE;

    private static final String SQL_DELETE_ALL =
            "DELETE FROM " + TABLE;

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
            "  id             BIGINT AUTO_INCREMENT PRIMARY KEY, " +
            "  operation_type VARCHAR(20)  NOT NULL, " +
            "  operand1       VARCHAR(255) NOT NULL, " +
            "  operand2       VARCHAR(255), " +
            "  result         VARCHAR(255), " +
            "  has_error      BOOLEAN      NOT NULL DEFAULT FALSE, " +
            "  error_message  VARCHAR(500), " +
            "  created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP" +
            ")";

    private static final String SQL_CREATE_IDX_OP =
            "CREATE INDEX IF NOT EXISTS idx_operation_type ON " + TABLE + "(operation_type)";

    private static final String SQL_CREATE_IDX_TS =
            "CREATE INDEX IF NOT EXISTS idx_created_at ON " + TABLE + "(created_at)";

    private final ConnectionPool connectionPool;

    public QuantityMeasurementDatabaseRepository(ConnectionPool connectionPool) {
        if (connectionPool == null)
            throw new IllegalArgumentException("ConnectionPool cannot be null");
        this.connectionPool = connectionPool;
        initialiseSchema();
        logger.info("QuantityMeasurementDatabaseRepository initialised");
    }

    // ── Schema initialisation ────────────────────────────────────────────────

    private void initialiseSchema() {
        Connection conn = connectionPool.acquire();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(SQL_CREATE_TABLE);
            stmt.execute(SQL_CREATE_IDX_OP);
            stmt.execute(SQL_CREATE_IDX_TS);
            logger.info("Database schema verified/created");
        } catch (SQLException e) {
            throw new DatabaseException("Failed to initialise schema: " + e.getMessage(), e);
        } finally {
            connectionPool.release(conn);
        }
    }

    // ── save ────────────────────────────────────────────────────────────────

    @Override
    public void save(QuantityMeasurementEntity entity) {
        if (entity == null) throw new IllegalArgumentException("Entity cannot be null");

        Connection conn = connectionPool.acquire();
        try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString   (1, entity.getOperationType());
            ps.setString   (2, entity.getOperand1());
            ps.setString   (3, entity.getOperand2());          // nullable
            ps.setString   (4, entity.getResult());            // nullable
            ps.setBoolean  (5, entity.hasError());
            ps.setString   (6, entity.getErrorMessage());      // nullable
            ps.setTimestamp(7, Timestamp.valueOf(
                    entity.getTimestamp() != null ? entity.getTimestamp() : LocalDateTime.now()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setId(keys.getLong(1));
                }
            }

            logger.debug("Saved {} entity with id={}", entity.getOperationType(), entity.getId());

        } catch (SQLException e) {
            throw new DatabaseException("Failed to save entity: " + e.getMessage(), e);
        } finally {
            connectionPool.release(conn);
        }
    }

    // ── findAll ─────────────────────────────────────────────────────────────

    @Override
    public List<QuantityMeasurementEntity> findAll() {
        Connection conn = connectionPool.acquire();
        try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            List<QuantityMeasurementEntity> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            logger.debug("findAll() returned {} records", list.size());
            return list;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve measurements: " + e.getMessage(), e);
        } finally {
            connectionPool.release(conn);
        }
    }

    // ── findByOperationType ──────────────────────────────────────────────────

    @Override
    public List<QuantityMeasurementEntity> findByOperationType(String operationType) {
        if (operationType == null) throw new IllegalArgumentException("operationType cannot be null");

        Connection conn = connectionPool.acquire();
        try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_OP)) {

            ps.setString(1, operationType);
            try (ResultSet rs = ps.executeQuery()) {
                List<QuantityMeasurementEntity> list = new ArrayList<>();
                while (rs.next()) list.add(mapRow(rs));
                logger.debug("findByOperationType('{}') returned {} records", operationType, list.size());
                return list;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to query by operation type: " + e.getMessage(), e);
        } finally {
            connectionPool.release(conn);
        }
    }

    // ── findByMeasurementType ────────────────────────────────────────────────

    @Override
    public List<QuantityMeasurementEntity> findByMeasurementType(String measurementType) {
        if (measurementType == null) throw new IllegalArgumentException("measurementType cannot be null");

        String pattern = "%" + measurementType.toUpperCase() + "%";

        Connection conn = connectionPool.acquire();
        try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_TYPE)) {

            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                List<QuantityMeasurementEntity> list = new ArrayList<>();
                while (rs.next()) list.add(mapRow(rs));
                logger.debug("findByMeasurementType('{}') returned {} records", measurementType, list.size());
                return list;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to query by measurement type: " + e.getMessage(), e);
        } finally {
            connectionPool.release(conn);
        }
    }

    // ── getTotalCount ────────────────────────────────────────────────────────

    @Override
    public int getTotalCount() {
        Connection conn = connectionPool.acquire();
        try (PreparedStatement ps = conn.prepareStatement(SQL_COUNT);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
            return 0;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to count measurements: " + e.getMessage(), e);
        } finally {
            connectionPool.release(conn);
        }
    }

    // ── deleteAll ────────────────────────────────────────────────────────────

    @Override
    public void deleteAll() {
        Connection conn = connectionPool.acquire();
        try (PreparedStatement ps = conn.prepareStatement(SQL_DELETE_ALL)) {

            int deleted = ps.executeUpdate();
            logger.info("deleteAll() removed {} records", deleted);

        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete measurements: " + e.getMessage(), e);
        } finally {
            connectionPool.release(conn);
        }
    }

    // ── Pool statistics ──────────────────────────────────────────────────────

    @Override
    public String getPoolStatistics() {
        return connectionPool.getStatistics();
    }

    // ── Release resources ────────────────────────────────────────────────────

    @Override
    public void releaseResources() {
        connectionPool.close();
        logger.info("Database resources released");
    }

    // ── Row mapper ───────────────────────────────────────────────────────────

    private QuantityMeasurementEntity mapRow(ResultSet rs) throws SQLException {
        String opType    = rs.getString("operation_type");
        String op1       = rs.getString("operand1");
        String op2       = rs.getString("operand2");
        String result    = rs.getString("result");
        boolean hasError = rs.getBoolean("has_error");
        String errMsg    = rs.getString("error_message");
        Timestamp ts     = rs.getTimestamp("created_at");

        QuantityMeasurementEntity entity;
        if (hasError) {
            entity = new QuantityMeasurementEntity(opType, op1, op2, errMsg, true);
        } else if (op2 == null) {
            entity = new QuantityMeasurementEntity(opType, op1, result);
        } else {
            entity = new QuantityMeasurementEntity(opType, op1, op2, result);
        }
        entity.setId(rs.getLong("id"));
        if (ts != null) entity.setTimestamp(ts.toLocalDateTime());
        return entity;
    }
}
