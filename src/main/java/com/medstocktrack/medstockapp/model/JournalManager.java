package com.medstocktrack.medstockapp.model;

import com.mysql.cj.jdbc.MysqlDataSource;

public class JournalManager {
    private final MysqlDataSource dataSource;
    public JournalManager() {
        dataSource = DataBaseManager.getDataSource();
    }
}
