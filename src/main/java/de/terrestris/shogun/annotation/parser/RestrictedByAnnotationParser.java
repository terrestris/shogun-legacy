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
