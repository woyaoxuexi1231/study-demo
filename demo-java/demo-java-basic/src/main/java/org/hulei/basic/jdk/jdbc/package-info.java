/**
 * <p>包含一系列 JDBC 操作的示例类。</p>
 * <ul>
 *     <li>{@code org.hulei.jdk.jdbc.ConnectFactory} - 连接建立工具。</li>
 *     <li>{@code org.hulei.jdk.jdbc.PrintUtil} - 结果输出工具。</li>
 * </ul>
 * <p>类顺序如下：</p>
 * <ol>
 *     <li>{@code org.hulei.jdk.jdbc.StatementBasicTest} - statement 最基础的操作，以及需要注意的点和问题。</li>
 *     <li>{@code org.hulei.jdk.jdbc.ScrollResultSetTest} - 可滚动结果集的详细 demo，一般不会使用这个东西。</li>
 *     <li>{@code org.hulei.jdk.jdbc.PreparedStatementTest} - SQL 预编译的 statement, MyBatis 的 # 占位符和 JdbcTemplate 的 ? 确实都是基于 PreparedStatement。</li>
 *     <li>{@code org.hulei.jdk.jdbc.SavePointTest} - 在使用某些驱动的时候，检查点可以提供更加细粒度的回滚操作，这个用得比较少。</li>
 *     <li>{@code org.hulei.jdk.jdbc.SelectModTest} - MySQL 的三种查询模式：普通查询、流式查询、游标查询。</li>
 * </ol>
 */

package org.hulei.basic.jdk.jdbc;