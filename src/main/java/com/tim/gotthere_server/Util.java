package com.tim.gotthere_server;

public class Util {
	
	private Util() {

	}

	public static String sqlDateTimeToJavaScript(String sqlDateTime) {
		return sqlDateTime.replace(' ', 'T');
	}

	public static String javaScriptDateTimeToSQL(String javaScriptDateTime) {
		return javaScriptDateTime.replace('T', ' ');
	}

	public static double metersPerSecondToMilesPerHour(double metersPerSecond) {
		return metersPerSecond * 2.23693629205;
	}
}
