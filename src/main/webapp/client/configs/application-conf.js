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