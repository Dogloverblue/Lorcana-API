package com.lorcanaapi;

public class SQLQuery {
	
	String select = "SELECT *";
//	String from = "FROM cards";
	String from = "";
	String where = "";
	String groupBy = "";
	String having = "";	
	String orderBy = "";
	boolean overrideClauseBuiltQueries = false;
	String overriddenQuery;
	
	final public static String DEFAULT_SELECT_VALUE = "SELECT *";
	// No default FROM
	final public static String DEFAULT_WHERE_VALUE = "";
	final public static String DEFAULT_GROUPBY_VALUE = "";
	final public static String DEFAULT_HAVING_VALUE = "";
	final public static String DEFAULT_ORDERBY_VALUE = "";

	public SQLQuery() {
		setSelect(DEFAULT_SELECT_VALUE);
		setWhere(DEFAULT_WHERE_VALUE);
		setGroupBy(DEFAULT_GROUPBY_VALUE);
		setHaving(DEFAULT_HAVING_VALUE);
		setOrderBy(DEFAULT_ORDERBY_VALUE);
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
