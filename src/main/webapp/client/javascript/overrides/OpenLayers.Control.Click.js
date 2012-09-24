/*
 * 
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
 * @author terrestris GmbH & Co. KG
 * 
 */
OpenLayers.Control.Click =
  OpenLayers.Class(OpenLayers.Control, {
    defaultHandlerOptions: {
      'single': true,
      'double': true,
      'pixelTolerance': 0,
      'stopSingle': true,
      'stopDouble': true
    },

    /**
     * 
     */
    initialize: function(options) {
      var opts = options || {};
      this.handlerOptions = OpenLayers.Util.applyDefaults(
        opts.handlerOptions || {},
        this.defaultHandlerOptions
      );
      OpenLayers.Control.prototype.initialize.apply(
        this,
        arguments
      );
      this.handler = new OpenLayers.Handler.Click(
        this,
        {
          'click': this.onClick,
          'dblclick': this.onDblClick
        },
        this.handlerOptions
      );
    },

    /**
     * 
     */
    onClick: function( evt ) {
      // click func
      var lonlat = map.getLonLatFromViewPortPx(evt.xy);
      alert('Klick auf Koordinate: ' + lonlat.lon + ', ' + lonlat.lat);
    },

    /**
     * 
     */
    onDblClick: function( evt ) {
      // doubleClick func
      var lonlat = map.getLonLatFromViewPortPx(evt.xy);
      alert('Doppelklick auf Koordinate: ' + lonlat.lon + ', ' + lonlat.lat);
    },

    CLASS_NAME: "OpenLayers.Control.Click"
  }
);
