package proj3;

public class RedBlackTree<AnyType extends Comparable<? super AnyType>>
{
     /**
     * Constructor
     */
    public RedBlackTree()
    {
        nullNode = new RedBlackNode<AnyType>(null);
        nullNode.left = nullNode.right = nullNode;
        header = new RedBlackNode<AnyType>(null); // root
        header.left = header.right = nullNode;
    }

    /**
     * Internal comparison utilizing compareTo
     */
    private int compare(AnyType item, RedBlackNode<AnyType> t)
    {
        if(t == header)
            return 1;
        else
            return item.compareTo(t.element);
    }

    /**
     * Insert into the tree.
     * @param item the item to insert.
     */
    public void insert(AnyType item)
    {
        current = parent = grand = header;
        nullNode.element = item;

        while(compare(item, current) != 0)
        {
            great = grand;
            grand = parent;
            parent = current;
            if(compare(item, current) < 0)
            {
                current = current.left;
            }
            else
            {
                current = current.right;
            }

            // Check for two red children; fix if so
            if(current.left.color == RED && current.right.color == RED)
            {
                handleReorient(item);
            }
        }

        // Insertion fails if already present
        if(current != nullNode)
            return;
        current = new RedBlackNode<AnyType>(item, nullNode, nullNode);

        // Attach to parent
        if(compare(item, parent) < 0)
            parent.left = current;
        else
            parent.right = current;
        handleReorient(item);
    }

    public AnyType findMin()
    {
        RedBlackNode<AnyType> itr = header.right;

        while(itr.left != nullNode)
            itr = itr.left;

        return itr.element;
    }

    /**
     * Find the largest item in the tree.
     */
    public AnyType findMax()
    {

        RedBlackNode<AnyType> itr = header.right;

        while(itr.right != nullNode)
            itr = itr.right;

        return itr.element;
    }

    /**
     * Find an item in the tree.
     * @param x the item to search for.
     * @return true if x is found; otherwise false.
     */
    public Partial retreiveIfItContains(AnyType x) // modified contains method
    {
        nullNode.element = x;
        current = header.right;

        while(true)
        {
            if( x.compareTo(current.element ) < 0)
                current = current.left;
            else if(x.compareTo(current.element) > 0)
                current = current.right;
            else if(current != nullNode)
                return (Partial) current.element;
            else
                return null; // match found
        }
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree()
    {
        if(isEmpty())
            System.out.println("Empty tree");
        else
            printTree(header.right);
    }

    /**
     * Internal method to print a subtree in sorted order.
     * @param t the node that roots the subtree.
     */
    private void printTree(RedBlackNode<AnyType> t)
    {
        if(t != nullNode)
        {
            printTree(t.left);
            System.out.println(t.element);
            printTree(t.right);
        }
    }

    public String printRoot()
    {
        if(isEmpty())
        {

            return("No root");
        }
        else
            return(printRoot(header));
    }

    private String printRoot(RedBlackNode<AnyType> t)
    {
        return(t.right.element.toString());
    }

    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty()
    {
        return header.right == nullNode;
    }

    /**
     * Internal routine that is called during an insertion
     * if a node has two red children. Performs flip and rotations.
     * @param item the item being inserted.
     */
    private void handleReorient(AnyType item)
    {
        // Do the color flip
        current.color = RED;
        current.left.color = BLACK;
        current.right.color = BLACK;

        if(parent.color == RED)   // rotate
        {
            grand.color = RED;
            if((compare(item, grand) < 0) !=
                    (compare(item, parent) < 0))
                parent = rotate(item, grand);  // Start dbl rotate
            current = rotate(item, great);
            current.color = BLACK;
        }
        header.right.color = BLACK; // root =  black
    }

    /**
     * Internal routine that performs a single or double rotation.
     * Because the result is attached to the parent, there are four cases.
     * Called by handleReorient.
     * @param item the item in handleReorient.
     * @param parent the parent of the root of the rotated subtree.
     * @return the root of the rotated subtree.
     */
    private RedBlackNode<AnyType> rotate(AnyType item, RedBlackNode<AnyType> parent)
    {
        if(compare(item, parent) < 0)
            return parent.left = compare(item, parent.left) < 0 ?
                    rotateWithLeftChild(parent.left)  :  // LL
                    rotateWithRightChild(parent.left) ;  // LR
        else
            return parent.right = compare(item, parent.right) < 0 ?
                    rotateWithLeftChild(parent.right) :  // RL
                    rotateWithRightChild(parent.right);  // RR
    }

    /**
     * Rotate binary tree node with left child.
     */
    private RedBlackNode<AnyType> rotateWithLeftChild(RedBlackNode<AnyType> k2 )
    {
        RedBlackNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        return k1;
    }

    /**
     * Rotate binary tree node with right child.
     */
    private RedBlackNode<AnyType> rotateWithRightChild(RedBlackNode<AnyType> k1)
    {
        RedBlackNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        return k2;
    }

    private static class RedBlackNode<AnyType>
    {
        // Constructors
        RedBlackNode(AnyType theElement)
        {
            this(theElement, null, null);
        }

        RedBlackNode(AnyType theElement, RedBlackNode<AnyType> lt, RedBlackNode<AnyType> rt)
        {
            element  = theElement;
            left     = lt;
            right    = rt;
            color    = RedBlackTree.BLACK;
        }

        AnyType               element;    // The data in the node
        RedBlackNode<AnyType> left;       // Left child
        RedBlackNode<AnyType> right;      // Right child
        int                   color;      // Color

        public RedBlackNode<AnyType> getLeftChild()
        {
            return left;
        }
        public RedBlackNode<AnyType> getRightChild()
        {
            return right;
        }
    }

    private RedBlackNode<AnyType> header;
    private RedBlackNode<AnyType> nullNode;

    private static final int BLACK = 1;    // BLACK must be 1
    private static final int RED   = 0;

    // Nodes and parent references
    private RedBlackNode<AnyType> current;
    private RedBlackNode<AnyType> parent;
    private RedBlackNode<AnyType> grand;
    private RedBlackNode<AnyType> great;
}
