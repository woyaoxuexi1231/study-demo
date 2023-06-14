package com.hundsun.demo.java.jdk.datastructure.tree;


/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.tree
 * @className: SimpleBiTreeNode
 * @description: 简单的二叉树
 * @author: h1123
 * @createDate: 2023/2/3 0:26
 */

public class SimpleBiTreeNode<T> extends BiTreeNode<T> {

    /**
     * constructor with data, left, right
     *
     * @param data  节点数据
     * @param left  左子树
     * @param right 右子树
     */
    public SimpleBiTreeNode(T data, BiTreeNode<T> left, BiTreeNode<T> right) {
        super(data, left, right);
    }

    /**
     * default constructor
     *
     * @param data 节点数据
     */
    public SimpleBiTreeNode(T data) {
        super(data);
    }

    @Override
    public BiTreeNode<T> add(BiTreeNode<T> node) {
        return null;
    }
}
