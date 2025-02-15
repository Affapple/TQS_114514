package tqs;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TqsStackTest {
    TqsStack<Integer> stack;

    @BeforeEach
    void setup() {
        stack = new TqsStack<>();
    }

    @Test
    @DisplayName("Check if stack is empty on creation and has size 0")
    public void StackEmptyOnCreation() {
        assertTrue(stack.size() == 0);
        assertTrue(stack.isEmpty());
    }

    @Test
    @DisplayName("After N pushes on an empty stack, stack has N elements")
    public void StackSizeAfterNPushes() {
        int N = 10;
        for (int i = 0; i < N; i++) {
            stack.push(i);
        }
        assertTrue(stack.size() == N);
    }

    @Test
    @DisplayName("After N pushes then N pops, check if stack is empty")
    @Disabled("Redundant after stackSizeAfterNPushes and LastElementPushedIsOnTop")
    public void StackSizeAfterNPushesAndNPops() {
        int N = 10;
        for (int i = 0; i < N; i++) {
            stack.push(i);
        }

        for (int i = 0; i < N; i++) {
            stack.pop();
        }
        assertTrue(stack.size() == 0);
    }

    @Test
    @DisplayName("After pushing <Element> to stack, push(while reducing in size) and pop(while maintaining size) return <Element>")
    public void LastElementPushedIsOnTop() {
        int N = 10;
        for (int i = 0; i < N; i++) {
            stack.push(i);
        }
        Integer element = 123321;
        stack.push(element);

        Integer initialSize = stack.size();
        assertTrue(stack.peek() == element && stack.size() == initialSize);
        assertTrue(stack.pop() == element && stack.size() == initialSize - 1);
    }


    @Test
    @DisplayName("Peek on empty stack should Return NoSuchElementException")
    public void EmptyStackPeek() {
        assertThrowsExactly(NoSuchElementException.class, stack::peek);
    }

    @DisplayName("Peek on empty stack should Return NoSuchElementException")
    @Test
    public void EmptyStackPop() {
        assertThrowsExactly(NoSuchElementException.class, stack::pop);
    }

    @Test
    public void PopTopZeroItems() {
        for (int i = 0; i < 10; i++) {
            stack.push(i);
        }

        assertTrue(stack.popTopN(0) == null);
    }

    @Test
    public void PopTopAllItems() {
        for (int i = 0; i < 10; i++) {
            stack.push(i);
        }

        assertTrue(stack.popTopN(stack.size()) != null);
    }

    @Test
    public void PopTopMoreThanAllItems() {
        for (int i = 0; i < 10; i++) {
            stack.push(i);
        }

        assertThrowsExactly(NoSuchElementException.class, ()->stack.popTopN(stack.size() + 1));
    }
}
