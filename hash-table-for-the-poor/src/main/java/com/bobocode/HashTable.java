
package com.bobocode;

import java.util.Objects;

/**
 * A simple implementation of the Hash Table that allows storing a generic key-value pair. The table itself is based
 * on the array of {@link Node} objects.
 * <p>
 * An initial array capacity is 16.
 * <p>
 * Every time a number of elements is equal to the array size that tables gets resized
 * (it gets replaced with a new array that it twice bigger than before). E.g. resize operation will replace array
 * of size 16 with a new array of size 32. PLEASE NOTE that all elements should be reinserted to the new table to make
 * sure that they are still accessible  from the outside by the same key.
 *
 * @param <K> key type parameter
 * @param <V> value type parameter
 */
public class HashTable<K, V> {
    private int capacity = 16;
    private int size = 0;
    @SuppressWarnings({"unchecked", "rawtype"})
    private Node<K, V>[] storage = new Node[capacity];

    /**
     * Puts a new element to the table by its key. If there is an existing element by such key then it gets replaced
     * with a new one, and the old value is returned from the method. If there is no such key then it gets added and
     * null value is returned.
     *
     * @param key   element key
     * @param value element value
     * @return old value or null
     */
    public V put(K key, V value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        int index = Math.abs(key.hashCode() % capacity);
        Node<K, V> node = storage[index];
        if (node == null) {
            storage[index] = new Node<>(key, value);
            size++;
            if (size >= capacity) {
                capacity *= 2;
                resize();
            }
        } else {
            if (Objects.equals(node.key, key)) {
                V oldValue = node.value;
                node.value = value;

                return oldValue;
            }
            while (node.next != null) {
                node = node.next;
                if (Objects.equals(node.key, key)) {
                    V oldValue = node.value;
                    node.value = value;

                    return oldValue;
                }
            }
            node.next = new Node<>(key, value);
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtype"})
    private void resize() {
        System.out.println("resize: " + capacity);
        Node<K, V>[] oldStorage = storage;
        storage = new Node[capacity];
        for (Node<K, V> node : oldStorage) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    /**
     * Prints a content of the underlying table (array) according to the following format:
     * 0: key1:value1 -> key2:value2
     * 1:
     * 2: key3:value3
     * ...
     */
    public void printTable() {
        for (int i = 0; i < storage.length; i++) {
            Node<K, V> node = storage[i];
            System.out.print(i + ":");
            while (node != null) {
                System.out.print(node.key + ":" + node.value);
                if (node.next != null) {
                    System.out.print(" -> ");
                }
                node = node.next;
            }
            System.out.println();
        }
    }
}
