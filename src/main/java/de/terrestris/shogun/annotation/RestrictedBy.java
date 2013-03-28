package de.terrestris.shogun.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * An annotation to mark a class, especially Hibernate models, as restricted.
 * So the access of a model-class could be secured by reading out the
 * restriction value set as parameter.
 *
 * For example:
 *
 * <code>
 *     @RestrictedBy(restriction = RestrictionType.GROUP)
 * </code>
 *
 * marks a model so it is only possible to access data of the same group.
 */
@Retention(RUNTIME)
public @interface RestrictedBy {
	RestrictionType restriction();
}
