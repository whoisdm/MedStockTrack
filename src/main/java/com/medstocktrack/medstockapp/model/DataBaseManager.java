package com.medstocktrack.medstockapp.model;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class DataBaseManager {

    public static MysqlDataSource getDataSource() {
        Properties props = new Properties();
        try {
            props.load(Files.newInputStream(Path.of("database.properties"), StandardOpenOption.READ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(props.getProperty("serverName"));
        dataSource.setPort(Integer.parseInt(props.getProperty("port")));
        dataSource.setDatabaseName(props.getProperty("databaseName"));
        dataSource.setUser(props.getProperty("user"));
        dataSource.setPassword(props.getProperty("password"));
        return dataSource;
    }


}
