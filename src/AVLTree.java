import java.util.Comparator;
import java.util.Iterator;


public class AVLTree<K, V> extends BinarySearchTree<K, V> implements Dictionary<K, V> {

    public AVLTree(Comparator<K> c) {
        super(c);
    }

    public AVLTree() {
        super();
    }

    /**
     * A node in the AVL Tree
     * @param <K> The key that will be stored
     * @param <V> The value that will be stored
     */
    protected static class AVLNode<K, V> extends BTNode<Entry<K, V>> {
        protected int height;

        AVLNode() {
        }

        AVLNode(Entry<K, V> element, BTPosition<Entry<K, V>> parent, BTPosition<Entry<K, V>> left, BTPosition<Entry<K, V>> right) {
            super(element, parent, left, right);
            height = 0;
            if (left != null)
                height = Math.max(height, 1 + ((AVLNode<K, V>) left).getHeight());
            if (right != null)
                height = Math.max(height, 1 + ((AVLNode<K, V>) right).getHeight());
        }

        public void setHeight(int h) {
            height = h;
        }

        public int getHeight() {
            return height;
        }
    }

    /**
     * Creates a new node that can be inserted into the tree
     * @param element The element that the node contains
     * @param parent A reference to the parent
     * @param left A reference to the left child
     * @param right A reference to the right child
     * @return
     */
    protected BTPosition<Entry<K, V>> createNode(Entry<K, V> element, BTPosition<Entry<K, V>> parent, BTPosition<Entry<K, V>> left, BTPosition<Entry<K, V>> right) {
        return new AVLNode<K, V>(element, parent, left, right);
    }

    /**
     * Return the height for a position in the AVL tree
     * @param p The position
     * @return returns the height of the node
     */
    protected int height(Position<Entry<K, V>> p) {
        return ((AVLNode<K, V>) p).getHeight();
    }

    /**
     * Set the height for a node in the tree
     * @param p The position of the node in the tree
     */
    protected void setHeight(Position<Entry<K, V>> p) {
        ((AVLNode<K, V>) p).setHeight(1 + Math.max(height(left(p)), height(right(p))));
    }

    /**
     * Return if the position is balanced, if the difference of the heights of the children are less than or equal to 1
     * @param p The position (root) of two children in the tree
     * @return true if the tree is balanced at this point
     */
    protected boolean isBalanced(Position<Entry<K, V>> p) {

        int bf = height(left(p)) - height(right(p));
        return ((-1 <= bf) && (bf <= 1));
    }

    /**
     * Return the taller of the two children for a position
     * @param p The parent of two children in the tree
     * @return The taller of the two children
     */
    protected Position<Entry<K, V>> tallerChild(Position<Entry<K, V>> p) {
    	Position<Entry<K, V>> tallChild = null;
    	if(isRoot(p)){
        	tallChild = p;
        }
    	
    	if(height(left(p))  > height(right(p))){
        	tallChild = right(p);
        }
        else if(height(right(p))  < height(left(p))){
        	tallChild = left(p);
        }
    	
        if(p == left(parent(p))){
        	tallChild = left(p);
        }
        else{
        	tallChild = right(p);
        }
        
    	return tallChild;
    }

    /**
     * Rebalance the tree from a starting z position
     * @param zPos A z position in the tree
     */
    protected void rebalance(Position<Entry<K, V>> zPos) {

        if (isInternal(zPos))
            setHeight(zPos);
        while (!isRoot(zPos)) {
            zPos = parent(zPos);
            setHeight(zPos);
            if (!isBalanced(zPos)) {
                Position<Entry<K, V>> xPos = tallerChild(tallerChild(zPos));
                zPos = restructure(xPos);
                setHeight(left(zPos));
                setHeight(right(zPos));
                setHeight(zPos);
            }
        }
    }

    /**
     * Restructure the tree given an x position in the tree. The Z and Y position must be calculated
     * @param x a X position in the tree.
     * @return The root of the restructured tree
     */
    protected Position<Entry<K, V>> restructure(Position<Entry<K, V>> x) {
        BTPosition<Entry<K, V>> a, b, c, t1, t2, t3, t4;

        Position<Entry<K, V>> y = parent(x);
        Position<Entry<K, V>> z = parent(y);
        boolean xLeft = (x == left(y));
        boolean yLeft = (y == left(z));
        BTPosition<Entry<K, V>> xx = (BTPosition<Entry<K, V>>) x,
                yy = (BTPosition<Entry<K, V>>) y, zz = (BTPosition<Entry<K, V>>) z;

        //determine the type of rotation and retructure the tree as required
        if (xLeft && yLeft) {
        	
        	yy.setParent(zz.getParent());
        	zz.setParent(yy);
        	yy.setRight(zz);
        	zz.setLeft(yy.getRight());
        	
        	t4 = zz.getRight();
            t3 = zz.getLeft();
            t2 = xx.getRight();
            t1 = xx.getLeft();
        	
        }

        if (isRoot(z)) {
        	root = yy;
        	zz.setParent(yy);
        	yy.setRight(zz);
        	zz.setLeft(yy.getRight());
        } else {
            rebalance(z);
        }

        // Set the references for a,b,c and t1,t2,t3,t4
        a = xx;
        b = yy;
        c = zz;
        
        t4 = zz.getRight();
        t3 = zz.getLeft();
        t2 = xx.getRight();
        t1 = xx.getLeft();
        

        ((BSTEntry<K, V>) a.element()).pos = a;
        ((BSTEntry<K, V>) b.element()).pos = b;
        ((BSTEntry<K, V>) c.element()).pos = c;

        //return the new root
        return b;
    }

    /**
     * Insert an item into the tree and rebalance
     * @param k The key
     * @param v The value
     * @return an Entry in the tree
     * @throws InvalidKeyException if the key is not valid
     */
    public Entry<K, V> insert(K k, V v) throws InvalidKeyException {
        Entry<K, V> toReturn = super.insert(k, v);
        rebalance(actionPos);
        return toReturn;
    }

    /**
     * Remove an item from the AVL tree and rebalance
     * @param ent the entry to remove
     * @return the removed entry
     * @throws InvalidEntryException if the key is not valid
     */
    public Entry<K, V> remove(Entry<K, V> ent) throws InvalidEntryException {
        Entry<K, V> toReturn = super.remove(ent);
        if (toReturn != null)
            rebalance(actionPos);
        return toReturn;
    }

    /**
     * Return a string representation of the tree
     * @return A string representation of the tree
     */
    public String toString() {

        String str = "";
        Iterator<Entry<K, V>> elementIter = iterator();
        while (elementIter.hasNext()) {
            Entry<K, V> elem = elementIter.next();
            if (elem != null) {
                str += elem.getKey().toString() + "\t" + elem.getValue().toString() + "\n";
            }
        }
        str += "\n";
        return str;
    }
}
