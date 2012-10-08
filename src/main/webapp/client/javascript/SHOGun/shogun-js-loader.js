/**
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
 */


/*
 * JS loader inspired by https://github.com/geoext/GXM/blob/master/lib/GXM.loader.js
 * 
 * closure to get a local copies of window & document 
 * and have 'undef' be 'undefined' 
 */
(function(window, document,  undef){
    if (window.SHOGun !== undef &&
        window.SHOGun.files !== undef &&
        window.Ext !== undef) {
        var jsfiles = window.SHOGun.files,
            allScriptTags = [];
            host = "";    
    
        Ext.each(jsfiles, function(jsfile) {
            allScriptTags.push(
                "<script type='text/javascript' src='" +
                host + jsfile + 
                "'></script>"
            );
        });
        
        document.write(allScriptTags.join(""));
    }
})(window, document);