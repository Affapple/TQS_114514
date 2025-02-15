package tqs;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class TqsStack<T> {
    LinkedList<T> stack;
    
    public TqsStack() {
        stack = new LinkedList<>();
    }

    public int size() {
        return stack.size();
    }

    public T pop() throws NoSuchElementException {
        return stack.pop();
    }

    public T peek() throws NoSuchElementException {
        T element = stack.peek();
        if (element == null)
            throw new NoSuchElementException();

        return element;
    }

    public void push(T item) {
        stack.addFirst(item);
    }


    public boolean isEmpty() {
        return stack.isEmpty();
    }


    public T popTopN(int n) {
        T top = null;
        for (int i = 0; i < n; i++) {
            top = stack.removeFirst();
        }
        return top;
    }
}
