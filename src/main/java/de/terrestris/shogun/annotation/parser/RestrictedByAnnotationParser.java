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
package de.terrestris.shogun.annotation.parser;

import de.terrestris.shogun.annotation.RestrictedBy;
import de.terrestris.shogun.annotation.RestrictionType;
import de.terrestris.shogun.exception.ShogunParseException;


/**
 * Parser class offering static methods to access {@link RestrictedBy}
 * annotations during runtime.
 */
public class RestrictedByAnnotationParser {

	/**
	 * Checks if the passed model class has an {@link RestrictedBy} annotation
	 *
	 * @param modelClass class to be checked for {@link RestrictedBy} annotation
	 * @return flag showing if the given class has an annotation or not
	 */
	public static boolean hasAnnotation(Class<?> modelClass) {

		Class<RestrictedBy> annotationClass = RestrictedBy.class;

		if (modelClass != null && modelClass.isAnnotationPresent(annotationClass)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the {@link RestrictionType} of a class, if set.
	 *
	 * @param modelClass class to be checked for {@link RestrictedBy} annotation
	 * @return the {@link RestrictionType} for the given model class or null
	 * @throws ShogunParseException
	 */
	public static RestrictionType getRestrictionType(Class<?> modelClass){

		RestrictionType restrictionType = null;

		if (RestrictedByAnnotationParser.hasAnnotation(modelClass) == true) {

			RestrictedBy restrictionAnnotation = modelClass
					.getAnnotation(RestrictedBy.class);

			restrictionType = restrictionAnnotation.restriction();
		}

		return restrictionType;

	}

	/**
	 * Checks if the passed model class has an {@link RestrictedBy} annotation
	 * which is set the GROUP as RestrictionType
	 *
	 * @param modelClass class to be checked for {@link RestrictedBy} annotation
	 * @return flag showing if the given class is group dependent or not
	 */
	public static boolean isGroupDependent(Class<?> modelClass) {

		boolean isGroupDependent = false;
		RestrictionType restrictionType = RestrictedByAnnotationParser.getRestrictionType(modelClass);

		if (restrictionType != null) {

			switch (restrictionType) {
			case GROUP:
				isGroupDependent = true;
				break;
			default:
				isGroupDependent = false;
				break;
			}
		}

		return isGroupDependent;
	}
}
