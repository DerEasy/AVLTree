package avltree;

class Node<T extends Comparable<T>> {
    private Node<T> left, right;
    private T value;

    Node(T val) {
        value = val;
    }

    Node<T> left() {
        return left;
    }

    Node<T> right() {
        return right;
    }

    T value() {
        return value;
    }

    void left(Node<T> node) {
        left = node;
    }

    void right(Node<T> node) {
        right = node;
    }

    void value(T val) {
        value = val;
    }

    boolean rightEmpty() {
        return right == null;
    }

    boolean leftEmpty() {
        return left == null;
    }

    boolean hasLeft() {
        return left != null;
    }

    boolean isLeaf() {
        return left == null && right == null;
    }

    boolean oneBranch() {
        return (left == null) != (right == null);
    }
}