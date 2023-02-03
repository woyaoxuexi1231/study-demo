package com.hundsun.demo.java.datastructure.tree;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.tree
 * @className: TreeUtils
 * @description:
 * @author: h1123
 * @createDate: 2023/2/1 23:07
 * @updateUser: h1123
 * @updateDate: 2023/2/1 23:07
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public class TreeUtils<T> {

    /**
     * 前序遍历二叉树
     *
     * @param node 源节点
     */
    public void preOrder(BiTreeNode<T> node) {
        if (node != null) {
            System.out.print(node.getData() + " ");
            preOrder(node.getLeft());
            preOrder(node.getRight());
        }
    }

    /**
     * 中序遍历二叉树
     *
     * @param node 源节点
     */
    public void midOrder(BiTreeNode<T> node) {
        if (node != null) {
            midOrder(node.getLeft());
            System.out.print(node.getData() + " ");
            midOrder(node.getRight());
        }
    }

    /**
     * 前序遍历二叉树
     *
     * @param node 源节点
     */
    public void postOrder(BiTreeNode<T> node) {
        if (node != null) {
            postOrder(node.getLeft());
            postOrder(node.getRight());
            System.out.print(node.getData() + " ");
        }
    }

    /*
    二叉树的四种旋转方式, 最终只有两个方法是核心的, LL 和 RR
    在设计插入节点的方法时, 一定要注意 Node 节点一共有三个指针 -- 左子树、右子树、父节点
    两个节点之间的对应关系一定得是 <左子树-父节点> 或者是 <右子树-父节点>, 也就是说两个节点必须互相指向对方, 否则条件会不足
     */

    /**
     * LL旋转平衡二叉树(右旋操作) - 在左孩子的左子树插入节点导致失衡
     * <p>
     * <p>
     * 1. 源节点的左子树会指向 -> 源节点左子树的右子树 + 源节点左子树的右子树的父节点指针会指向 -> 源节点
     * <p>
     * 2. 新的源节点(源节点的原来的左孩子)的父节点会指向 -> 源节点的父节点指针指向的对象, *但是这里无法对源节点的父节点的指针进行修改
     * <p>
     * 3. 新的源节点的右子树指针会指向 -> 源节点 + 源节点的父节点指针会指向 -> 新的源节点
     *
     * @param node 源节点
     * @return 新的源节点
     */
    public BiTreeNode<T> llRotate(BiTreeNode<T> node) {

        // 当前父节点
        BiTreeNode<T> parent;
        parent = node;

        if (parent != null) {
            // 获取左子树
            BiTreeNode<T> lson;
            if ((lson = parent.getLeft()) != null) {

                // 1. 源节点的左子树的右子树 -> 源节点的左子树
                parent.setLeft(lson.getRight());
                if (lson.getRight() != null) {
                    lson.getRight().setParent(parent);
                }

                // 2. 新的根节点的父节点指向源节点的父节点
                lson.setParent(parent.getParent());

                // 3. 源节点 -> 源节点的左子树的右子树
                lson.setRight(parent);
                parent.setParent(lson);

            }
            // 4. 源节点的左子树作为新的根节点返回
            return lson;
        }

        return null;
    }

    /**
     * RR旋转平衡二叉树(左旋操作) - 在右孩子的右子树插入节点导致失衡
     * <p>
     * <p>
     * 1. 源节点的右子树指针会指向 -> 源节点的右子树的左子树 +　源节点的右子树的左子树的父节点指针会指向 -> 源节点
     * <p>
     * 2. 新的源节点(源节点的原来的右孩子)的父节点指针会指向 -> 源节点的父节点指针指向的对象, *同样这里无法对源节点的父节点的指针进行修改
     * <p>
     * 3.新的源节点的左子树指针会指向 -> 源节点 + 源节点的父节点指针会指向 -> 新的源节点
     *
     * @param node 源节点
     * @return 新的源节点
     */
    public BiTreeNode<T> rrRotate(BiTreeNode<T> node) {

        // 当前父节点
        BiTreeNode<T> parent;
        parent = node;

        if (parent != null) {
            // 获取左子树和左子树的右子树
            BiTreeNode<T> rson;
            if ((rson = node.getRight()) != null) {

                // 1. 源节点的右子树的左子树 -> 源节点的右子树
                parent.setRight(rson.getLeft());
                if (rson.getLeft() != null) {
                    rson.getLeft().setParent(parent);
                }

                // 2. 新的根节点的父节点指向源节点的父节点
                rson.setParent(parent.getParent());

                // 3. 源节点 -> 源节点的右子树的左子树
                rson.setLeft(parent);
                parent.setParent(rson);

            }

            // 4. 源节点的右子树作为新的根节点返回
            return rson;
        }

        return null;
    }

    /**
     * LR旋转平衡二叉树 - 在左孩子的右子树插入节点导致失衡
     * <p>
     * LR旋转 - 先对左孩子进行左旋操作, 再对根节点进行优选操作
     *
     * @param node 源根节点
     * @return 新根节点
     */
    public BiTreeNode<T> lrRotate(BiTreeNode<T> node) {

        // 当前父节点
        BiTreeNode<T> parent;
        parent = node;

        if (parent != null) {
            // 先对左子树进行左旋操作, 此时新的左子树节点已经指向当前节点, 需要把当前节点的左子树指针指向新的左子树节点
            parent.setLeft(rrRotate(parent.getLeft()));
            // 再对父节点进行右旋操作
            return llRotate(parent);
        }

        return null;
    }

    /**
     * RL旋转平衡二叉树 - 在右孩子的左子树插入节点导致失衡
     * <p>
     * LR旋转 - 先对右孩子进行右旋, 再对根节点进行左旋
     *
     * @param node 源根节点
     * @return 新根节点
     */
    public BiTreeNode<T> rlRotate(BiTreeNode<T> node) {

        // 当前父节点
        BiTreeNode<T> parent;
        parent = node;

        if (parent != null) {
            // 先对右子树进行右旋, 此时新的右子树节点已经指向当前节点, 需要把当前节点的右子树指针指向新的右子树节点
            parent.setRight(llRotate(parent.getRight()));
            // 再对父节点进行左旋
            return rrRotate(parent);
        }

        return null;
    }
}
