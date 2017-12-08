package com.dataonline.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.dataonline.util.LineNo;

public class Connector {
    private static final String Driver = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/";

    private Logger log = Logger.getLogger(Connector.class);
    private Connection connection = null;

    public Connector(String databaseName, String user, String password) throws Exception {
        Class.forName(Driver);
        connection = DriverManager.getConnection(URL + databaseName + "?characterEncoding=UTF-8", user, password);

        log.debug(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "数据库连接成功");

        Statement st = null;
        try {
            String sql = "use " + databaseName;
    
            st = connection.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws Exception {
        if (null == connection) {
            return;
        }

        try {
            connection.close();
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }

        log.debug(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "数据库关闭成功");
    }
}
