package de.terrestris.shogun.deserializer;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 *
 * Class responsible for deserializing a date
 * Makes a Date object out of an UNIX timestamp
 *
 * @author terrestris GmbH & Co. KG
 *
 */
public class DateDeserializer extends JsonDeserializer<Date> {

	/**
	 * Overwrite the deserializer for date strings in the for of a
	 * unix timestamp (1293836400)
	 *
	 * TODO exception handling
	 */
	@Override
	public Date deserialize(JsonParser jsonParser, DeserializationContext context)
			throws IOException, JsonProcessingException {

		Date d = null;

		try {

			String s = jsonParser.getText();

			// 1293836400
			Long l = new Long(s);
			d = new Date(l);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return d;

	}

}