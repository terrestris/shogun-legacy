package de.terrestris.shogun.deserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 * Class responsible for deserializing a date.
 *
 * Makes a Date object out of an UNIX timestamp or a formatted date string.
 *
 * @author terrestris GmbH & Co. KG
 *
 */
public class DateDeserializer extends JsonDeserializer<Date> {

	private static Logger LOGGER = Logger.getLogger(DateDeserializer.class);

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
