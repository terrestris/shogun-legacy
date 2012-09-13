/**
 * 
 */
package de.terrestris.shogun.jsonmodel;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import de.terrestris.shogun.model.WmsMapLayer;

/**
 * 
 * @author terrestris GmbH & Co. KG
 * @author Marc Jansen
 * @author Christian Mayer
 * 
 * @version $Id$
 *
 */
@JsonAutoDetect
public class WmsMapLayerList {
	
    List<WmsMapLayer> wmsMapLayers;
    
    /**
     * 
     * @return List<WmsMapLayer>
     */
    public List<WmsMapLayer> getWmsMapLayers() {
	    return wmsMapLayers;
    }
    
    /**
     * 
     * @param wmsMapLayers
     */
    public void setWmsMapLayers(List<WmsMapLayer> wmsMapLayers) {
	     this.wmsMapLayers = wmsMapLayers ;
    }
}
