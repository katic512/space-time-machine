package com.gamesys.timetravel.spacetimemachine.util;

import static com.gamesys.timetravel.spacetimemachine.constant.AppConstants.RANDOM_MAX_LIMIT;
import static com.gamesys.timetravel.spacetimemachine.constant.AppConstants.RANDOM_MIN_LIMIT;

import java.util.Random;
public class AppUtils {

	private static Random random = new Random();
	
	public static int getRandomNumInRange() {
		return random.nextInt((RANDOM_MAX_LIMIT - RANDOM_MIN_LIMIT) + 1) + RANDOM_MIN_LIMIT;
	}
}
