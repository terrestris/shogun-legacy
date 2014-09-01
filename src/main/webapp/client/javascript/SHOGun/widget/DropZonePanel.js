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
 * A panel acting as dropzone for drag and drop components
 */
SHOGun.widget.DropZonePanel = Ext.extend(Ext.Panel, {
    margins: '0',
    border: true,
    autoScroll: true,
    defaults: {
        border: false
    },
    layout: 'fit',
    enableDD: true,
    split: true,
    //DARF nicht nur hier stehen, sondern MUSS auch in der InitialConfig ...new Panel({collapsible: true})... gesetzt werden!!!
    collapsible: true,
    ddGroup: null,
    
    /**
     * 
     */
    initComponent: function(){
        var constants = {
            helpId: null,
            userConf: null,
            rightTo: null
        }; // eo config object
        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, constants));
        
        SHOGun.widget.DropZonePanel.superclass.initComponent.apply(this, arguments);
    },
    listeners: {
        'render': function(panel){
            var dropTarget = panel;
            dropTarget.dropZone = new Ext.dd.DropZone(dropTarget.getEl(), {
                lastDropInfo: {
                    pos: -1,
                    match: null,
                    overSelf: false,
                    elem: null
                },
                ddGroup: this.ddGroup,
                
                // If the mouse is over a grid row, return that node. This is
                // provided as the "target" parameter in all "onNodeXXXX" node event handling functions
                getTargetFromEvent: function(e){
                    //log(arguments);
                    //return e.getTarget(myGridPanel.getView().rowSelector);
                },
                onContainerOver: function(ddProxy, event, data){
                    /**
                     * Helper method to find the insert position.
                     *TODO: refactor to a shareable method.
                     *
                     * @param {Object} dropTarget
                     * @param {Object} xy
                     */
                    var findPosInfo = function(dropTarget, xy){
                        // find insert position
                        var p, match = false, pos = 0, items = dropTarget.items.items, overSelf = false;
                        
                        for (var len = items.length; pos < len; pos++) {
                            p = items[pos];
                            var h = p.el.getHeight();
                            if (h === 0 && p.hidden === false) {
                                overSelf = true;
                            }
                            else 
                                if ((p.el.getY() + (h / 2)) > xy[1]) {
                                    match = true;
                                    break;
                                }
                        }
                        pos = (match && p ? pos : dropTarget.items.getCount()) + (overSelf ? -1 : 0);
                        
                        // set values to reference later:
                        var lastDropInfo = {
                            pos: pos,
                            match: match,
                            overSelf: overSelf,
                            elem: p
                        };
                        
                        return lastDropInfo;
                    };
                    // log(arguments);
                    // log('dropTarget', dropTarget);
                    var draggedPanel = data.panel;
                    // log('draggedPanel', draggedPanel);
                    
                    this.lastDropInfo = findPosInfo(dropTarget, event.getXY());
                    // var overElem = dropTarget.items.get(pos);
                    ddProxy.proxy.getProxy().setWidth('auto');
                    if (this.lastDropInfo.elem) {
                        ddProxy.proxy.moveProxy(this.lastDropInfo.elem.el.dom.parentNode, this.lastDropInfo.match ? this.lastDropInfo.elem.el.dom : null);
                    }
                    else {
                        log('Kein this.lastDropInfo.elem');
                    }
                },
                onContainerDrop: function(ddProxy, event, data){
                    //log(this.id);
                    var draggedPanel = data.panel;
                    if (draggedPanel && draggedPanel.parentPanelId) {
                        draggedPanel.parentPanelId = this.id;
                    }
                    
                    // works
                    ddProxy.proxy.getProxy().remove();
                    draggedPanel.el.dom.parentNode.removeChild(draggedPanel.el.dom);
                    
                    var pos = this.lastDropInfo.pos;
                    
                    if (pos !== false || pos !== -1) {
                        dropTarget.insert(pos, draggedPanel);
                    }
                    else {
                        dropTarget.add(draggedPanel);
                    }
                    
                    dropTarget.doLayout();
                    return true;
                }
            });
        }
    }
});
Ext.reg('shogun_dropzonepanel', SHOGun.widget.DropZonePanel);
