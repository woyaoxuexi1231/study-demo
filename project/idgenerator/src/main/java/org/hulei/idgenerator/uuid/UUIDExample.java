package org.hulei.idgenerator.uuid;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public class UUIDExample {

    public static void main(String[] args) {

        // 生成版本1的UUID
        for (int i = 0; i < 100; i++) {
            UUID timeUUID = UuidCreator.getTimeBased();
            System.out.printf("UUID version %d: %s%n", timeUUID.version(), timeUUID);
        }

        // 基于指定的字节数组生成 UUID v3
        byte[] nameBytes = "nameSpace-name".getBytes();
        UUID nameUUID = UUID.nameUUIDFromBytes(nameBytes);
        System.out.printf("Name-based UUID (UUID v%d), variant %d: %s%n", nameUUID.version(), nameUUID.variant(), nameUUID);
        byte[] nameBytes2 = "nameSpace-name2".getBytes();
        UUID nameUUID2 = UUID.nameUUIDFromBytes(nameBytes2);
        System.out.printf("Name-based UUID (UUID v%d), variant %d: %s%n", nameUUID2.version(), nameUUID2.variant(), nameUUID2);

        // 生成随机的 UUID (UUID v4)
        UUID randomUUID = UUID.randomUUID();
        System.out.printf("Random UUID (UUID v%d), variant %d: %s%n", randomUUID.version(), randomUUID.variant(), randomUUID);


    }
}