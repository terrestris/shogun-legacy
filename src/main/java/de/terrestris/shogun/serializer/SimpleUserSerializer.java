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

		// Also serialize the name of the user into a field "ownerName"
		// TODO We shouldn't assume that we always are serializing
		//      an "owner" (hence "ownerName" might be not very good decision)
		// TODO In case we have multiple User.class entities that are serialized
		//      this might lead to an illegal (?) JSON, as duplicate keys might
		//      be produced. I do not know how all parsers deal with this.
		//      "The names within an object SHOULD be unique."
		//      http://www.ietf.org/rfc/rfc4627.txt
		String ownerName = null;
		if (value.getUser_firstname() != null) {
			ownerName = value.getUser_firstname();
		}
		if (value.getUser_lastname() != null) {
			if (ownerName == null) {
				ownerName = "";
			} else {
				ownerName += " ";
			}
			ownerName += value.getUser_lastname();
		}
		jgen.writeStringField("ownerName", ownerName);
	}

}
