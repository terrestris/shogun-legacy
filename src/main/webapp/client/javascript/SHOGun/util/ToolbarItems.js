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
 * Util class offering toolbar items.
 * 
 * TODO make this as a static Ext component
 */
SHOGun.util.ToolbarItems = function(){
    
    /**
     * 
     */
    this.toolbarItems = null;
    
    /**
     * 
     */
    this.initToolbarItems = function(){
        
        this.toolbarItems = [];
        
        // ZoomIn
        var action = new GeoExt.Action({
            control: new OpenLayers.Control.ZoomIn(),
            map: map,
            icon: 'images/map_icons/icon_zoomin.png',
            tooltip: "Zoom In"
        });
        this.toolbarItems.push(action);
        
        // ZoomOut
        action = new GeoExt.Action({
            control: new OpenLayers.Control.ZoomOut(),
            map: map,
            icon: 'images/map_icons/icon_zoomout.png',
            tooltip: "Zoom Out"
        });
        this.toolbarItems.push(action);
        
        // ZoomBox
        action = new GeoExt.Action({
            control: new OpenLayers.Control.ZoomBox(),
            map: map,
            icon: 'images/map_icons/icon_zoombox.png',
            toggleGroup: "draw",
            allowDepress: true,
            pressed: false,
            group: "draw",
            checked: false,
            tooltip: "In freies Rechteck zoomen"
        });
        this.toolbarItems.push(action);
        
        this.toolbarItems.push(" ");
        
        // Pan
        action = new GeoExt.Action({
            icon: 'images/map_icons/icon_pan.png',
            control: new OpenLayers.Control.Navigation(),
            map: map,
            // button options
            toggleGroup: "draw",
            allowDepress: true,
            pressed: true,
            tooltip: "Ausschnitt verschieben",
            // check item options
            group: "draw",
            checked: true
        });
        this.toolbarItems.push(action);
        
        
        // ZoomToMaxExtent
        //TODO: we should make new OpenLayers.Control.ZoomToMaxExtent
        // behave as we expect it.
        action = new GeoExt.Action({
            // control: new OpenLayers.Control.ZoomToMaxExtent(),
            handler: function(){
              window.map.setCenter(new OpenLayers.LonLat(10.2, 48.9).transform(new OpenLayers.Projection("EPSG:4326"), map.getProjectionObject()), 2);  
            },
            map: map,
            icon: 'images/map_icons/icon_zoomfull.png',
            tooltip: "Zurück zur Übersicht"
        });
        this.toolbarItems.push(action);
        
        // SetCenter control, click button -> click map -> action
        action = new GeoExt.Action({
            control: new OpenLayers.Control.Click({
                handlerOptions: {
                    'double': false,
                    'stopSingle': true
                },
                onClick: function(evt){
                    //TODO: Wert als WKT in Formular
                    var lonlat = this.map.getLonLatFromViewPortPx(evt.xy);
                    map.setCenter(lonlat, map.getZoom());
                }
            }),
            map: map,
            enableToggle: true,
            toggleGroup: 'draw',
            icon: "images/map_icons/set_center.png",
            tooltip: 'Mittelpunkt setzen'
        });
        this.toolbarItems.push(action);  
        
        this.toolbarItems.push('-');

        // measure controls
        var measureOptions = {
            geodesic: true,
            handlerOptions: {
                citeCompliant: false,
                style: "default",
                layerOptions: {
                    styleMap: new OpenLayers.StyleMap({
                        'default': new OpenLayers.Style({
                            fillColor: '#646464',
                            fillOpacity: 0.4,
                            strokeColor: '#e00000',
                            strokeOpacity: 0.7,
                            strokeWidth: 3,
                            strokeLinecap: 'square',
                            strokeDashstyle: 'dashdot'
                        })
                    })
                },
                persist: true,
                // disable streaming Mode with SHIFT
                freehandToggle: null
            }
        };
        
        var lineMeasure = new OpenLayers.Control.Measure(OpenLayers.Handler.Path, measureOptions);
        var polygonMeasure = new OpenLayers.Control.Measure(OpenLayers.Handler.Polygon, measureOptions);
        
        lineMeasure.events.on({
            "measure": this.handleDistanceMeasurements,
            "measurepartial": this.clearMeasureResult, // or clearMeasureResult
            "activate": this.clearMeasureResult,
            "deactivate": this.clearMeasureResult
        });
        
        polygonMeasure.events.on({
            "measure": this.handleAreaMeasurements,
            "measurepartial": this.clearMeasureResult, // or clearMeasureResult,
            "activate": this.clearMeasureResult,
            "deactivate": this.clearMeasureResult
        });
        
        action = new GeoExt.Action({
            control: lineMeasure,
            map: map,
            enableToggle: true,
            toggleGroup: 'draw',
            icon: 'images/map_icons/draw_line_off.png',
            tooltip: 'Strecke messen'
        });
        this.toolbarItems.push(action);
        
        action = new GeoExt.Action({
            control: polygonMeasure,
            map: map,
            enableToggle: true,
            toggleGroup: 'draw',
            icon: 'images/map_icons/draw_polygon_off.png',
            tooltip: 'Fläche messen'
        });
        this.toolbarItems.push(action);
        
        this.toolbarItems.push("-");

        
        // History
        var ctrl = new OpenLayers.Control.NavigationHistory();
        map.addControl(ctrl);
        
        action = new GeoExt.Action({
            icon: "images/map_icons/resultset_previous.png",
            control: ctrl.previous,
            disabled: true,
            tooltip: "Vorheriger Ausschnitt"
        });
        this.toolbarItems.push(action);
        
        action = new GeoExt.Action({
            icon: "images/map_icons/resultset_next.png",
            control: ctrl.next,
            disabled: true,
            tooltip: "Folgender Ausschnitt"
        });
        this.toolbarItems.push(action);
        
        
        var app_scale_chooser = new SHOGun.widget.ScaleChooserComboBox({map: map});
        this.toolbarItems.push(app_scale_chooser);
        
        
        // add more actions here ...
        
        
//       this.toolbarItems.push('->');
        
    };
    
    /**
     * 
     */
    this.getItems = function() {
        if (this.toolbarItems === null) {  
            this.initToolbarItems();
        }
        return this.toolbarItems;
    };

    /**
     * Called by measure-control-handlers, updates measurePopup-Window.
     * Based upon code found here: http://map.globe.admin.ch/
     * @access public
     */
    //TODO: window. replace global 
    window.renderMeasureResult = function(resultHTML){
        window.measurePopup = new Ext.Window({
            id: 'renderMeasureResultWindow',
            title: 'Messergebnis:',
            html: resultHTML,
            width: 150,
            plain: true
        });
        window.measurePopup.show();
    }; // end of function "renderMeasureResult"
    
    /**
     * The handler for area measurements.
     *
     * @see renderMeasureResult
     */    
    this.handleAreaMeasurements = function(event){
        var units = event.units;
        var measure = event.measure;
        var out = "";
        out += "Fl&auml;che: " + measure.toFixed(3) + " " + units + "<sup>2</sup>";
        window.renderMeasureResult(out);
    };// end of function "handleAreaMeasurements"
    
    /**
     * The handler for distance measurements.
     * 
     * @see renderMeasureResult
     */
    this.handleDistanceMeasurements = function(event){
        var units = event.units;
        var measure = event.measure;
        var out = "";
        out += "Strecke: " + measure.toFixed(3) + " " + units;
        window.renderMeasureResult(out);
    };// end of function "handleDistanceMeasurements"

    /**
     * Destroys measurePopup-Window.
     * 
     * @see global variable measurePopup
     */
    this.clearMeasureResult = function(){
        if (window.measurePopup && window.measurePopup.destroy) {
            window.measurePopup.destroy();
        }
    }; // end of function "clearMeasureResult"
    
};