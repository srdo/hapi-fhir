package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class TestUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TestUtil.class);

	/**
	 * Set some system properties randomly after each test.. this is kind of hackish,
	 * but it helps us make sure we don't have any tests that depend on a particular
	 * environment
	 */
	public static void randomizeLocale() {
 		Locale[] availableLocales = {Locale.CANADA, Locale.GERMANY, Locale.TAIWAN};
		Locale.setDefault(availableLocales[(int) (Math.random() * availableLocales.length)]);
		ourLog.info("Tests are running in locale: " + Locale.getDefault().getDisplayName());
		if (Math.random() < 0.5) {
			ourLog.info("Tests are using WINDOWS line endings and ISO-8851-1");
			System.setProperty("file.encoding", "ISO-8859-1");
			System.setProperty("line.separator", "\r\n");
		} else {
			ourLog.info("Tests are using UNIX line endings and UTF-8");
			System.setProperty("file.encoding", "UTF-8");
			System.setProperty("line.separator", "\n");
		}
		String availableTimeZones[] = {"GMT+08:00", "GMT-05:00", "GMT+00:00", "GMT+03:30"};
		String timeZone = availableTimeZones[(int) (Math.random() * availableTimeZones.length)];
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
		ourLog.info("Tests are using time zone: {}", TimeZone.getDefault().getID());
	}


	/**
	 * <b>THIS IS FOR UNIT TESTS ONLY - DO NOT CALL THIS METHOD FROM USER CODE</b>
	 * <p>
	 * Wait for an atomicinteger to hit a given site and fail if it never does
	 */
	public static void waitForSize(int theTarget, AtomicInteger theInteger) {
		long start = System.currentTimeMillis();
		while (theInteger.get() != theTarget && (System.currentTimeMillis() - start) <= 15000) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException theE) {
				throw new Error(theE);
			}
		}
		if ((System.currentTimeMillis() - start) >= 15000) {
			throw new IllegalStateException("Size " + theInteger.get() + " is != target " + theTarget);
		}
	}

	/**
	 * <b>THIS IS FOR UNIT TESTS ONLY - DO NOT CALL THIS METHOD FROM USER CODE</b>
	 * <p>
	 * Wait for an atomicinteger to hit a given site and fail if it never does
	 */
	public static void waitForSize(int theTarget, Callable<Integer> theSource) throws Exception {
		long start = System.currentTimeMillis();
		while (theSource.call() != theTarget && (System.currentTimeMillis() - start) <= 15000) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException theE) {
				throw new Error(theE);
			}
		}
		if ((System.currentTimeMillis() - start) >= 15000) {
			throw new IllegalStateException("Size " + theSource.call() + " is != target " + theTarget);
		}
	}

	/**
	 * <b>THIS IS FOR UNIT TESTS ONLY - DO NOT CALL THIS METHOD FROM USER CODE</b>
	 * <p>
	 * Strip \r chars from a string to account for line ending platform differences
	 */
	public static String stripReturns(String theString) {
		return defaultString(theString).replace("\r", "");
	}

}
