package com.hundsun.demo.java.datastructure.tree;


/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.tree
 * @className: SimpleBiTreeNode
 * @description:
 * @author: h1123
 * @createDate: 2023/2/3 0:26
 * @updateUser: h1123
 * @updateDate: 2023/2/3 0:26
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public class SimpleBiTreeNode<T> extends BiTreeNode<T> {


    public SimpleBiTreeNode(T data, BiTreeNode<T> left, BiTreeNode<T> right) {
        super(data, left, right);
    }

    public SimpleBiTreeNode(T data) {
        super(data);
    }

    @Override
    public BiTreeNode<T> add(BiTreeNode<T> node) {
        return null;
    }
}
