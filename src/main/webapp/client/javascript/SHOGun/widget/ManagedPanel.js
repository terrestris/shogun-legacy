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
 */
Ext.ns('SHOGun.widget');

/**
 * Ext.Panel with a member variable managed, so this component
 * could be 
 */
SHOGun.widget.ManagedPanel = Ext.extend(Ext.Panel, {
    
    /**
     * 
     */
    initComponent: function(){
        var constants = {
            userConf: null,
            rightTo: null,
            managed: true
        }; // eo config object
        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, constants));
        SHOGun.widget.ManagedPanel.superclass.initComponent.apply(this, arguments);
    } // eo function initComponent
});
Ext.reg('shogun_managedpanel', SHOGun.widget.ManagedPanel);
