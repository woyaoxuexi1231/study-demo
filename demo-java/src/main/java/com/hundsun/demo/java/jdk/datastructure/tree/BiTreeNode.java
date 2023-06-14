package com.hundsun.demo.java.jdk.datastructure.tree;

import lombok.Data;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.tree
 * @className: TreeNode
 * @description:
 * @author: h1123
 * @createDate: 2023/2/1 23:01
 */
@Data
public abstract class BiTreeNode<T> {

    /**
     * 节点数据
     */
    private T data;

    /**
     * 左子树
     */
    private BiTreeNode<T> left;

    /**
     * 右子树
     */
    private BiTreeNode<T> right;

    /**
     * 父节点
     */
    private BiTreeNode<T> parent;

    /**
     * 深度
     */
    private int depth;

    /**
     * constructor with data, left, right
     *
     * @param data  节点数据
     * @param left  左子树
     * @param right 右子树
     */
    public BiTreeNode(T data, BiTreeNode<T> left, BiTreeNode<T> right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    /**
     * default constructor
     *
     * @param data 节点数据
     */
    public BiTreeNode(T data) {
        this.data = data;
    }

    /**
     * 获得该节点的平衡因子 (balance factory)
     *
     * @return balance factory
     */
    public int getBF() {
        if (left == null && right == null) {
            return 0;
        } else if (left == null) {
            return -right.getDepth();
        } else if (right == null) {
            return left.getDepth();
        } else {
            return left.getDepth() - right.getDepth();
        }
    }

    /**
     * 获得该节点的深度 (定义第一层深度为 1)
     *
     * @return 高度
     */
    public int getDepth() {
        if (left == null && right == null) {
            return 1;
        } else if (left == null) {
            return right.getDepth() + 1;
        } else if (right == null) {
            return left.getDepth() + 1;
        } else {
            return Math.max(left.getDepth(), right.getDepth()) + 1;
        }
    }

    /**
     * 新增节点
     *
     * @param node 新增的节点
     * @return 新的根节点
     */
    public abstract BiTreeNode<T> add(BiTreeNode<T> node);

    @Override
    public String toString() {
        return "BiTreeNode{" +
                "data=" + data +
                '}';
    }
}
