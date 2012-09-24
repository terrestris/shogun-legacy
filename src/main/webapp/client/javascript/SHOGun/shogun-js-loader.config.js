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
            'javascript/SHOGun/BaseApplication.js',


        ];
        // assign local configuration to glbal variable
        window.SHOGun = conf;
    }    
})(window);
