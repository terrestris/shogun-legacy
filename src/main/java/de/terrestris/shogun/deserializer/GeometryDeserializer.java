package de.terrestris.shogun.deserializer;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

/**
 *
 * Class responsible for deserializing a Geometry
 * Makes a JTS Geometry object out of an WKT String
 *
 * @author terrestris GmbH & Co. KG
 *
 */
public class GeometryDeserializer extends JsonDeserializer<Geometry> {

	/**
	 * Overwrite the deserializer for WKT geometry strings
	 */
	@Override
	public Geometry deserialize(JsonParser jsonParser, DeserializationContext context)
			throws JsonProcessingException {

		Geometry geom = null;

		try {
			geom = new WKTReader().read(jsonParser.getText());
			if(geom != null) {
				//TODO: set the SRID dynamically
				geom.setSRID(900913);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return geom;
	}

}