import mysql.connector
from faker import Faker
import random

fake = Faker()

# 连接数据库
conn = mysql.connector.connect(
    host="192.168.3.102",
    user="root",
    password="123456",
    database="test"
)
cursor = conn.cursor()

# 批量生成用户
def generate_users(count):
    for _ in range(count):
        name = fake.name()
        email = fake.email()
        cursor.execute(
            "INSERT INTO big_data_users (name, email) VALUES (%s, %s)",
            (name, email)
        )
    conn.commit()

# 批量生成商品
def generate_products(count):
    for _ in range(count):
        name = fake.word()
        category = fake.word()
        price = round(random.uniform(1, 1000), 2)
        cursor.execute(
            "INSERT INTO big_data_products (name, category, price) VALUES (%s, %s, %s)",
            (name, category, price)
        )
    conn.commit()

# 批量生成订单
def generate_orders(count, max_user_id):
    for _ in range(count):
        user_id = random.randint(1, max_user_id)
        total_amount = round(random.uniform(10, 5000), 2)
        status = random.choice(['pending', 'paid', 'shipped', 'completed'])
        cursor.execute(
            "INSERT INTO big_data_orders (user_id, total_amount, status) VALUES (%s, %s, %s)",
            (user_id, total_amount, status)
        )
    conn.commit()

# 批量生成订单明细
def generate_order_items(count, max_order_id, max_product_id):
    for _ in range(count):
        order_id = random.randint(1, max_order_id)
        product_id = random.randint(1, max_product_id)
        quantity = random.randint(1, 10)
        price = round(random.uniform(1, 1000), 2)
        cursor.execute(
            "INSERT INTO big_data_order_items (order_id, product_id, quantity, price) VALUES (%s, %s, %s, %s)",
            (order_id, product_id, quantity, price)
        )
    conn.commit()

# 批量生成评论
def generate_reviews(count, max_user_id, max_product_id):
    for _ in range(count):
        user_id = random.randint(1, max_user_id)
        product_id = random.randint(1, max_product_id)
        rating = random.randint(1, 5)
        comment = fake.sentence()
        cursor.execute(
            "INSERT INTO big_data_reviews (user_id, product_id, rating, comment) VALUES (%s, %s, %s, %s)",
            (user_id, product_id, rating, comment)
        )
    conn.commit()


# ⚙️ 按需执行
generate_users(10000000)      # 1千万
generate_products(500000)     # 50万
generate_orders(100000000)    # 1亿
generate_order_items(300000000, 100000000, 500000)  # 3亿
generate_reviews(20000000, 10000000, 500000)        # 2000万

cursor.close()
conn.close()
