package com.lorcanaapi.parameters;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.URIBit;
import com.lorcanaapi.URLParameter;

public class BulkTypeParameter extends URLParameter {

	public BulkTypeParameter() {
		super("bulktype", -1, "cards", "sets");
		Thread bulkThread = new BulkParameterThread();
		bulkThread.start();
	}

	@Override
	public void modifyReponse(URIBit bit, APIResponse response) {

		switch (bit.getValue()) {
		case "cards":
			response.setResponse(BulkParameterThread.getCards().toString());
			break;
		case "sets":
			response.setResponse(BulkParameterThread.getSets().toString());
			break;
		default:
			break;
		}

//		response.setResponse(getParameterKey());

	}

}

class BulkParameterThread extends Thread {

	private static JSONArray cards;

	public static JSONArray getCards() {
		if (cards == null) {
			return new JSONArray("");
		}
		return cards;
	}

	private static JSONArray sets;

	public static JSONArray getSets() {
		if (sets == null) {
			return new JSONArray("");
		}
		return sets;
	}

	@Override
	public void run() {

		System.out.println("Starting a new bulk endpoint thread. You should only see this message once");

		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
		ZonedDateTime nextRun = now.withHour(5).withMinute(0).withSecond(0);
		if (now.compareTo(nextRun) > 0)
			nextRun = nextRun.plusHours(12);

		Duration duration = Duration.between(now, nextRun);
//		long initialDelay = duration.getSeconds();
		long initialDelay = 1;
		System.out.println("intial delayt:" + initialDelay);

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				System.out.println("Updating cards and sets for bulk endpoint...");

				cards = MandatorySQLExecutor.getSQLResponseAsJSON("select * from card_info");
				sets = MandatorySQLExecutor.getSQLResponseAsJSON("select * from set_info");
				System.out.println("fetching complete");

			}

		}, initialDelay, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
		

	}
}
