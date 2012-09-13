package de.terrestris.shogun.serializer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A serializer that can handle JTS geometries.
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
public class WKTSerializer extends JsonSerializer<Geometry> {

    /**
     * Overwrite the original serialize and return a WKT of the JTS geometry. 
     */
    @Override
    public void serialize(Geometry jtsGeometry, JsonGenerator jgen,
            SerializerProvider provider) throws IOException,
            JsonProcessingException {
        // returns the geometry as well known text
        jgen.writeString(jtsGeometry.toText());
    }

}