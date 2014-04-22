/**
 *
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
