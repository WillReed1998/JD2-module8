package org.example;

import org.apache.log4j.Logger;
import org.example.data.Member;
import org.example.data.OsbbCRUD;
import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.example.Config.*;

public class App {
    private static final Logger logger = Logger.getLogger(App.class);


    public static void main( String[] args ) {
        logger.info("The program has started");

        try (OsbbCRUD crud = new OsbbCRUD()
                     .init()) {
            for(Member member : crud.getMembersWithAutoNotAllowed()) {
                final StringBuffer sb = new StringBuffer();
                sb.append(member.getId())
                        .append(" : ")
                        .append(member.getName())
                        .append(" : ")
                        .append("\r\n");
                System.out.println(sb);
            }
        } catch (SQLException | IOException e) {
            logger.fatal(e);
        }
    }
}
