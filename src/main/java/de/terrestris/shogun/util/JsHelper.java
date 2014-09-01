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
 */
package de.terrestris.shogun.util;

/**
 * Helper class for modifying JavaScript files
 */
public class JsHelper {

	public static final String BASE_MODULEPATH = "\n            'javascript/module/";
	public static final String START_MODULE_INDICATOR = "-- START MODULE REPLACE --";
	public static final String END_MODULE_INDICATOR = "-- END MODULE REPLACE --";

	public static final String START_KEY_INDICATOR = "-- START KEY REPLACE --";
	public static final String END_KEY_INDICATOR = "-- END MODULE REPLACE --";

	public static final String CLASSNAME_UP = "__CLASSNAME__";
	public static final String CLASSNAME_LO = "#classname#";


	/**
	 * Modifies the JS loader conf file
	 *
	 * @param nextLine
	 * @param module_name
	 * @return
	 */
	public static Object processLoaderConfByLine(String nextLine, String module_name) {

		if (nextLine.contains(START_MODULE_INDICATOR)) {
			nextLine += BASE_MODULEPATH + module_name + ".js',";
		}
		else if(nextLine.contains(END_MODULE_INDICATOR)) {

		}
		nextLine += "\n";

		return nextLine;
	}

	/**
	 * Modifies the right-keys JS file
	 *
	 * @param nextLine
	 * @param module_name
	 * @return
	 */
	public static Object processLineRightKeys(String nextLine, String module_name) {
		if (nextLine.contains(START_KEY_INDICATOR)) {
			nextLine += "\n    'MODULE_" + module_name.toUpperCase() + "': '" + module_name + "',";
		}
		else if(nextLine.contains(END_KEY_INDICATOR)) {

		}
		nextLine += "\n";

		return nextLine;
	}

	/**
	 * Replaces the placeholder of the template module
	 * with the new of the new module
	 */
	public static String processLine(String strLine, String moduleName) {
		// use a second Scanner to parse the content of each line
		if (strLine.contains(CLASSNAME_UP)) {
			strLine = strLine.replace(CLASSNAME_UP, moduleName);

		}
		if (strLine.contains(CLASSNAME_LO)) {
			strLine = strLine.replace(CLASSNAME_LO, moduleName.toLowerCase());
		}

		strLine += "\n";

		return strLine;
	}

}
