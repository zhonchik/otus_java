package ru.otus;

import java.util.*;
import java.util.function.UnaryOperator;


class DIYarrayList<E> implements List<E> {

    private Object[] innerArray;

    private static int defaultCapacity = 10;

    private int listSize = 0;

    public DIYarrayList(int initialCapacity) {
        this.innerArray = new Object[initialCapacity];
    }

    public DIYarrayList() {
        innerArray = new Object[defaultCapacity];
    }

    private class DIYListItr implements ListIterator<E> {

        private int iterIndex;

        DIYListItr(int index) {
            iterIndex = index - 1;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            iterIndex++;
            checkIndex(iterIndex);
            return (E) innerArray[iterIndex];
        }

        @Override
        public void set(E e) {
            checkIndex(iterIndex);
            innerArray[iterIndex] = e;
        }

        @Override
        public boolean hasNext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasPrevious() {
            throw new UnsupportedOperationException();
        }

        @Override
        public E previous() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int nextIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }
    }

    private void ensureFits(int index) {
        int previousLength = innerArray.length;
        if (index >= previousLength) {
            innerArray = Arrays.copyOf(innerArray, previousLength * 2, Object[].class);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= listSize) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public boolean add(E e) {
        ensureFits(listSize);
        innerArray[listSize] = e;
        listSize++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);
        return (E) innerArray[index];
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        innerArray[index] = element;
        return element;
    }

    @Override
    public int size() {
        return listSize;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sort(Comparator<? super E> c) {
        Arrays.sort((E[]) innerArray, 0, listSize, c);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new DIYListItr(0);
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<E> spliterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(innerArray, listSize, Object[].class));
    }
}
