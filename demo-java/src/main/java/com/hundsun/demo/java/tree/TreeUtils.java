package com.hundsun.demo.java.tree;

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
     * @param node
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
     * @param node
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
     * @param node
     */
    public void postOrder(BiTreeNode<T> node) {
        if (node != null) {
            postOrder(node.getLeft());
            postOrder(node.getRight());
            System.out.print(node.getData() + " ");
        }
    }

    /**
     * LL旋转平衡二叉树(右旋操作) - 在左孩子的左子树插入节点导致失衡
     * LL旋转 - 当前父节点变为其左节点的右子树, 左节点右子树变为当前父节点的左子树
     *
     * @param node 源根节点
     * @return 新根节点
     */
    public BiTreeNode<T> llRotate(BiTreeNode<T> node) {

        // 当前父节点
        BiTreeNode<T> parent;
        parent = node;

        if (parent != null) {
            // 获取左子树和左子树的右子树
            BiTreeNode<T> lson;
            if ((lson = parent.getLeft()) != null) {

                // 1. 源节点的左子树的右子树 -> 源节点的左子树
                parent.setLeft(lson.getRight());
                if (lson.getRight() != null) {
                    lson.getRight().setParent(parent);
                }

                // 2. 源节点 -> 源节点的左子树的右子树
                lson.setRight(parent);
                parent.setParent(lson);
            }
            // 3. 源节点的左子树作为新的根节点返回
            return lson;
        }

        return null;
    }

    /**
     * RR旋转平衡二叉树(左旋操作) - 在右孩子的右子树插入节点导致失衡
     * RR旋转 - 当前父节点变为其右子树的左子树, 父节点右子树的左子树会变成原父节点的新的右子树
     *
     * @param node 源根节点
     * @return 新根节点
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

                // 2. 源节点 -> 源节点的右子树的左子树
                rson.setLeft(parent);
            }

            // 3. 源节点的右子树作为新的根节点返回
            return rson;
        }

        return null;
    }

    /**
     * LR旋转平衡二叉树 - 在左孩子的右子树插入节点导致失衡
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
            // 先对左子树进行左旋操作
            BiTreeNode<T> lson = parent.getLeft();
            parent.setLeft(rrRotate(lson));
            // 再对父节点进行右旋操作
            return llRotate(parent);
        }

        return null;
    }

    /**
     * RL旋转平衡二叉树 - 在右孩子的左子树插入节点导致失衡
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
            // 先对左子树进行左旋操作
            BiTreeNode<T> rson = parent.getRight();
            parent.setRight(llRotate(rson));
            // 再对父节点进行左旋
            return rrRotate(parent);
        }

        return null;
    }
}
