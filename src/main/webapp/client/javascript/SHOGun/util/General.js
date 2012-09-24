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
 * @author terrestris GmbH & Co. KG
 */
Ext.ns('SHOGun.util.General');

/**
 * This file contains the SHOGun.util.General-singleton object with general
 * methods that are shared between modules and widgets.
 */
Ext.apply(SHOGun.util.General, {
    /**
     * An helper function to log to an available console (e.g. the one provided
     * by the Firefox extension Firebug). Checks the setting of
     * SHOGun.config.DEBUG_MODE first whether we are in debug mode.
     * This method can be called with multiple parameters and should not throw
     * an error even if we don't have a console (e.g. in older versions of
     * Internet Explorer)
     *
     * @param {Mixed} an entity to log
     * @param {Mixed} ...
     * @return undefined
     */
    log: function log(){
        if (SHOGun.config.DEBUG_MODE === true && typeof console !== 'undefined' && typeof console.log !== 'undefined' && typeof console.log !== 'object') {
            // console.log.apply(this, arguments);
            Ext.each(arguments, function(arg){
               console.log(arg);
            });
        }
    },
    
    /**
     * Finds a menu element in a given parent that has a matching
     * text-attribute.
     *
     * @param {Ext.Toolbar|Ext.menuMenu} The parent to search (Can actually be
     *     any object that implemenmts a method "find('text', textValue)" and
     *     returns an array of matches)
     * @param {String} the text to search for
     * @return {Mixed} The element if it found or undefined.
     * @see SHOGun.util.General.log used in case an illegal call was issued
     */
    findFirstMenuElementByText: function findFirstMenuElementByText(parent, text){
        var matches, first;
        if (parent && parent.find && Ext.isFunction(parent.find)) {
            matches = parent.find('text', text);
            if (matches && matches.length > 0) {
                first = matches[0];
            }
        }
        else {
            SHOGun.util.General.log('WARNING: findFirstMenuElementByText called with parent that has no method find()');
        }
        return first;
    },
    
    /**
     * Dynamically loads a JavaScript file into the page by adding a
     * <script>-tag to the pages head.
     *
     * @return undefined
     */
    loadJavaScriptFile: function(fullfilename){
        var head = document.getElementsByTagName("head")[0];
        var script = document.createElement('script');
        script.setAttribute("type", "text/javascript");
        script.setAttribute("src", fullfilename);
        script.onload = script.onreadystatechange = function(){
            head.removeChild(script);
        };
        head.appendChild(script);
    },
    
    /**
     * Checks the browser language and loads a matching translation file if we
     * have one.
     *
     * This is far from being perfect, as we check navigator.language, which
     * actually tells you which localised version of the browser is installed
     * and not what the user set as his prefered languages.
     *
     * These can be derived from the http-header "Accept-Language", which is
     * unavailable to JavaScript. This method could be enhanced to call a simple
     * server-side script that spits out the valueof that particular header.
     *
     * @see SHOGun.util.General.loadJavaScriptFile
     */
    translateAccordingToBrowserLanguage: function(){
        var implementedLanguages = ['de', 'en'];
        var baseLang = '';
        if (navigator && navigator.language) {
            baseLang = navigator.language.split("-")[0];
        }
        var filename = "javascript/Terrestris/language/" + baseLang + ".js";
        SHOGun.util.General.loadJavaScriptFile(filename);
    },
    
    
    /**
     * Returns the translation value
     *
     * @param {Object} entity
     * @param {Object} key
     */
    getText: function(entity, key){
        var isDef = Ext.isDefined || function(){return false;},
            log = SHOGun.util.General.log || function(){},
            debugPrefix = 'NOTICE [SHOGun.util.General.getText()]: ',
            debugSuffix = ' is undefined.\n\tTranslation is falling back to translation "' + key + '" for entity "' + entity + '[\'' + key + '\']".',
            languageMapping,
            language,
            dictionary,
            entityDict,
            text = key; // if all else fails, we return the passed key
        
        if (isDef(SHOGun.language) && isDef(SHOGun.language.LANGUAGE_MAPPING)){
            languageMapping = SHOGun.language.LANGUAGE_MAPPING;
            if (isDef(SHOGun.config) && isDef(SHOGun.config.LANGUAGE)){
                language = SHOGun.config.LANGUAGE;
                if (isDef(languageMapping[language])) {
                    dictionary = languageMapping[language];
                    if (isDef(dictionary[entity])) {
                        entityDict = dictionary[entity];
                        if (isDef(entityDict[key])) {
                            text = entityDict[key];
                        } else {
                            log(debugPrefix + 'translation for key "' + key + '"' + debugSuffix);
                        }
                    } else {
                        log(debugPrefix + 'language mapping for "' + language + '"' + debugSuffix);
                    }
                } else {
                    log(debugPrefix + 'language mapping for "' + language + '"' + debugSuffix);
                }
            } else {
                log(debugPrefix + 'SHOGun.config.LANGUAGE' + debugSuffix);
            }
        } else {
            log(debugPrefix + 'SHOGun.language.LANGUAGE_MAPPING' + debugSuffix);
        }
        
        return text;
        
        //Mehrsprachigkeit?
        // return SHOGun.language.LANGUAGE_MAPPING[SHOGun.config.LANGUAGE][entity][key];
    },
    /**
     *
     *
     * @param {Object} value
     */
    createHtmlLinkFromText: function(value){
        return '<a href="' + value + '">Zur Meldung</a>';
    },
    
    /**
     *
     * @param {Object} anq
     */
    callHelp: function(anq){
        var helpUrl = "./help/hilfe.html#" + anq;
        var win = window.open(helpUrl, "GMOnlineHilfe", "width=800,height=550,scrollbars=yes,left=200,top=150,resizable=yes,location=no,menubar=no,status=no,dependent=yes");
        if (win) {
            win.focus();
        }
        else {
            infoAlert('Um die Online-Hilfe öffnen zu können, sollten Sie etwaige Popup-Blocker deaktivieren.');
        }
    },
    
    /**
     *
     * @param {Object} anq
     */
    callManual: function(){
        var manualUrl = "./help/hilfe.html";
        var win = window.open(manualUrl, "GMOnlineHandbuch", "width=800,height=550,scrollbars=yes,left=200,top=150,resizable=yes,location=no,menubar=no,status=no,dependent=yes");
        if (win) {
            win.focus();
        }
        else {
            infoAlert('Um das Online-Handbuch öffnen zu können, sollten Sie etwaige Popup-Blocker deaktivieren.');
        }
    },
    
    /**
     * A function to create a tree node displaying a legend via WMS GetLengendGraphic
     *
     * @param {Object} attr
     */
    createTreeNodeWithWmsLegend: function(attr){
        // add a url legend to each node created
        var item = {};
        var layer = attr.layer;
        if (layer instanceof OpenLayers.Layer.WMS) {
            item = {
                xtype: 'gx_wmslegend',
                layerRecord: Ext.getCmp('app-mappanel').layers.getByLayer(layer),
                showTitle: false,
                cls: "legend " + (layer.visibility ? 'visible-legend' : 'hidden-legend'),
                baseParams: {
                    FORMAT: 'image/png'
                }
            };
        }
        attr.component = item;
        var node = GeoExt.tree.LayerLoader.prototype.createNode.call(this, attr);
        node.addListener('checkchange', function(node, checked){
            var legendElem = node.component.el;
            if (checked) {
                legendElem.removeClass('hidden-legend');
                legendElem.addClass('visible-legend');
            }
            else {
                legendElem.removeClass('visible-legend');
                legendElem.addClass('hidden-legend');
            }
        });
        return node;
    },
    
    /**
     * A function to create a tree node displaying an icon as legend
     *
     * @param {Object} attr
     */
    createTreeNodeWithUrlLegend: function(attr){
        // add a url legend to each node created
        var item = {};
        var layer = attr.layer;
        
        // add a url legend to each node created
        var layerRecord = Ext.getCmp('app-mappanel').layers.getByLayer(layer);
        
        var legendXType = 'gx_urllegend';
        layerRecord.set("legendURL", SHOGun.ajax.GetLegendGraphics + layer.params.LAYERS.toLowerCase() + '_n_map.png');
        layerRecord.set("legendURLsmall", SHOGun.ajax.GetLegendGraphicsSmall + layer.params.LAYERS.toLowerCase() + '_n_map.png');
        layerRecord.set("forceActiveLegend", true);
        item = {
            xtype: legendXType,
            layerRecord: layerRecord,
            showTitle: false,
            // toggle for inline/above icon
            //            cls: "legend " + (layer.visibility ? 'visible-legend' : 'hidden-legend')
            cls: "legend hidden-legend"
        };
        attr.component = item;
        
        // attr.cls = "terrestris-tree-node-" + layerRecord.name;
        attr.iconCls = "terrestris-tree-node-icon-" + layerRecord.name;
        
        //log(layerRecord);
        
        var node = GeoExt.tree.LayerLoader.prototype.createNode.call(this, attr);
        
        // setting special CSS-classes for legends:
        
        node.addListener('checkchange', function(node, checked){
            var legendElem = node.component.el;
            if (checked) {
                // toggle for inline/above icon
                //                legendElem.removeClass('hidden-legend');
                //                legendElem.addClass('visible-legend');
                legendElem.removeClass('visible-legend');
                legendElem.addClass('hidden-legend');
                node.ui.iconNode.style.opacity = '1.0';
            }
            else {
                legendElem.removeClass('visible-legend');
                legendElem.addClass('hidden-legend');
                node.ui.iconNode.style.opacity = '0.3';
            }
        });
        // toggle for inline/above icon
        node.addListener('rendernode', function(node){
            node.ui.iconNode.style.backgroundImage = 'url(' + layerRecord.data.legendURLsmall + ')';
            node.ui.iconNode.style.opacity = layer.visibility ? '1.0' : '0.3';
        });
        
        return node;
    },
    
    /**
     * Toggles the state of all child nodes of a tree node
     *
     * @param {Object} node
     * @param {Object} state
     */
    
    toggleAllChilds: function(node, state){
        if (state == true) {
            if (this.expanded == false) {  
                this.toggle();
            }
            node.eachChild( function() {
                if(this.leaf){
                    this.layer.setVisibility(true);
                }
                else{
                    this.expand();
                    var tmpNode = this;
                    //TODO: find a better way to check the group checkbox
                    this.attributes.checked = true;
                    this.ui.checkbox.checked = true;
                    SHOGun.util.General.toggleAllChilds(tmpNode, true);
                }
                
            });
        }
        else {
            if (this.expanded == true) {  
                this.toggle();
            }
            node.eachChild( function() {
                if(this.leaf){
                    this.layer.setVisibility(false);
                }
                else{
                    var tmpNode = this;
                    //TODO: find a better way to uncheck the group checkbox
                    this.attributes.checked = false;
                    this.ui.checkbox.checked = false;
                    SHOGun.util.General.toggleAllChilds(tmpNode, false);
                }
                
            });
        }
    },
    /**
     * Gives back base-Url
     */
    getBaseURL: function(){
        var url = location.href; // entire url including querystring - also: window.location.href;
        var baseURL = url.substring(0, url.indexOf('/', 14));
        
// JW 20111202: Commented some of the following parts, they are unnecessary, doesnt matter if localhost or not, always append the baseLocalURL (projectname)        
//        if (baseURL.indexOf('http://localhost') != -1) {
            // Base Url for localhost
            var url = location.href; // window.location.href;
            var pathname = location.pathname; // window.location.pathname;
            var index1 = url.indexOf(pathname);
            var index2 = url.indexOf("/", index1 + 1);
            var baseLocalUrl = url.substr(0, index2);
            
            return baseLocalUrl + "/";
//        }
//        else {
//            // Root Url for domain name
//            return baseURL + "/";
//        }
    },
    /**
     * 
     * Usage:         getMD5Hash ( string str [, bool raw_output ][, bool hexcase ][, number charset {8(ASCII):16(UNICODE)} ] )
     * Examples:     var MD5Hash = getMD5Hash("testtext"); //returns 0ea2d99c9848117666c38abce16bb43e
     *                 var MD5Hash = getMD5Hash("testtext",false,true); //returns 0EA2D99C9848117666C38ABCE16BB43E
     *                 var MD5Hash = getMD5Hash("testtext",true); //returns binary string 
     * Source from: http://www.sencha.com/forum/showthread.php?28460-Ext.util.MD5
     * @param {Object} s
     * @param {Object} raw
     * @param {Object} hexcase
     * @param {Object} chrsz
     */
    getMD5Hash: function(s, raw, hexcase, chrsz){
        raw = raw || false;
        hexcase = hexcase || false;
        chrsz = chrsz || 8;
        
        function safe_add(x, y){
            var lsw = (x & 0xFFFF) + (y & 0xFFFF);
            var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
            return (msw << 16) | (lsw & 0xFFFF);
        }
        function bit_rol(num, cnt){
            return (num << cnt) | (num >>> (32 - cnt));
        }
        function md5_cmn(q, a, b, x, s, t){
            return safe_add(bit_rol(safe_add(safe_add(a, q), safe_add(x, t)), s), b);
        }
        function md5_ff(a, b, c, d, x, s, t){
            return md5_cmn((b & c) | ((~ b) & d), a, b, x, s, t);
        }
        function md5_gg(a, b, c, d, x, s, t){
            return md5_cmn((b & d) | (c & (~ d)), a, b, x, s, t);
        }
        function md5_hh(a, b, c, d, x, s, t){
            return md5_cmn(b ^ c ^ d, a, b, x, s, t);
        }
        function md5_ii(a, b, c, d, x, s, t){
            return md5_cmn(c ^ (b | (~ d)), a, b, x, s, t);
        }
        
        function core_md5(x, len){
            x[len >> 5] |= 0x80 << ((len) % 32);
            x[(((len + 64) >>> 9) << 4) + 14] = len;
            var a = 1732584193;
            var b = -271733879;
            var c = -1732584194;
            var d = 271733878;
            for (var i = 0; i < x.length; i += 16) {
                var olda = a;
                var oldb = b;
                var oldc = c;
                var oldd = d;
                a = md5_ff(a, b, c, d, x[i + 0], 7, -680876936);
                d = md5_ff(d, a, b, c, x[i + 1], 12, -389564586);
                c = md5_ff(c, d, a, b, x[i + 2], 17, 606105819);
                b = md5_ff(b, c, d, a, x[i + 3], 22, -1044525330);
                a = md5_ff(a, b, c, d, x[i + 4], 7, -176418897);
                d = md5_ff(d, a, b, c, x[i + 5], 12, 1200080426);
                c = md5_ff(c, d, a, b, x[i + 6], 17, -1473231341);
                b = md5_ff(b, c, d, a, x[i + 7], 22, -45705983);
                a = md5_ff(a, b, c, d, x[i + 8], 7, 1770035416);
                d = md5_ff(d, a, b, c, x[i + 9], 12, -1958414417);
                c = md5_ff(c, d, a, b, x[i + 10], 17, -42063);
                b = md5_ff(b, c, d, a, x[i + 11], 22, -1990404162);
                a = md5_ff(a, b, c, d, x[i + 12], 7, 1804603682);
                d = md5_ff(d, a, b, c, x[i + 13], 12, -40341101);
                c = md5_ff(c, d, a, b, x[i + 14], 17, -1502002290);
                b = md5_ff(b, c, d, a, x[i + 15], 22, 1236535329);
                a = md5_gg(a, b, c, d, x[i + 1], 5, -165796510);
                d = md5_gg(d, a, b, c, x[i + 6], 9, -1069501632);
                c = md5_gg(c, d, a, b, x[i + 11], 14, 643717713);
                b = md5_gg(b, c, d, a, x[i + 0], 20, -373897302);
                a = md5_gg(a, b, c, d, x[i + 5], 5, -701558691);
                d = md5_gg(d, a, b, c, x[i + 10], 9, 38016083);
                c = md5_gg(c, d, a, b, x[i + 15], 14, -660478335);
                b = md5_gg(b, c, d, a, x[i + 4], 20, -405537848);
                a = md5_gg(a, b, c, d, x[i + 9], 5, 568446438);
                d = md5_gg(d, a, b, c, x[i + 14], 9, -1019803690);
                c = md5_gg(c, d, a, b, x[i + 3], 14, -187363961);
                b = md5_gg(b, c, d, a, x[i + 8], 20, 1163531501);
                a = md5_gg(a, b, c, d, x[i + 13], 5, -1444681467);
                d = md5_gg(d, a, b, c, x[i + 2], 9, -51403784);
                c = md5_gg(c, d, a, b, x[i + 7], 14, 1735328473);
                b = md5_gg(b, c, d, a, x[i + 12], 20, -1926607734);
                a = md5_hh(a, b, c, d, x[i + 5], 4, -378558);
                d = md5_hh(d, a, b, c, x[i + 8], 11, -2022574463);
                c = md5_hh(c, d, a, b, x[i + 11], 16, 1839030562);
                b = md5_hh(b, c, d, a, x[i + 14], 23, -35309556);
                a = md5_hh(a, b, c, d, x[i + 1], 4, -1530992060);
                d = md5_hh(d, a, b, c, x[i + 4], 11, 1272893353);
                c = md5_hh(c, d, a, b, x[i + 7], 16, -155497632);
                b = md5_hh(b, c, d, a, x[i + 10], 23, -1094730640);
                a = md5_hh(a, b, c, d, x[i + 13], 4, 681279174);
                d = md5_hh(d, a, b, c, x[i + 0], 11, -358537222);
                c = md5_hh(c, d, a, b, x[i + 3], 16, -722521979);
                b = md5_hh(b, c, d, a, x[i + 6], 23, 76029189);
                a = md5_hh(a, b, c, d, x[i + 9], 4, -640364487);
                d = md5_hh(d, a, b, c, x[i + 12], 11, -421815835);
                c = md5_hh(c, d, a, b, x[i + 15], 16, 530742520);
                b = md5_hh(b, c, d, a, x[i + 2], 23, -995338651);
                a = md5_ii(a, b, c, d, x[i + 0], 6, -198630844);
                d = md5_ii(d, a, b, c, x[i + 7], 10, 1126891415);
                c = md5_ii(c, d, a, b, x[i + 14], 15, -1416354905);
                b = md5_ii(b, c, d, a, x[i + 5], 21, -57434055);
                a = md5_ii(a, b, c, d, x[i + 12], 6, 1700485571);
                d = md5_ii(d, a, b, c, x[i + 3], 10, -1894986606);
                c = md5_ii(c, d, a, b, x[i + 10], 15, -1051523);
                b = md5_ii(b, c, d, a, x[i + 1], 21, -2054922799);
                a = md5_ii(a, b, c, d, x[i + 8], 6, 1873313359);
                d = md5_ii(d, a, b, c, x[i + 15], 10, -30611744);
                c = md5_ii(c, d, a, b, x[i + 6], 15, -1560198380);
                b = md5_ii(b, c, d, a, x[i + 13], 21, 1309151649);
                a = md5_ii(a, b, c, d, x[i + 4], 6, -145523070);
                d = md5_ii(d, a, b, c, x[i + 11], 10, -1120210379);
                c = md5_ii(c, d, a, b, x[i + 2], 15, 718787259);
                b = md5_ii(b, c, d, a, x[i + 9], 21, -343485551);
                a = safe_add(a, olda);
                b = safe_add(b, oldb);
                c = safe_add(c, oldc);
                d = safe_add(d, oldd);
            }
            return [a, b, c, d];
        }
        function str2binl(str){
            var bin = [];
            var mask = (1 << chrsz) - 1;
            for (var i = 0; i < str.length * chrsz; i += chrsz) {
                bin[i >> 5] |= (str.charCodeAt(i / chrsz) & mask) << (i % 32);
            }
            return bin;
        }
        function binl2str(bin){
            var str = "";
            var mask = (1 << chrsz) - 1;
            for (var i = 0; i < bin.length * 32; i += chrsz) {
                str += String.fromCharCode((bin[i >> 5] >>> (i % 32)) & mask);
            }
            return str;
        }
        function binl2hex(binarray){
            var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
            var str = "";
            for (var i = 0; i < binarray.length * 4; i++) {
                str += hex_tab.charAt((binarray[i >> 2] >> ((i % 4) * 8 + 4)) & 0xF) + hex_tab.charAt((binarray[i >> 2] >> ((i % 4) * 8)) & 0xF);
            }
            return str;
        }
        return (raw ? binl2str(core_md5(str2binl(s), s.length * chrsz)) : binl2hex(core_md5(str2binl(s), s.length * chrsz)));
    },

    /**
     * The "neue Geometrie abgreifen" handler
     * 
     * @param {Ext.Button} button
     * @param {Boolean} isDigitizing (true|false), this is a toggle button
     * @param {String} objektart
     * @param {String} winid
     */
    digitizeGeometryButtonHandler: function (button, isDigitizing, objektart, winid){
        // Kartenauswahl deaktivieren
        // alten Original-Zustands Text ablegen, falls nicht gesetzt:
        if (!Ext.isDefined(button.originalText)) {
            button.originalText = button.getText();
        }
        // alten Original-Zustands Tooltip ablegen, falls nicht gesetzt:
        if (!Ext.isDefined(button.originalTooltip)) {
            button.originalTooltip = button.tooltip;
        }
        var windowId = (Ext.isDefined(winid) && !Ext.isEmpty(winid)) ? winid : 'detailWindow' + objektart;
        var win = Ext.getCmp(windowId);
        var mapPanel = Ext.getCmp('app-mappanel');
        var tbar = mapPanel.getTopToolbar();
        
        var removeDigitizePointToolbarItem = function(win){
            var digitizeToolbarItem = Ext.getCmp('digitizePointOnMap');
            if (digitizeToolbarItem) {
                digitizeToolbarItem.baseAction.control.deactivate();
                digitizeToolbarItem.baseAction.control.destroy();
                digitizeToolbarItem.destroy();
    //            enableMapSelect();
            }
        };
        
        var digitizeToolbarItem = Ext.getCmp('digitizePointOnMap');
        if (digitizeToolbarItem) {
            removeDigitizePointToolbarItem(win);
        }
        
        // This is a toggle Button
        if (isDigitizing) {
    //        disableMapSelect();
            // Button Text ändern
            button.setText('Geometrie abgreifen beenden');
            // neuen Tooltip zuweisen
            button.setTooltip('Erneut klicken, um das Digitalisieren zu beenden.');
            // Action erzeugen
            digitizeToolbarItem = new GeoExt.Action({
                control: new OpenLayers.Control.Click({
                    handlerOptions: {
                        'double': false,
                        'stopSingle': true
                    },
                    onClick: function(evt){
                        var lonlat = this.map.getLonLatFromViewPortPx(evt.xy);
                        var point = new OpenLayers.Geometry.Point(lonlat.lon, lonlat.lat);
                        var feature = new OpenLayers.Feature.Vector(point);
                        var format = new OpenLayers.Format.WKT();
                        var wkt = format.write(feature);
    //                    Ext.Msg.alert('', 'Der Punkt wurde an der Koordinate ' + lonlat.lon + ', ' + lonlat.lat + ' platziert. Die Karte wird nach Betätigen von Speichern aktualisiert.');
                        // Event feuern, dass sich die Geometry geändert hat
                        // Event gebunden, so dass sich das Formular anpasst
                        button.fireEvent('newPointGeometry', this, wkt);
                    }
                }),
                map: map,
                enableToggle: true,
                toggleGroup: 'draw',
                icon: "images/map_icons/draw_point_off.png",
                tooltip: "Punkt auf der Karte abgreifen",
                text: 'Punkt abgreifen',
                id: 'digitizePointOnMap',
                pressed: true
            });
            // und der Toolbar hinzufügen
            tbar.insert(0, digitizeToolbarItem);
            // Toolbar neuzeichnen
            tbar.doLayout();
            // aktiviere das Toolbarelement
            digitizeToolbarItem.execute();
            // Events auf window registrieren:
            //   - bei close-Event Control entfernen
            //   - destroy dito
            win.on('close', removeDigitizePointToolbarItem);
            win.on('destroy', removeDigitizePointToolbarItem);
        }
        else {
            if (digitizeToolbarItem) {
                removeDigitizePointToolbarItem(win);
            }
            // Button Text ändern
            button.setText(button.originalText);
            // neuen Tooltip zuweisen
            button.setTooltip(button.originalTooltip);
            // close/destroy hander des windows entfernen
            win.un('close', removeDigitizePointToolbarItem);
            win.un('destroy', removeDigitizePointToolbarItem);
        }
    }, // end of function "digitizeGeometryButtonHandler"
    
    /**
     * 
     */
    getFieldTypeByColumnDataIndex: function(reader, dataindex) {
        
        var type = null;
        
        Ext.each(reader.meta.fields, function(field) {
            if (field.name === dataindex) {  
                type = field.type;
                return false;
            }
        });
        
        return type;
        
    }
});
