package com.FEIS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryBuilder {
	
	List<Pair<String>> tables = new ArrayList<Pair<String>>();
	
	List<String> fields = new ArrayList<String>();
	List<String> order = new ArrayList<String>();

	String select = null;
	
	public void addTable(String table, String alias) { 
		tables.add(new Pair<String>(table, alias));
	}
	
	public void addField(String field) { 
		fields.add(field);
	}
	
	public String getQuery()
	{
		return getSelectForQuery() + 
				getFieldsForQuery() + 
				getFromTablesForQuery() + 
				getWhereClauseForQuery() + 
				getOrderByForQuery();
	}
	
	private String getOrderByForQuery() {
		// TODO Auto-generated method stub
		if(!order.isEmpty()) { 
			return " ORDER BY " + toCommaSepList(order);
		}
		else
			return " " ;
	}

	private String toCommaSepList(List<String> l) {
		StringBuilder sb = new StringBuilder();
		
		for (String string : l) {
			sb.append(string).append(" , ");
		}
		
		if(sb.length()> 0) { 
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	
	private String toCommaSepList2(List<Pair<String>> l) {
		StringBuilder sb = new StringBuilder();
		
		for (Pair<String> p : l) {
			sb.append(p.one).append(" ").append(p.two).append(" , ");
		}
		
		if(sb.length()> 0) { 
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}

	private String getWhereClauseForQuery() {
		// TODO Auto-generated method stub
		return "";
	}

	private String getFromTablesForQuery() {
		// TODO Auto-generated method stub
		return "";
	}

	private String getFieldsForQuery() {
		// TODO Auto-generated method stub
		return "";
	}

	private String getSelectForQuery() {
		// TODO Auto-generated method stub
		return select + " ";
	}

	static class Pair<T>
	{
		public T one; 
		public T two;
		public Pair(T t, T a) {
			one = t; 
			two = a; 
		}
	}
}
