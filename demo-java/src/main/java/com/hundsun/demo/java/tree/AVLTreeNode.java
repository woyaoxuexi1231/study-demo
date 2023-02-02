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
                // 这里判断走向, 以确定如果失衡, 具体采用哪种旋转方法
                if (node.getData() > right.getData()) {
                    // 插入右孩子的右子树
                    if (right.getRight() == null) {
                        right.setRight(node);
                    } else {
                        right.getRight().add(node);
                    }
                    // 添加完成后进行计算平衡因子, 如果失衡了
                    int balance = this.getBF();
                    if (balance < -1) {
                        // 这种情况是 RR, 直接左旋
                        return treeUtils.rrRotate(this.getMinBalNode(this));
                    }
                } else {
                    // 这里插入右孩子的左子树
                    if (right.getLeft() == null) {
                        right.setLeft(node);
                    } else {
                        right.getLeft().add(node);
                    }
                    // 添加完成后进行计算平衡因子, 如果失衡了
                    int balance = this.getBF();
                    if (balance < -1) {
                        // 这种情况是 RR, 直接左旋
                        return treeUtils.rlRotate(this);
                    }
                }
            }
        } else if (node.getData() < getData()) {
            // 插入左孩子
            BiTreeNode<Integer> left = getLeft();
            if (left == null) {
                this.setLeft(node);
                return this;
            } else {
                // 这里判断走向, 以确定如果失衡, 具体采用哪种旋转方法
                if (node.getData() > left.getData()) {
                    // 插入左孩子的右子树
                    if (left.getRight() == null) {
                        left.setRight(node);
                    } else {
                        left.getRight().add(node);
                    }
                    // 添加完成后进行计算平衡因子, 如果失衡了
                    int balance = this.getBF();
                    if (balance > 1) {
                        // 这种情况是 LR
                        return treeUtils.lrRotate(this);
                    }
                } else {
                    // 这里插入左孩子的左子树
                    if (left.getLeft() == null) {
                        left.setLeft(node);
                    } else {
                        left.getLeft().add(node);
                    }
                    // 添加完成后进行计算平衡因子, 如果失衡了
                    int balance = this.getBF();
                    if (balance > 1) {
                        // 这种情况是 LL
                        return treeUtils.llRotate(this);
                    }
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

    public BiTreeNode<Integer> getMinBalNode(BiTreeNode<Integer> node) {
        if ((node.getLeft() == null || (node.getLeft().getBF() <= 1 && node.getLeft().getBF() >= -1))
                && ((node.getRight() == null || (node.getRight().getBF() <= 1 && node.getRight().getBF() >= -1)))) {
            return node;
        } else if (node.getLeft() != null && (node.getLeft().getBF() > 1 || node.getLeft().getBF() < -1)) {
            return node.getMinBalNode(node.getLeft());
        } else {
            return node.getMinBalNode(node.getRight());
        }
    }
}
