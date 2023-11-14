package com.lorcanaapi.parameters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.URIBit;
import com.lorcanaapi.URLParameter;

public class MandatorySQLExecutor extends URLParameter {

	public MandatorySQLExecutor() {
		super("sqlexecutor", 0);
	}

	@Override
	public void modifyReponse(URIBit URIBit, APIResponse response) {
		System.out.println("MANDATYORY SQL THING TRIGGERD");
		System.out.println("Querty is " + response.getSqlQuery().getQuery());
		response.setResponse(getSQLResponseAsJSON(response.getSqlQuery().getQuery()).toString());
	}

//	static final String DB_URL = "jdbc:mysql://localhost:3306/?user=root";
//	   static final String USER = "root";
//	   static final String PASS = "whatisthisthing";
	static final String DB_URL = "jdbc:mysql://8.8.246.41:3306/?user=ptcjlukd_firefox";
	   static final String USER = "ptcjlukd_firefox";
	   static final String PASS = "FoxBot12!";
	public JSONArray getSQLResponseAsJSON(String sqlCommand) {
		 try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
		         Statement stmt = conn.createStatement();
		      ) {		      
//		         String sql = "USE lorcanaapi;";
			 	String sql = "USE ptcjlukd_lordb;";
		         System.out.println(stmt.executeUpdate(sql));
		         ResultSet set = stmt.executeQuery(sqlCommand);
		         return resultSetToJSONArray(set);
		 } catch(SQLException e) {
			 e.printStackTrace();
			 return null;
		 }
	}
	public JSONArray resultSetToJSONArray(ResultSet resultSet) {
		try {
		ResultSetMetaData md = resultSet.getMetaData();
		int numCols = md.getColumnCount();
		List<String> colNames = IntStream.range(0, numCols)
		  .mapToObj(i -> {
		      try {
		          return md.getColumnName(i + 1);
		      } catch (SQLException e) {
		          e.printStackTrace();
		          return "?";
		      }
		  })
		  .collect(Collectors.toList());

		JSONArray result = new JSONArray();
			while (resultSet.next()) {
			    JSONObject row = new JSONObject();
			    colNames.forEach(cn -> {
			        try {
			            row.put(cn, resultSet.getObject(cn));
			        } catch (JSONException | SQLException e) {
			            e.printStackTrace();
			        }
			    });
			    result.put(row);
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
