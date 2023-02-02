package com.hundsun.demo.java.tree;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.tree
 * @className: TreeTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/2 0:16
 * @updateUser: h1123
 * @updateDate: 2023/2/2 0:16
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public class TreeTest {

    private final static TreeUtils<String> stringTreeUtils = new TreeUtils<>();
    private final static TreeUtils<Integer> integerTreeUtils = new TreeUtils<>();

    /**
     * 从上到下, 从左到右为
     * ------A-----
     * ----B---C---
     * --D----E-F--
     * ---G--------
     * 前序访问 - A B D G C E F
     * 中序访问 - D G B A E C F
     * 后序访问 - G D B E F C A
     */
    private final static BiTreeNode<String> SIMPLE_TREE;

    static {
        SIMPLE_TREE = new SimpleBiTreeNode<>("A");
        SIMPLE_TREE.setLeft(
                new SimpleBiTreeNode<>(
                        "B",
                        new SimpleBiTreeNode<>("D", null, new SimpleBiTreeNode<>("G")),
                        null
                )
        );
        SIMPLE_TREE.setRight(
                new SimpleBiTreeNode<>(
                        "C",
                        new SimpleBiTreeNode<>("E"),
                        new SimpleBiTreeNode<>("F")
                )
        );
    }

    private final static BiTreeNode<String> LL_TREE;

    static {

        BiTreeNode<String> a = new SimpleBiTreeNode<>("A");
        BiTreeNode<String> b = new SimpleBiTreeNode<>("B");
        BiTreeNode<String> c = new SimpleBiTreeNode<>("C");
        BiTreeNode<String> d = new SimpleBiTreeNode<>("D");
        BiTreeNode<String> e = new SimpleBiTreeNode<>("E");
        BiTreeNode<String> f = new SimpleBiTreeNode<>("F");

        LL_TREE = a;
        a.setLeft(b);
        a.setRight(c);
        b.setLeft(d);
        b.setRight(e);
        d.setLeft(f);

    }

    private final static BiTreeNode<String> RR_TREE;

    static {

        BiTreeNode<String> a = new SimpleBiTreeNode<>("A");
        BiTreeNode<String> b = new SimpleBiTreeNode<>("B");
        BiTreeNode<String> c = new SimpleBiTreeNode<>("C");
        BiTreeNode<String> d = new SimpleBiTreeNode<>("D");
        BiTreeNode<String> e = new SimpleBiTreeNode<>("E");
        BiTreeNode<String> f = new SimpleBiTreeNode<>("F");

        RR_TREE = a;
        a.setLeft(b);
        a.setRight(c);
        c.setLeft(d);
        c.setRight(e);
        e.setRight(f);

    }

    private final static BiTreeNode<String> LR_TREE;

    static {

        BiTreeNode<String> a = new SimpleBiTreeNode<>("A");
        BiTreeNode<String> b = new SimpleBiTreeNode<>("B");
        BiTreeNode<String> c = new SimpleBiTreeNode<>("C");
        BiTreeNode<String> d = new SimpleBiTreeNode<>("D");
        BiTreeNode<String> e = new SimpleBiTreeNode<>("E");
        BiTreeNode<String> f = new SimpleBiTreeNode<>("F");

        LR_TREE = a;
        a.setLeft(b);
        a.setRight(c);
        b.setLeft(d);
        b.setRight(e);
        e.setLeft(f);

    }

    private final static BiTreeNode<String> RL_TREE;

    static {

        BiTreeNode<String> a = new SimpleBiTreeNode<>("A");
        BiTreeNode<String> b = new SimpleBiTreeNode<>("B");
        BiTreeNode<String> c = new SimpleBiTreeNode<>("C");
        BiTreeNode<String> d = new SimpleBiTreeNode<>("D");
        BiTreeNode<String> e = new SimpleBiTreeNode<>("E");
        BiTreeNode<String> f = new SimpleBiTreeNode<>("F");

        RL_TREE = a;
        a.setLeft(b);
        a.setRight(c);
        c.setLeft(d);
        c.setRight(e);
        d.setRight(f);

    }


    public static void main(String[] args) {
        stringTreeUtils.preOrder(SIMPLE_TREE);
        System.out.println();
        stringTreeUtils.midOrder(SIMPLE_TREE);
        System.out.println();
        stringTreeUtils.postOrder(SIMPLE_TREE);
        System.out.println();

        System.out.println("---------LL旋转----------");
        stringTreeUtils.preOrder(LL_TREE);
        System.out.print(" LL旋转后 -> ");
        BiTreeNode<String> newLLTree = stringTreeUtils.llRotate(LL_TREE);
        stringTreeUtils.preOrder(newLLTree);
        System.out.println();
        stringTreeUtils.preOrder(RR_TREE);
        System.out.print(" RR旋转后 -> ");
        BiTreeNode<String> newRRTree = stringTreeUtils.rrRotate(RR_TREE);
        stringTreeUtils.preOrder(newRRTree);
        System.out.println();
        stringTreeUtils.preOrder(LR_TREE);
        System.out.print(" LR旋转后 -> ");
        BiTreeNode<String> newLRTree = stringTreeUtils.lrRotate(LR_TREE);
        stringTreeUtils.preOrder(newLRTree);
        System.out.println();
        stringTreeUtils.preOrder(RL_TREE);
        System.out.print(" LR旋转后 -> ");
        BiTreeNode<String> newRLTree = stringTreeUtils.rlRotate(RL_TREE);
        stringTreeUtils.preOrder(newRLTree);
        System.out.println();

        BiTreeNode<Integer> root = new AVLTreeNode(3);
        integerTreeUtils.preOrder(root);
        System.out.println();
        root = root.add(new AVLTreeNode(2));
        integerTreeUtils.preOrder(root);
        System.out.println();
        root = root.add(new AVLTreeNode(1));
        integerTreeUtils.preOrder(root);
        System.out.println();
        root = root.add(new AVLTreeNode(4));
        integerTreeUtils.preOrder(root);
        System.out.println();
        root = root.add(new AVLTreeNode(5));
        integerTreeUtils.preOrder(root);
        System.out.println();
        root = root.add(new AVLTreeNode(6));
        integerTreeUtils.preOrder(root);
        System.out.println();
        root = root.add(new AVLTreeNode(7));
        integerTreeUtils.preOrder(root);
        System.out.println();
        root = root.add(new AVLTreeNode(10));
        integerTreeUtils.preOrder(root);
        System.out.println();
        root = root.add(new AVLTreeNode(9));
        integerTreeUtils.preOrder(root);
        System.out.println();
        root = root.add(new AVLTreeNode(8));
        integerTreeUtils.preOrder(root);
        System.out.println();
    }

}
