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
Ext.ns('SHOGun.config');

/**
 * Common configs for the webgis application
 */
SHOGun.config.DEBUG_MODE = true;
SHOGun.config.LANGUAGE = 'DE';

Ext.ns('SHOGun.help');
SHOGun.help.HELP_KEYS = {
    'MAP': 'mapPanel',
    'LAYERTREE': 'tree',
    'LEGEND': 'legend',
    'PRINTPANEL': 'printPanel',
    'PRINT': 'printPanel',
    'ZOOMIN': 'zoomIn',
    'ZOOMOUT': 'zoomOut',
    'ZOOMBOXIN': 'zoomBoxIn',
    'PAN': 'pan',
    'LOGOUT': 'abmelden',
    'REDLINING': 'redlining',
    'HELP': 'hilfe',
    'FEATUREINFO': 'featureInfo',
    'NAVIGATIONNEXT': 'navigationNext',
    'NAVIGATIONPREVIOUS': 'navigationPrevious',
    'ZOOMTOSELECTION': 'zoomToSelection',
    'SETCENTER': 'setCenter',
    'MAXEXTENT': 'zoomMaxOut',
    'MEASUREPOLYGON': 'measurePolygon',
    'MEASURELINE': 'measureLine',
    'EXTERNALINFORMATION': 'externalinformation',
    'INTERNALINFORMATION': 'internalinformation',
    'SEARCHFILTER': 'searchfilter',
    'TRAVELWARNING': 'travelwarning',
    'DETAILFENSTERBUTTON': 'detailfensterbutton',
    'ADDRESS': 'address'
};

Date.patterns = {
    ISO8601Long: "Y-m-d H:i:s",
    ISO8601Middle: "Y-m-d H:i",
    ISO8601Short: "Y-m-d",
    ShortDate: "n/j/Y",
    LongDate: "l, F d, Y",
    FullDateTime: "l, F d, Y g:i:s A",
    MonthDay: "F d",
    ShortTime: "g:i A",
    LongTime: "g:i:s A",
    SortableDateTime: "Y-m-d\\TH:i:s",
    UniversalSortableDateTime: "Y-m-d H:i:sO",
    YearMonth: "F, Y",
    GermanWithTime: "d.m.Y H:i:s",
    UnixTimeStamp: "U"
};