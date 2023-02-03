package com.hundsun.demo.java.tree;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.tree
 * @className: AVLTreeNode
 * @description:
 * @author: h1123
 * @createDate: 2023/2/2 23:40
 * @updateUser: h1123
 * @updateDate: 2023/2/2 23:40
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public class AVLTreeNode extends BiTreeNode<Integer> {

    private final static TreeUtils<Integer> treeUtils = new TreeUtils<>();

    public AVLTreeNode(Integer data) {
        super(data);
    }

    /**
     * 添加节点
     *
     * @param node target
     * @return 合并后的树
     */
    @Override
    public BiTreeNode<Integer> add(BiTreeNode<Integer> node) {

        // if null, do nothing
        if (node == null) {
            return this;
        }

        // 插入左边或者右边
        if (node.getData() > getData()) {
            // 插入右孩子
            BiTreeNode<Integer> right = getRight();
            if (right == null) {
                this.setRight(node);
                return this;
            } else {
                right.add(node);
            }
            int balance = this.getBF();
            if (balance < -1) {
                // 失衡了
                if (right.getBF() < 0) {
                    // 说明在右孩子的右子树插入节点导致失衡
                    return treeUtils.rrRotate(this);
                } else {
                    // 说明在右孩子的左子树插入节点导致失衡
                    return treeUtils.rlRotate(this);
                }
            }
        } else if (node.getData() < getData()) {
            // 插入左孩子
            BiTreeNode<Integer> left = getLeft();
            if (left == null) {
                this.setLeft(node);
                return this;
            } else {
                left.add(node);
            }
            int balance = this.getBF();
            if (balance > 1) {
                // 失衡了
                if (left.getBF() > 0) {
                    // 说明在左孩子的左子树插入节点导致失衡
                    return treeUtils.llRotate(this);
                } else {
                    // 说明在左孩子的右子树插入节点导致失衡
                    return treeUtils.lrRotate(this);
                }
            }
        } else {
            // if same, do nothing
        }
        return this;
    }

    public Integer getData() {
        return super.getData();
    }

    public BiTreeNode<Integer> getRight() {
        return super.getRight();
    }

    public BiTreeNode<Integer> getLeft() {
        return super.getLeft();
    }

}
