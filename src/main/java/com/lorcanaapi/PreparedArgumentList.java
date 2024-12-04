package com.lorcanaapi;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreparedArgumentList {
    private List<Object> arguments;
    private List<String> types;

    public PreparedArgumentList() {
        arguments = new ArrayList<>();
        types = new ArrayList<>();
    }

    public void addArgument(Object argument, String type) {
        arguments.add(argument);
        types.add(type);
    }

    public void setArguments(PreparedStatement preparedStatement) throws SQLException {
        for (int i = 0; i < arguments.size(); i++) {
            String type = types.get(i);
            Object argument = arguments.get(i);

            switch (type.toUpperCase()) {
                case "VARCHAR":
                    preparedStatement.setString(i + 1, (String) argument);
                    break;
                case "INT":
                    preparedStatement.setInt(i + 1, (Integer) argument);
                    break;
                case "TINYINT":
                    preparedStatement.setByte(i + 1, (Byte) argument);
                    break;
                case "DATETIME":
                    preparedStatement.setTimestamp(i + 1, java.sql.Timestamp.valueOf((String) argument));
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported type: " + type);
            }
        }
    }

}