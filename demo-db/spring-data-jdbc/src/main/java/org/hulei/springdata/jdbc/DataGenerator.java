package org.hulei.springdata.jdbc;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RequestMapping("/data-generator")
@RestController
@Component
public class DataGenerator {

    private static final int BATCH_SIZE = 5000; // 每批插入多少条
    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    @Autowired
    DataSource ds;

    public void generateUsers(DataSource ds, long total) throws SQLException {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO big_data_users (name, email) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                long inserted = 0;
                while (inserted < total) {
                    for (int i = 0; i < BATCH_SIZE && inserted < total; i++) {
                        ps.setString(1, faker.name().fullName());
                        ps.setString(2, faker.internet().emailAddress());
                        ps.addBatch();
                        inserted++;
                    }
                    ps.executeBatch();
                    conn.commit();
                    System.out.printf("Users: 已插入 %d 条\n", inserted);
                }
            }
        }
    }

    public void generateProducts(DataSource ds, long total) throws SQLException {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO big_data_products (name, category, price) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                long inserted = 0;
                while (inserted < total) {
                    for (int i = 0; i < BATCH_SIZE && inserted < total; i++) {
                        ps.setString(1, faker.commerce().productName());
                        ps.setString(2, faker.commerce().department());
                        ps.setDouble(3, Double.parseDouble(faker.commerce().price()));
                        ps.addBatch();
                        inserted++;
                    }
                    ps.executeBatch();
                    conn.commit();
                    System.out.printf("Products: 已插入 %d 条\n", inserted);
                }
            }
        }
    }

    public void generateOrders(DataSource ds, long total, long maxUserId) throws SQLException {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO big_data_orders (user_id, total_amount, status) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                long inserted = 0;
                String[] statusArr = {"pending", "paid", "shipped", "completed"};
                while (inserted < total) {
                    for (int i = 0; i < BATCH_SIZE && inserted < total; i++) {
                        ps.setLong(1, Math.abs(random.nextLong()) % maxUserId + 1);
                        ps.setDouble(2, random.nextDouble());
                        ps.setString(3, statusArr[random.nextInt(statusArr.length)]);
                        ps.addBatch();
                        inserted++;
                    }
                    ps.executeBatch();
                    conn.commit();
                    System.out.printf("Orders: 已插入 %d 条\n", inserted);
                }
            }
        }
    }

    public void generateOrderItems(DataSource ds, long total, long maxOrderId, long maxProductId) throws SQLException {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO big_data_order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                long inserted = 0;
                while (inserted < total) {
                    for (int i = 0; i < BATCH_SIZE && inserted < total; i++) {
                        ps.setLong(1, Math.abs(random.nextLong()) % maxOrderId + 1);
                        ps.setLong(2, Math.abs(random.nextLong()) % maxProductId + 1);
                        ps.setInt(3, random.nextInt(10) + 1);
                        ps.setDouble(4, random.nextDouble());
                        ps.addBatch();
                        inserted++;
                    }
                    ps.executeBatch();
                    conn.commit();
                    System.out.printf("OrderItems: 已插入 %d 条\n", inserted);
                }
            }
        }
    }

    public void generateReviews(DataSource ds, long total, long maxUserId, long maxProductId) throws SQLException {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO big_data_reviews (user_id, product_id, rating, comment) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                long inserted = 0;
                while (inserted < total) {
                    for (int i = 0; i < BATCH_SIZE && inserted < total; i++) {
                        ps.setLong(1, Math.abs(random.nextLong()) % maxUserId + 1);
                        ps.setLong(2, Math.abs(random.nextLong()) % maxProductId + 1);
                        ps.setInt(3, random.nextInt(5) + 1);
                        ps.setString(4, faker.lorem().sentence());
                        ps.addBatch();
                        inserted++;
                    }
                    ps.executeBatch();
                    conn.commit();
                    System.out.printf("Reviews: 已插入 %d 条\n", inserted);
                }
            }
        }
    }

    @PostMapping("/exec")
    public void exec() throws SQLException {

        ThreadPoolExecutor tpe = new ThreadPoolExecutor(
                5, 5, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());


        // 按需注释或放开，按顺序生成
        tpe.execute(() -> {
            try {
                generateUsers(ds, 10_000_000);                // 用户表
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        tpe.execute(() -> {
            try {
                generateProducts(ds, 500_000);                // 商品表
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        tpe.execute(() -> {
            try {
                generateOrders(ds, 100_000_000, 10_000_000);  // 订单表
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        tpe.execute(() -> {
            try {
                generateOrderItems(ds, 300_000_000, 100_000_000, 500_000); // 订单明细
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        tpe.execute(() -> {
            try {
                generateReviews(ds, 20_000_000, 10_000_000, 500_000);      // 评论表
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
