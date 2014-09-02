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

// closure to get a local copy of window and have 'undef' be 'undefined' 
(function(window, undef) {
    if (window.SHOGun === undef) {
        var conf = {
            files: null
        };
        conf.files = [
            
            // Initialize Utils 
            'javascript/SHOGun/util/General.js',
            'javascript/SHOGun/util/ConfigParser.js',
            'javascript/SHOGun/util/ToolbarItems.js',
            
            // Load Configs
            'configs/application-conf.js',
            'configs/right-keys.js',
            
            // Initialize Models 
//            'javascript/SHOGun/model/UserColModel.js',
//            'javascript/SHOGun/model/UserReader.js',
            
            // Initialize Templates
//            'javascript/SHOGun/template/BaseXTemplate.js', 
            
            // Initialize Viewport (Whole Browser window)     
            'javascript/SHOGun/widget/ManagedPanel.js',
            'javascript/SHOGun/widget/PinablePanel.js',
                    
            // Initialize Modules
            'javascript/SHOGun/module/BorderLayout.js',
            'javascript/SHOGun/module/MainComponent.js',
            'javascript/SHOGun/module/WindowManagerModule.js',
            
            //-- START MODULE REPLACE --
            'javascript/SHOGun/module/Login.js',
            'javascript/SHOGun/module/OverviewMapPanel.js',
            //-- END MODULE REPLACE --
            
            // Initialize Widgets
            'javascript/SHOGun/widget/DropZonePanel.js',
            'javascript/SHOGun/widget/DummyPanel.js',
            'javascript/SHOGun/widget/ScaleChooserComboBox.js',
            'javascript/SHOGun/widget/MenuToolbar.js',
            
            // Initialize Application
            'javascript/SHOGun/BaseApplication.js'


        ];
        // assign local configuration to glbal variable
        window.SHOGun = conf;
    }    
})(window);
