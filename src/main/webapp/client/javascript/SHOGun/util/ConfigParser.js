/*
 * Copyright (C) 2012  terrestris GmbH & Co. KG, info@terrestris.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.-
 *
 * @author terrestris GmbH & Co. KG <info@terrestris.de>
 * 
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
