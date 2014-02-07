package de.terrestris.shogun.serializer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import de.terrestris.shogun.model.User;

/**
 * A serializer that takes the user object of a model
 * and returns an id of the corresponding user.
 *
 * @author terrestris GmbH & Co. KG
 *
 */

public class SimpleUserSerializer extends JsonSerializer<User> {

	@Override
	public void serialize(User value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeNumber(value.getId());
		jgen.writeStringField("ownerName", value.getUser_firstname() + " " +
				value.getUser_lastname());
	}

}
