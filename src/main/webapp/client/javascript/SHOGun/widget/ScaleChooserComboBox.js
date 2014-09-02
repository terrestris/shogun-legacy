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
