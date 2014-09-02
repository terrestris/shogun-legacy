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
 *
 * The client-side code of SHOGun partly depends on the ExtJS-framework (see
 * http://www.sencha.com/products/extjs, available separately under various
 * licensing options: http://www.sencha.com/products/extjs/licensing); it is
 * released in accordance with Sencha's Exception for Development Version 1.04
 * from January 18, 2013 (see http://www.sencha.com/legal/open-source-faq/
 * open-source-license-exception-for-development/).
 */
Ext.ns('SHOGun.util');

/**
 * Static methods parsing the confing of the backend and creating
 * needed objects due to config.
 */
SHOGun.util.ConfigParser = Ext.extend(Ext.util.Observable, {

    /**
     *
     */
    createLayers: function(layersArr) {
    
        var layers = layersArr || window.applicationConf.layer || [];
        var layerObjects = [];
        
        Ext.each(layers, function(layer) {
            if (layer instanceof OpenLayers.Layer) {
                layerObjects.push(layer);
            }
            else {
                if (layer.type === 'WMS') {
                    ol_layer = new OpenLayers.Layer.WMS(layer.name, layer.url, {
                        layers: layer.layers,
                        transparent: layer.transparent || 'false',
                        format: layer.format || 'image/png'
                    }, {
                        alwaysInRange: layer.alwaysInRange,
                        attribution: layer.attribution,
                        displayInLayerSwitcher: layer.displayInLayerSwitcher,
                        displayOutsideMaxExtent: layer.displayOutsideMaxExtent,
                        gutter: layer.gutter,
                        isBaseLayer: layer.isBaseLayer,
                        maxExtent: layer.maxExtent,
                        maxResolution: layer.maxResolution,
                        maxScale: layer.maxScale,
                        minExtent: layer.minExtent,
                        minResolution: layer.minResolution,
                        minScale: layer.minScale,
                        numZoomLevels: layer.numZoomLevels,
                        projection: layer.projection,
                        ratio: layer.ratio,
                        resolutions: layer.resolutions,
                        scales: layer.scales,
                        singleTile: layer.singleTile,
                        tileSize: layer.tileSize || new OpenLayers.Size(256, 256),
                        transitionEffect: layer.transitionEffect,
                        units: layer.units,
                        visibility: layer.visibility
                    });
                    layerObjects.push(ol_layer);
                }
                else 
                    if (layer.type == 'GeoJSON') {
                        var req = OpenLayers.Request.GET({
                            url: layer.url,
                            callback: function(req){
                                var response = req.responseText || '';
                                var geojson_format = new OpenLayers.Format.GeoJSON({                                
                                // internalProjection: new OpenLayers.Projection('EPSG:900913'),
                                // externalProjection: new OpenLayers.Projection('EPSG:4326')
                                });
                                ol_layer = new OpenLayers.Layer.Vector(layer.name);
                                var features = geojson_format.read(response);
                                ol_layer.addFeatures(features);
                            }
                        });
                        layerObjects.push(ol_layer);
                    }
            }
        });
        
        return layerObjects;
    },
    
    /**
     *
     */
    createMap: function(conf) {
    
        var mapConf = null;
        
//        mapConf = conf || window.applicationConf.map || {};
        
        mapConf = conf || {};
        
        /*
         * MaxExtent
         */
        maxExtent = null;
        if (Ext.isDefined(mapConf.maxExtent)) {
            if (mapConf.maxExtent instanceof OpenLayers.Bounds) {
                maxExtent = mapConf.maxExtent;
            }
            else if (Ext.isString(mapConf.maxExtent)) {
                mapConf.maxExtent = mapConf.maxExtent.replace(/ /g, "");
                maxExtent = OpenLayers.Bounds.fromString(mapConf.maxExtent);
            }
            else {
                maxExtent = new OpenLayers.Bounds(mapConf.maxExtent[0], mapConf.maxExtent[1], mapConf.maxExtent[2], mapConf.maxExtent[3]);
            }
        }
        
        /*
         * Projection
         */
        var projection = null;
        if (mapConf.projection instanceof OpenLayers.Projection) {
            projection = mapConf.projection;
        }
        else {
            projection = new OpenLayers.Projection(mapConf.projection);
        }
        
        /*
         * DisplayProjection
         */
        var displayProjection = null;
        if (!Ext.isEmpty(mapConf.projection) && !mapConf.displayProjection instanceof OpenLayers.Projection) {
            displayProjection = new OpenLayers.Projection(mapConf.displayProjection);
        }
        else if(mapConf.displayProjection instanceof OpenLayers.Projection){
            displayProjection = mapConf.displayProjection;
        }
        else {
            displayProjection = new OpenLayers.Projection("EPSG:4326");
        }
        
        /*
         * Center
         */
        var center = null;
        if (Ext.isDefined(mapConf.center)) {
            if (mapConf.center instanceof OpenLayers.LonLat) {
                center = mapConf.center;
            }
            else {
                center = new OpenLayers.LonLat(mapConf.center[0], mapConf.center[1]);
            }
        }
        
        /*
         * Controls
         */
        var defaultControls = [];
        var controls = null;
        if (Ext.isArray(mapConf.controls)) {
            
            // make OpenLayers objects from config string
            var controlObjectsFromConf = SHOGun.Config.createControls(mapConf.controls);
            // merge the default array with the created one
            controls = defaultControls.concat(controlObjectsFromConf);
        }
        else {
            controls = defaultControls;
        }
        
        // create OpenLayers map object 
        var map = new OpenLayers.Map({
            theme: null,
            allOverlays: mapConf.allOverlays || false,
            projection: projection,
            displayProjection: displayProjection,
            units: mapConf.units,
            numZoomLevels: mapConf.numZoomLevels,
            maxExtent: maxExtent,
            resolutions: mapConf.resolutions,
//            maxResolution: mapConf.maxResolution,                                                                                                    
//            center: center,
//            zoom: mapConf.zoom,
//            layers: SHOGun.Config.createLayers(),
            controls: controls
        });
        
        return map;
    },
    
    /**
     * 
     */
    createControls: function (controlNames){
    
        var controls = [];
        var controlnames = controlNames || window.applicationConf.map.controls || [];
        
        Ext.each(controlnames, function(name){
            if (Ext.isFunction(OpenLayers.Control[name])) {
                var constructor = OpenLayers.Control[name];
                controls.push(new constructor());
            }
        });
        
        return controls;
    }
    
});


/**
 * @class SHOGun.Config
 * @extends SHOGun.util.ConfigParser
 * A singleton instance of an {@link SHOGun.util.ConfigParser}.
 * @singleton
 */
SHOGun.Config = new SHOGun.util.ConfigParser({});
