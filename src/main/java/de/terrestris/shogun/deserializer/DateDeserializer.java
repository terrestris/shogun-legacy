/* Copyright (c) 2012-2014, terrestris GmbH & Co. KG
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * (This is the BSD 3-Clause, sometimes called 'BSD New' or 'BSD Simplified',
 * see http://opensource.org/licenses/BSD-3-Clause)
 */
package de.terrestris.shogun.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class responsible for deserializing a date.
 *
 * Makes a Date object out of an UNIX timestamp or a formatted date string.
 *
 * @author terrestris GmbH & Co. KG
 *
 */
public class DateDeserializer extends JsonDeserializer<Date> {

	private static Logger LOGGER = LogManager.getLogger(DateDeserializer.class);

	private static String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";

	private static String[] FORMATTED_DATE_HINT_CHARS = {":", "."};

	/**
	 * Does some simple checks to determine whether we we're passed a formatted
	 * date string. This is far from being complete, but does the job for the
	 * current purposes.
	 *
	 * @param jsonStr
	 * @return Whether the passed string looked like a formatted date.
	 */
	public static boolean looksLikeFormattedDateString(String jsonStr) {
		boolean looksLikeFormattedDate = false;
		int formattedLen = DATE_FORMAT.length();

		if (jsonStr != null) {
			if(jsonStr.length() == formattedLen) {
				for (int idx = 0; idx < FORMATTED_DATE_HINT_CHARS.length; idx++) {
					if(!jsonStr.contains(FORMATTED_DATE_HINT_CHARS[idx])){
						looksLikeFormattedDate = false;
						break;
					} else {
						looksLikeFormattedDate = true;
					}
				}
			}
		}

		return looksLikeFormattedDate;
	}

	/**
	 * This method tries to convert a passed date string into a date.
	 *
	 * Currently only strings formatted as <code>dd.MM.yyyy HH:mm:ss</code> are
	 * supported.
	 *
	 * @param jsonStr
	 * @return a <code>Date</code>-object or null.
	 */
	public static Date parseCommonlyFormattedDate(String jsonStr) {
		SimpleDateFormat parserSDF = new SimpleDateFormat(DATE_FORMAT);
		parserSDF.setLenient(false);

		Date parsedDate = null;

		String failNotice = "Failed to parse string '" + jsonStr +
				"' with format '" + DATE_FORMAT + "'";
		try {
			parsedDate = parserSDF.parse(jsonStr);
		} catch (ParseException e) {
			LOGGER.error(failNotice + ": " + e.getMessage());
		}
		if (parsedDate == null) {
			LOGGER.info(failNotice);
		}
		return parsedDate;
	}

	/**
	 * Overwrite the deserializer for date strings in the form of a
	 * unix timestamp (1293836400).
	 *
	 * This method will also try to interpret strings like "30.04.2013 13:43:55"
	 * as dates.
	 */
	@Override
	public Date deserialize(JsonParser jsonParser, DeserializationContext context)
			throws IOException, JsonProcessingException {

		Date deserializedDate = null;
		String s = jsonParser.getText();

		if ( DateDeserializer.looksLikeFormattedDateString(s) ) {
			deserializedDate = DateDeserializer.parseCommonlyFormattedDate(s);
		} else {
			String failNotice = "Failed to interpret string '" + s +
					"' as java.lang.Long. That string did not look like a " +
					"formatted date either.";
			try {
				Long number = new Long(s);
				deserializedDate = new Date(number);
			} catch (NumberFormatException e) {
				LOGGER.error(failNotice + " : " + e.getMessage());
			}
		}
		return deserializedDate;
	}

}
