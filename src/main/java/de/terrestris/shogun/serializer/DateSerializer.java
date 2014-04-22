package de.terrestris.shogun.serializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * A serializer that can handle java.util.Date-instances.
 *
 * @author terrestris GmbH & Co. KG
 *
 */
public class DateSerializer extends JsonSerializer<Date> {

    /**
     * Overwrite the original serialize and return a String representation of
     * the java.util.Date-instance.
     */
    @Override
    public void serialize(Date value, JsonGenerator jgen,
            SerializerProvider provider) throws IOException,
            JsonProcessingException {

        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        jgen.writeString(formatter.format(new Date(value.getTime())));
    }
}
