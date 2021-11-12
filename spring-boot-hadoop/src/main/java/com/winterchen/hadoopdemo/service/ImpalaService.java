package com.winterchen.hadoopdemo.service;

import java.util.List;

public interface ImpalaService {

    Object select(String hql);

    List<String> listAllTables();

    List<String> describeTable(String tableName);

    List<String> selectFromTable(String tableName);

}
