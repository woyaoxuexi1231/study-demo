package com.hundsun.demo.java.datastructure.tree;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.datastructure.tree
 * @className: BTreeNode
 * @description: B树 (多路查找树, B-)
 * @author: h1123
 * @createDate: 2023/2/4 15:50
 */

public class BTreeNode<T> {

    /*
    B树(B-树)
    对于一棵 m 阶(m>3)的 B-树, 或为空树, 或为满足一下特征的 m 叉树
    1. 树中每个结点至多有 m 棵子树
    2. 若根结点不是叶子结点, 则至少有两棵子树
    3. 所有的非终端结点中包含下列信息 (n, p0, k1, p1, k2, p2,..., kn, pn)
        其中 ki 为关键字, 且 ki < k(i+1), pj 为指向子树根结点的指针
        且 pj 所指的子树中所有的结点的关键值均小于 k(j+1)
        pn 所指的子树中所有的结点的关键值均大于 kn
        n 为关键字个数
        n+1 为叶子个数
    4. 除根结点之外的所有非终端结点至少有 ⌈m/2⌉ 棵子树, 即每个非根节点至少应有 ⌈m/2⌉-1 个关键字
    5. 所有的叶子节点都出现在同一层次上, 而且不带信息
    *在B树上进行查找需要比较的结点个数最多为B树的深度
    若当 n = 10000, m = 10, B树的深度在 5-6 之间, 而如果是二叉排序树, 则树的深度至少为 14

    B+树
    B+树与B-树的结构大致相同, 差异有以下几点:
    1. B+树中, 每个结点中含有 n 个关键字和和 n 个子树, 即一个关键字对应一个子树
    2. B+树中, 根结点的关键字个数的取值范围为 1<= n <= m, 其他结点的关键字个数取值为 ⌈m/2⌉ <= n <= m
    3. B+树中, 所有叶子结点包含了所有关键字及指向对应记录的指针, 且所有叶子结点按关键字从小到大依次链接
    4. B+树中, 所有非叶子结点仅起到索引作用
    MySQL 的 innodb 存储引擎索引有使用到 B+树

    红黑树(对称二叉B树)
    1. 根结点和所有外部结点的颜色都是黑色
    2. 从根结点到外部结点的所有路径上没有两个连续的红色结点
    3. 从根结点到外部结点的所有路径上都包含相同数目的黑色结点
    HashMap 的存储中有用到红黑树
     */

    /**
     * 关键字个数
     */
    private int keyNum;

    /**
     * 是否为树叶
     */
    private boolean isLeaf;

    /**
     * 关键字数组
     */
    private T[] key;

    /**
     * 子树指针数组
     */
    private BTreeNode<T>[] child;

    /**
     * 双亲结点指针
     */
    private BTreeNode<T> parent;

    public BTreeNode(int m) {
        keyNum = 0;
        isLeaf = true;
        // 每个非根节点至少有 ⌈m/2⌉-1 个关键字
        key = (T[]) (new Object[2 * m - 1]);
        // 每个根节点至少有 ⌈m/2⌉ 棵子树
        child = new BTreeNode[2 * m];
        parent = null;
    }
}
