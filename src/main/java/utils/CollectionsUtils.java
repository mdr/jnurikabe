package utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CollectionsUtils {

    public static <T> Set<T> makeSet(T... things) {
        return new HashSet<T>(Arrays.asList(things));
    }

    public static <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        Set<T> intersection = new HashSet<T>(set1);
        intersection.retainAll(set2);
        return intersection;
    }

    public static <T> Set<T> intersectAll(Collection<Set<T>> sets) {
        if (sets.size() == 0)
            return Collections.emptySet();
        Set<T> intersection = null;
        for (Set<T> set : sets) {
            if (intersection == null)
                intersection = new HashSet<T>(set);
            else
                intersection.retainAll(set);
        }
        return intersection;
    }
    
    public static <T> T getSingleElementOf(Iterable<T> iterable) {
        Iterator<T> iterator = iterable.iterator();
        T first = iterator.next();
        if (iterator.hasNext())
            throw new IllegalArgumentException("Expected only a single element in " + iterable);
        return first;
    }

    public static <T> T getFirstElementOf(Iterable<T> iterable) {
        return iterable.iterator().next();
    }

    
}
