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
Ext.ns('SHOGun.widget');

/**
 * A combo box offering all scales of given map.
 * By selecting a scale the given map zooms
 */
SHOGun.widget.ScaleChooserComboBox = Ext.extend(Ext.form.ComboBox, {
    
    /**
     * The store holding all scales.
     * Will be created/filled in constructor
     */
    store: null,
    
    /**
     * the map (OL) to interact with
     */
    map: null,
    
    /**
     * the text if nothing is selected
     */
    emptyText: "Choose Scale",

    /**
     * the width of the combo box
     */
    width: 150,
    
    /**
     * 
     */
    tpl: '<tpl for="."><div class="x-combo-list-item">1 : {[parseInt(values.scale)]}</div></tpl>',
    
    /**
     * 
     */
    editable: false,
    
    /**
     * needed so that the combo box doesn't filter by its current content
     */
    triggerAction: 'all',
    
    /**
     * keep the combo box from forcing a lot of unneeded data refreshes
     */
    mode: 'local',
    
    /**
     * initializes this component
     */
    initComponent: function() {
            
        var me = this;
        
        // apply conf
        Ext.apply(this, this.initialConfig);

        // create a store with all scales
        me.store = new GeoExt.data.ScaleStore({
            map: me.map
        });
        
        // register on select: zoom the map to selected scale
        me.on('select', function(combo, record, index){
            me.map.zoomTo(record.data.level);
        }, me);
        
        // adjust displayed value if map changes
        me.map.events.register('zoomend', this, function(){
            var scale = me.store.queryBy(function(record){
                return me.map.getZoom() == record.data.level;
            });
            
            if (scale.length > 0) {
                scale = scale.items[0];
                me.setValue("1 : " + parseInt(scale.data.scale, 10));
            }
            else {
                if (!me.rendered) {
                    return;
                }
                me.clearValue();
            }
        });
        
        // call parent
        SHOGun.widget.ScaleChooserComboBox.superclass.initComponent.apply(this, arguments);
            
    } 
});

Ext.reg('shogun_scalechooser_combobox', SHOGun.widget.ScaleChooserComboBox);
