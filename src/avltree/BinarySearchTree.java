package avltree;

class BinarySearchTree<T extends Comparable<T>> {
    private static final int EQUAL = 0;
    private static final int LEFT = -1;
    private static final int RIGHT = 1;

    private int size = 0;
    private Node<T> root;

    private record NodeBranch<T extends Comparable<T>> (Node<T> node, Node<T> parent, int branch) {}

    public void insert(T value) {
        if (size == 0) {
            root = new Node<>(value);
            ++size;
            return;
        }

        Node<T> parent = root;
        for (;;) {
            int state = parent.value().compareTo(value);

            if (state > EQUAL)
                if (parent.leftEmpty()) {
                    parent.left(new Node<>(value));
                    ++size;
                    return;
                }
                else parent = parent.left();

            else if (state < EQUAL)
                if (parent.rightEmpty()) {
                    parent.right(new Node<>(value));
                    ++size;
                    return;
                }
                else parent = parent.right();

            else return;
        }
    }

    private NodeBranch<T> searchNode(T value) {
        if (size == 0) return null;

        Node<T> node = root;
        Node<T> parent = null;
        int branch = 0;

        while (node.value() != value) {
            int state = node.value().compareTo(value);

            if (state > EQUAL)
                if (node.leftEmpty())
                    return null;
                else {
                    parent = node;
                    node = node.left();
                    branch = LEFT;
                }

            else if (state < EQUAL)
                if (node.rightEmpty())
                    return null;
                else {
                    parent = node;
                    node = node.right();
                    branch = RIGHT;
                }
        }

        return new NodeBranch<>(node, parent, branch);
    }

    public T search(T value) {
        NodeBranch<T> result = searchNode(value);
        if (result != null)
            return result.node.value();
        else return null;
    }

    public boolean has(T value) {
        return search(value) != null;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    private NodeBranch<T> leftmostNode(Node<T> parent) {
        Node<T> node = parent.right();
        if (node.leftEmpty())
            return new NodeBranch<>(node, parent, RIGHT);

        while (node.hasLeft()) {
            parent = node;
            node = node.left();
        }

        return new NodeBranch<>(node, parent, LEFT);
    }

    public boolean delete(T value) {
        if (size == 0) return false;

        NodeBranch<T> deletable = searchNode(value);
        if (deletable == null)
            return false;

        Node<T> node = deletable.node;
        Node<T> parent = deletable.parent;
        int branch = deletable.branch;

        if (node.isLeaf()) {
            if (branch == LEFT)
                parent.left(null);
            else
                parent.right(null);
        }

        else if (node.oneBranch()) {
            if (node.hasLeft())
                parent.right(node.left());
            else
                parent.left(node.right());
        }

        else {
            NodeBranch<T> leftmost = leftmostNode(node);

            Node<T> lParent = leftmost.parent;
            Node<T> rNode = leftmost.node.right();
            int lBranch = leftmost.branch;
            T lValue = leftmost.node().value();

            if (lBranch == LEFT)
                lParent.left(rNode);
            else
                lParent.right(rNode);

            node.value(lValue);
        }

        --size;
        return true;
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer> b = new BinarySearchTree<>();
        b.insert(10);
        b.insert(8);
        b.insert(12);
        b.insert(16);
        b.insert(20);
        b.insert(18);
        b.insert(9);
        b.insert(4);
        b.insert(14);
        b.insert(19);
    }
}