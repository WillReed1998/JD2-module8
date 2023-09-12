package org.example.data;

import org.apache.log4j.Logger;
import org.example.App;
import org.flywaydb.core.Flyway;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import static org.example.Config.*;

public class OsbbCRUD implements Closeable {

    private static final Logger logger = Logger.getLogger(OsbbCRUD.class);

    private Connection conn = null;

    private static final String sqlMembersWithAutoNotAllowedQuery = "SELECT\n" +
            "    R.first_name,\n" +
            "    R.last_name,\n" +
            "    R.email,\n" +
            "    B.address AS building_address,\n" +
            "    A.apartmentNumber,\n" +
            "    A.area\n" +
            "FROM\n" +
            "    Residents R\n" +
            "JOIN\n" +
            "    Apartments A ON R.apartment_id = A.idApartments\n" +
            "JOIN\n" +
            "    Buildings B ON A.building_id = B.idBuildings\n" +
            "WHERE\n" +
            "    R.lives_here = 1\n" +
            "    AND R.apartment_count < 2\n" +
            "    AND (\n" +
            "        R.idResident NOT IN (\n" +
            "            SELECT RC.resident_id\n" +
            "            FROM ResidentsCar RC\n" +
            "            WHERE RC.entryPermit = 1\n" +
            "        )\n" +
            "        OR R.idResident IS NULL\n" +
            "    );";

    private void fwMigration(){
        logger.debug("Flyway migration execute");

        Flyway.configure()
                .dataSource(jdbcUrl, username, password)
                .locations("classpath:flyway/scripts")
                .load()
                .migrate();
    }
    public OsbbCRUD init() throws SQLException {
        logger.info("Crud has initialized");
        fwMigration();

        conn = DriverManager.getConnection(jdbcUrl, username, password);

        return this;
    }

    @Override
    public void close() throws IOException{
        try {
            conn.close();
            conn = null;
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public List<Member> getMembersWithAutoNotAllowed() throws SQLException {
        logger.trace("Call getting members with auto not allowed method");

        final List<Member> result = new LinkedList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlMembersWithAutoNotAllowedQuery)) {
            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result.add(new Member()
                        .setFirstName(resultSet.getString("first_name"))
                        .setLastName(resultSet.getString("last_name"))
                        .setEmail(resultSet.getString("email"))
                        .setAddress(resultSet.getString("address"))
                        .setApartmentNumber(resultSet.getInt("apartmentNumber"))
                        .setArea(resultSet.getFloat("area"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
