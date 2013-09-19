package de.terrestris.shogun.serializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import de.terrestris.shogun.model.BaseModelInterface;

/**
 * A serializer that takes sets of instances of {@link BaseModelInterface}
 * and returns a an array of the ids of the instances as serialisation value.
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
@SuppressWarnings("rawtypes")
public class LeanBaseModelSetSerializer extends JsonSerializer<Set> {

	@Override
	public void serialize(Set set, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartArray();
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			BaseModelInterface object = (BaseModelInterface) iterator.next();
			jgen.writeNumber(object.getId());
		}
		jgen.writeEndArray();
	}
    
}
