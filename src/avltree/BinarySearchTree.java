package avltree;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class BinarySearchTree<T extends Comparable<T>> {
    private static final int EQUAL = 0; //Used for comparisons
    private static final int LEFT = -1; //Used for branch assignment
    private static final int RIGHT = 1; //Used for branch assignment

    private int size = 0;
    private Node<T> root;
    private NodeBranch<T> recentNode;


    private record NodeBranch<T extends Comparable<T>>
            (Node<T> node, Node<T> parent, int branch) {}

    private boolean notExists(NodeBranch<T> nodeBranch) {
        return nodeBranch == null;
    }

    private boolean exists(Node<T> node) {
        return node != null;
    }

    /**
     * Checks if there are any elements in this tree.
     * @return True if this tree is empty.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in this tree.
     * @return The size of the tree.
     */
    public int size() {
        return size;
    }

    /**
     * Clear the tree and set its size to 0.
     */
    public void clear() {
        root = null;
        recentNode = null;
        size = 0;
    }

    /**
     * Creates a String that contains all elements of this tree
     * inorder / in ascending order.
     * @return A String containing all elements in ascending order.
     */
    public String toString() {
        return Arrays.toString(toArray());
    }

    /**
     * Puts all elements of this tree in the given array and
     * resizes the array as needed.
     * The elements are ordered "inorder" / in ascending order.
     * This method never returns null, instead it will return
     * the given array unchanged if this tree is empty.
     * @param array The array in which the elements are to be
     *               stored in if it is big enough. Else a new
     *               array of the same type will be created.
     * @return An array of all elements in ascending order or
     * the same unchanged array that was passed to this method
     * if the tree is empty.
     */
    @SuppressWarnings("unchecked")
    public T[] toArray(T[] array) {
        if (isEmpty())
            return array;
        if (array.length < size)
            array = (T[]) Array.newInstance(
                    array.getClass().getComponentType(), size
            );

        MinimalStack<Node<T>> stack = new MinimalStack<>();
        Node<T> node = root;

        for (int i = 0; stack.notEmpty() || exists(node);) {
            if (exists(node)) {
                stack.push(node);
                node = node.left();}
            else {
                node = stack.pop();
                array[i++] = node.value();
                node = node.right();}
        }

        return array;
    }

    /**
     * Puts all elements of this tree in a newly assigned array.
     * The elements are ordered "inorder" / in ascending order.
     * @return An array of all elements in ascending order or
     * null if the tree is empty.
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        if (isEmpty()) return null;

        T[] result = (T[]) Array.newInstance(
                root.value().getClass(), size
        );

        return toArray(result);
    }

    /**
     * Returns the element with the highest order.
     * @return Null if tree is empty.
     */
    public T max() {
        if (isEmpty()) return null;

        Node<T> node = root;
        while (exists(node.right()))
            node = node.right();

        return node.value();
    }

    /**
     * Returns the element with the lowest order.
     * @return Null if tree is empty.
     */
    public T min() {
        if (isEmpty()) return null;

        Node<T> node = root;
        while (exists(node.left()))
            node = node.left();

        return node.value();
    }

    /**
     * Inserts an element into the tree.
     * If successful, this method allows constant time
     * operations on the inserted element, until
     * another element is selected.
     * @param value The value to insert.
     * @return True if the value has been inserted.
     * False if the value is already in the tree.
     */
    public boolean insert(T value) {
        if (isEmpty()) { //Insert root node
            root = new Node<>(value);
            ++size;
            return true;
        }

        Node<T> parent = root;
        for (;;) {
            int state = parent.value().compareTo(value);

            //If the value is lower than the current node
            if (state > EQUAL)
                if (parent.leftEmpty()) { //Node can be inserted as the left branch
                    parent.left(new Node<>(value));
                    recentNode = new NodeBranch<>(parent.left(), parent, LEFT);
                    ++size;
                    return true;
                }
                else parent = parent.left(); //Need to iterate to the next left branch

            //If the value is higher than the current node
            else if (state < EQUAL)
                if (parent.rightEmpty()) { //Node can be inserted as the right branch
                    parent.right(new Node<>(value));
                    recentNode = new NodeBranch<>(parent.right(), parent, RIGHT);
                    ++size;
                    return true;
                }
                else parent = parent.right(); //Need to iterate to the next right branch

            else return false; //Value is already in the tree
        }
    }

    private NodeBranch<T> searchNode(T value) {
        if (isEmpty())
            return null;
        if (!notExists(recentNode) && recentNode.node.value() == value)
            return recentNode;

        Node<T> node = root;
        Node<T> parent = null;
        int branch = 0;

        while (node.value() != value) {
            int state = node.value().compareTo(value);

            //If the value is lower than the current node
            if (state > EQUAL)
                if (node.leftEmpty())
                    return null; //Value does not exist
                else { //Iterate to the next left branch
                    parent = node;
                    node = node.left();
                    branch = LEFT;
                }

            //If the value is higher than the current node
            else if (state < EQUAL)
                if (node.rightEmpty())
                    return null; //Value does not exist
                else { //Iterate to the next right branch
                    parent = node;
                    node = node.right();
                    branch = RIGHT;
                }
        }

        //If we got here, that means the value has been found
        return new NodeBranch<>(node, parent, branch);
    }

    /**
     * Search for an element.
     * Any further operations on the element are
     * constant time (if it exists), until another
     * element is selected.
     * @param value Value to be found.
     * @return The value or null if it doesn't exist.
     */
    public T search(T value) {
        NodeBranch<T> result = searchNode(value);
        if (notExists(result))
            return null;
        else {
            recentNode = result;
            return result.node.value();
        }
    }

    /**
     * Check if some element exists in the tree.
     * Any further operations on the element are
     * constant time (if it exists), until another
     * element is selected.
     * @param value The value.
     * @return True if the value was found.
     */
    public boolean has(T value) {
        NodeBranch<T> nodeBranch = searchNode(value);
        if (notExists(nodeBranch))
            return false;
        else {
            recentNode = nodeBranch;
            return true;
        }
    }

    /**
     * Gets the leftmost node of the right branch of this node.
     * If there are no left branches, the right branch of the
     * given node is returned.
     * This method is useful when deleting nodes that have two
     * branches.
     * @param parent The node.
     * @return The resulting leftmost node, the side of the branch
     * it emerges from and its parent node.
     */
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

    /**
     * Deletes the value from the tree.
     * @param value The value you want to delete.
     * @return True if the value was found in the tree.
     * False if the value does not exist.
     */
    public boolean delete(T value) {
        if (isEmpty()) return false;

        NodeBranch<T> deletable = searchNode(value);
        if (notExists(deletable))
            return false;

        Node<T> node = deletable.node;
        Node<T> parent = deletable.parent;
        int branch = deletable.branch;

        //If node is leaf
        if (node.isLeaf())
            //Just delete the reference
            if (branch == LEFT)
                parent.left(null);
            else
                parent.right(null);


        //If node has one branch
        else if (node.oneBranch())
            if (node.hasLeft())
                //Set the parent's right branch to the left
                //branch of the node
                parent.right(node.left());
            else
                //Set the parent's left branch to the right
                //branch of the node
                parent.left(node.right());


        //If node has two branches
        else {
            NodeBranch<T> leftmost = leftmostNode(node);
            //We need the leftmost node of the right branch of
            //the deletable node

            //Get leftmost node's parent
            Node<T> lParent = leftmost.parent;
            //Get all of leftmost node's children
            Node<T> rNode = leftmost.node.right();
            //Get leftmost node's value to insert later
            T lValue = leftmost.node().value();
            //If the branch is RIGHT, there is no leftmost node
            int lBranch = leftmost.branch;

            if (lBranch == LEFT)
                //This is the case most of the time
                //It means that the right branch of the deletable
                //node has left branches
                lParent.left(rNode);
            else
                //This is only the case if the deletable node's
                //right branch has no left branches
                lParent.right(rNode);

            //Set the deletable node's value to the now deleted
            //leftmost node's value
            node.value(lValue);
        }

        --size;
        return true;
    }

    /**
     * Deletes the old value from the tree and inserts a node
     * with the new value instead. If the old value is not
     * found in the tree, this method does nothing.
     * @param oldValue The value to be replaced.
     * @param newValue The value of the potential element to
     *                 be inserted.
     * @return False if the old value does not exist.
     */
    public boolean replace(T oldValue, T newValue) {
        if (!delete(oldValue))
            return false;
        else
            insert(newValue);
        return true;
    }

    /**
     * Deletes the old value from the tree if it exists and
     * inserts a node with the new value. This method will
     * still insert even if the old value is not found.
     * @param oldValue The potential value to be replaced.
     * @param newValue The value of the element to be inserted.
     * @return The return value of the
     * {@link #insert(Comparable)} method. That is, whether
     * the value has been inserted (True) or if it already
     * exists in the tree (False).
     */
    public boolean replaceOrInsert(T oldValue, T newValue) {
        delete(oldValue);
        return insert(newValue);
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

        System.out.println(b);
    }
}