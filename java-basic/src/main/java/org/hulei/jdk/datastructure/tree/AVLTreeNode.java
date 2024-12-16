package org.hulei.jdk.datastructure.tree;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.tree
 * @className: AVLTreeNode
 * @description: 二叉平衡树
 * @author: h1123
 * @createDate: 2023/2/2 23:40
 */

public class AVLTreeNode extends BiTreeNode<Integer> {

    /**
     * 二叉树操作工具类
     */
    private final static TreeUtils<Integer> treeUtils = new TreeUtils<>();

    public AVLTreeNode(Integer data) {
        super(data);
    }

    /**
     * 添加节点
     *
     * @param node target
     * @return 新的根节点
     */
    @Override
    public BiTreeNode<Integer> add(BiTreeNode<Integer> node) {

        // if null, do nothing
        if (node == null) {
            return this;
        }

        // 缓存一个原始的父节点 - 如果发生失衡, 用来连接到新的节点
        BiTreeNode<Integer> oriParent = this.getParent();
        // 不管加入节点后失衡与否, 用来保存新的右子树或者左子树
        BiTreeNode<Integer> newChild;
        // 插入左边或者右边
        if (node.getData() > getData()) {
            // 插入右孩子
            BiTreeNode<Integer> right = getRight();
            // if right is null, insert
            if (right == null) {
                this.setRight(node);
                node.setParent(this);
                return this;
            } else {
                // 如果不为空, 在当前节点的右子树下继续遍历
                newChild = right.add(node);
                // 当前节点的右子树指针需要指向新的右子树节点
                this.setRight(newChild);
            }
            // 元素添加完成后, 判断是否失衡
            int balance = this.getBF();
            if (balance < -1) {
                if (right.getBF() < 0) {
                    // 说明在右孩子的右子树插入节点导致失衡
                    newChild = treeUtils.rrRotate(this);
                } else {
                    // 说明在右孩子的左子树插入节点导致失衡
                    newChild = treeUtils.rlRotate(this);
                }
                return newChild;
            }
        } else if (node.getData() < getData()) {
            BiTreeNode<Integer> left = getLeft();
            if (left == null) {
                this.setLeft(node);
                node.setParent(this);
                return this;
            } else {
                newChild = left.add(node);
                this.setLeft(newChild);
            }
            int balance = this.getBF();
            if (balance > 1) {
                if (left.getBF() > 0) {
                    newChild = treeUtils.llRotate(this);
                } else {
                    newChild = treeUtils.lrRotate(this);
                }
                return newChild;
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
