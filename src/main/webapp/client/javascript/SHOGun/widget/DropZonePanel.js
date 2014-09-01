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
