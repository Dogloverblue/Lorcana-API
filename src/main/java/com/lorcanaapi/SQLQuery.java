package com.lorcanaapi;

public class SQLQuery {
	
	String select = "SELECT *";
//	String from = "FROM cards";
	String from = "FROM card_info";
	String where = "";
	String groupBy = "";
	String having = "";	
	String orderBy = "";
	boolean overrideClauseBuiltQueries = false;
	String overriddenQuery;

	public SQLQuery() {

	}
	
	public void setQueryAndDisableIndividualClauseEditing(String query) {
		overrideClauseBuiltQueries = true;
		overriddenQuery = query;
	}
	
	public String getQuery() {
		if (overrideClauseBuiltQueries) {
			return overriddenQuery;
		}
		return select + " " + from + " " + where + " " + groupBy + " " + having + " " + orderBy;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public String getHaving() {
		return having;
	}

	public void setHaving(String having) {
		this.having = having;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	
	
	

}
