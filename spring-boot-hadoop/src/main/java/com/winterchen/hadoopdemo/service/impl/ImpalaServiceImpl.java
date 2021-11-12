package com.winterchen.hadoopdemo.service.impl;

import com.winterchen.hadoopdemo.service.ImpalaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ImpalaServiceImpl implements ImpalaService {

    @Autowired
    @Qualifier("impalaJdbcTemplate")
    private JdbcTemplate impalaJdbcTemplate;

    @Autowired
    @Qualifier("impalaDruidDataSource")
    private DataSource impalaDruidDataSource;


    @Override
    public Object select(String hql) {
        return impalaJdbcTemplate.queryForObject(hql, Object.class);
    }

    @Override
    public List<String> listAllTables() {
        List<String> result = new ArrayList<>();
        try {
            Statement statement = impalaDruidDataSource.getConnection().createStatement();
            String sql = "show tables";
            log.info("Running:  " + sql);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                result.add(resultSet.getString(1));
            }
            return result;
        } catch (SQLException throwables) {
            log.error(throwables.getMessage());
        }

        return Collections.emptyList();
    }

    @Override
    public List<String> describeTable(String tableName) {
        if (StringUtils.isEmpty(tableName)){
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>();
        try {
            Statement statement = impalaDruidDataSource.getConnection().createStatement();
            String sql = "describe " + tableName;
            log.info("Running " + sql);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                result.add(resultSet.getString(1));
            }
            return result;
        } catch (SQLException throwables) {
            log.error(throwables.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> selectFromTable(String tableName) {
        if (StringUtils.isEmpty(tableName)){
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>();
        try {
            Statement statement = impalaDruidDataSource.getConnection().createStatement();
            String sql = "select * from " + tableName;
            log.info("Running " + sql);
            ResultSet resultSet = statement.executeQuery(sql);
            int columnCount = resultSet.getMetaData().getColumnCount();
            String str = null;
            while (resultSet.next()) {
                str = "";
                for (int i = 1; i < columnCount; i++) {
                    str += resultSet.getString(i) + "  ";
                }
                str += resultSet.getString(columnCount);
                log.info(str);
                result.add(str);
            }
            return result;
        } catch (SQLException throwables) {
            log.error(throwables.getMessage());
        }
        return Collections.emptyList();
    }
}
