package org.hulei.jdk.algorithms;

import java.util.LinkedList;
import java.util.Queue;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}

public class BFSBinaryTree {
    public void BFS(TreeNode root) {
        if (root == null)
            return;

        // 创建一个队列用于广度优先遍历
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            // 出队并访问当前节点
            TreeNode node = queue.poll();
            System.out.print(node.val + " ");

            // 如果当前节点有左子树，则将左子节点加入队列
            if (node.left != null)
                queue.add(node.left);

            // 如果当前节点有右子树，则将右子节点加入队列
            if (node.right != null)
                queue.add(node.right);
        }
    }

    public static void main(String[] args) {
        /*
                 1
                / \
               2   3
              / \ / \
             4  5 6  7
        */

        // 构建二叉树
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);

        BFSBinaryTree bfs = new BFSBinaryTree();
        System.out.println("广度优先遍历结果：");
        bfs.BFS(root);
    }
}
