package com.hundsun.demo.springboot.idgenerator.uuid;

import java.util.Date;
import java.util.UUID;

public class UUIDExample {

    public static void main(String[] args) {

        // 生成随机的 UUID (UUID v4)
        UUID randomUUID = UUID.randomUUID();
        System.out.println("Random UUID (UUID v4): " + randomUUID);

        // 基于指定的字节数组生成 UUID v3
        byte[] nameBytes = "example name".getBytes();
        UUID nameUUID = UUID.nameUUIDFromBytes(nameBytes);
        System.out.println("Name-based UUID (UUID v3): " + nameUUID);

        // 获取 UUID 的版本号
        int version = randomUUID.version();
        System.out.println("UUID version: " + version);

        // 获取 UUID 的变体号
        int variant = randomUUID.variant();
        System.out.println("UUID variant: " + variant);

        UUID uuid = UUID.randomUUID();
        System.out.println(String.format("%s-%s", uuid, new Date().getTime()));
    }
}

/*
对于不同版本的UUID，各部分的含义和目的有所差异，主要基于UUID生成时所使用的算法。下面是各个版本UUID具体各部分的解释：

### 版本 1：基于时间戳的UUID
- **字段 1：时间戳的低位部分** (8 位)
- **字段 2：时间戳的中位部分** (4 位)
- **字段 3：时间戳的高位部分，并包括UUID版本号** (4 位，其中最高位两个数字表示UUID版本)
- **字段 4：时钟序列，并包括变体的信息** (2位时钟序列高位 + 2位时钟序列低位，其中高位的几个位包括变体信息，通常为1)
- **字段 5：节点ID** (通常是MAC地址，12 位)

### 版本 2：不提供详细的公开定义

### 版本 3：基于名称和MD5散列的UUID
- **字段 1：散列生成的部分** (8 位)
- **字段 2：散列生成的部分** (4 位)
- **字段 3：散列生成的部分，包含UUID版本号** (4 位，其中最高位两个数字表示UUID版本)
- **字段 4：散列生成的部分** (4 位)
- **字段 5：散列生成的部分** (12 位)

### 版本 4：随机生成的UUID
- **字段 1：随机生成的数字** (8 位)
- **字段 2：随机生成的数字** (4 位)
- **字段 3：随机生成的数字但包含UUID版本号** (4 位，其中最高位两个数字表示UUID版本)
- **字段 4：随机生成的数字，包含变体信息** (4 位，其中最高位的几位包含变体信息)
- **字段 5：随机生成的数字** (12 位)

### 版本 5：基于名称和SHA-1散列的UUID
- **字段 1：散列生成的部分** (8 位)
- **字段 2：散列生成的部分** (4 位)
- **字段 3：散列生成的部分，包含UUID版本号** (4 位，其中最高位两个数字表示UUID版本)
- **字段 4：散列生成的部分** (4 位)
- **字段 5：散列生成的部分** (12 位)

对于版本3和版本5的UUID，名称和命名空间是通过散列算法生成UUID的，所以它们在结构上与版本4相似，但生成算法和用途不同。版本3使用MD5散列算法，而版本5使用SHA-1散列算法。这些散列算法帮助确保从相同的输入名称和命名空间生成的UUID具有确定性和唯一性，即使在不同的执行间也是如此。
 */
