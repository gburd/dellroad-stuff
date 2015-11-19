
/*
 * Copyright (C) 2011 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.stuff.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;

/**
 * Validation utility methods.
 */
public final class ValidationUtil {

    private ValidationUtil() {
    }

    /**
     * Validate the given object.
     *
     * <p>
     * This method simply creates a {@link ValidationContext} with the given root and invokes {@link ValidationContext#validate()}.
     *
     * @param obj object to validate
     * @param groups group(s) targeted for validation (if empty, defaults to {@link javax.validation.groups.Default})
     * @throws IllegalArgumentException if either paramter is null
     */
    public static <T> Set<ConstraintViolation<T>> validate(T obj, Class<?>... groups) {
        return new ValidationContext<T>(obj, groups).validate();
    }

    /**
     * Describe the validation errors in a friendly format.
     *
     * @param violations validation violations
     * @return description of the validation errors
     * @throws NullPointerException if {@code violations} is null
     */
    public static String describe(Set<? extends ConstraintViolation<?>> violations) {
        if (violations.isEmpty())
            return "  (no violations)";
        StringBuilder buf = new StringBuilder(violations.size() * 32);
        for (ConstraintViolation<?> violation : violations) {
            String violationPath = violation.getPropertyPath().toString();
            buf.append("  ");
            if (violationPath.length() > 0)
                buf.append("[").append(violationPath).append("]: ");
            buf.append(violation.getMessage()).append('\n');
        }
        return buf.toString();
    }
}

