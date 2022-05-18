package com.bobocode;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A generic comparator that is comparing a random field of the given class. The field is either primitive or
 * {@link Comparable}. It is chosen during comparator instance creation and is used for all comparisons.
 * <p>
 * By default it compares only accessible fields, but this can be configured via a constructor property. If no field is
 * available to compare, the constructor throws {@link IllegalArgumentException}
 *
 * @param <T> the type of the objects that may be compared by this comparator
 */
public class RandomFieldComparator<T> implements Comparator<T> {

    private Class<T> targetType;
    private boolean compareOnlyAccessibleFields;
    Field randomField;


    public RandomFieldComparator(Class<T> targetType) {
        this(targetType, true);
    }

    /**
     * A constructor that accepts a class and a property indicating which fields can be used for comparison. If property
     * value is true, then only public fields or fields with public getters can be used.
     *
     * @param targetType                  a type of objects that may be compared
     * @param compareOnlyAccessibleFields config property indicating if only publicly accessible fields can be used
     */
    public RandomFieldComparator(Class<T> targetType, boolean compareOnlyAccessibleFields) {
        this.targetType = targetType;
        this.compareOnlyAccessibleFields = compareOnlyAccessibleFields;
        Field[] fields = compareOnlyAccessibleFields ? targetType.getFields() : targetType.getDeclaredFields();

        Field[] fieldsToCompare = Arrays.stream(fields)
                .filter(field -> field.getType().isPrimitive() ||
                        Comparable.class.isAssignableFrom(field.getType()))
                .toArray(value -> new Field[value]);
        int randomFieldIndex = ThreadLocalRandom.current().nextInt(fields.length);
        randomField = fieldsToCompare[randomFieldIndex];
        if (!compareOnlyAccessibleFields) {
            randomField.setAccessible(true);
        }
    }

    /**
     * Compares two objects of the class T by the value of the field that was randomly chosen. It allows null values
     * for the fields, and it treats null value grater than a non-null value (nulls last).
     */
    @SneakyThrows
    @Override
    public int compare(T o1, T o2) {
        Comparable f1 = (Comparable) randomField.get(o1);
        Comparable f2 = (Comparable) randomField.get(o2);

        if (f1 == null && f2 == null) {
            return 0;
        } else if (f1 == null) {
            return 1;
        } else if (f2 == null) {
            return -1;
        }

        return f1.compareTo(f2);
    }

    /**
     * Returns a statement "Random field comparator of class '%s' is comparing '%s'" where the first param is the name
     * of the type T, and the second parameter is the comparing field name.
     *
     * @return a predefined statement
     */
    @Override
    public String toString() {
        return String.format("Random field comparator of class '%s' is comparing '%s'",
                targetType.getSimpleName(), randomField.getName());
    }
}