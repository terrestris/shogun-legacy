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
Ext.ns('SHOGun.widget');

/**
 * Panel which is put hidden into the accordions as first element
 * in order to fix the bug of opening the first accordion with the full size
 * of the accordion panel
 */
SHOGun.widget.DummyPanel = function() {
    
    /**
     * get the dummy panel
     */
    this.getPanel = function(){
        return {
            title: 'dummy',
            hidden: true,
            height: 1,
            items: [],
            cls: 'empty'
        };
    };
};