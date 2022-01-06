package avltree;

class MinimalStack<T> {
    private Frame<T> topFrame;

    private record Frame<T>
            (T value, Frame<T> next) {}

    boolean notEmpty() {
        return topFrame != null;
    }

    void push(T value) {
        topFrame = new Frame<>(value, topFrame);
    }

    T pop() {
        T popValue = topFrame.value;
        topFrame = topFrame.next;
        return popValue;
    }
}