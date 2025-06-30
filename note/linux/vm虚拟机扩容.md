# VM虚拟机扩容

[VMware虚拟机扩容--保姆级教学_vmware虚拟机扩展内存-CSDN博客](https://blog.csdn.net/qq_51601665/article/details/129539534)





# XFS 盘空间的转移



在 CentOS 7 中，`/home` 分区通常挂载在 LVM 逻辑卷上（如 `/dev/mapper/centos-home`），而根目录 `/` 挂载在 `/dev/mapper/centos-root`。要将 `/home` 的空间缩减 40G 并分配给根目录 `/`，需要 **调整 LVM 逻辑卷**。以下是详细步骤：

---

## **步骤 1：检查当前磁盘和 LVM 布局**
### **1.1 查看磁盘分区和挂载点**
```bash
df -h
```
输出示例：
```
Filesystem               Size  Used Avail Use% Mounted on
/dev/mapper/centos-root   50G   10G   40G  20% /
/dev/mapper/centos-home   47G  3.2G   44G   7% /home
```
### **1.2 查看 LVM 逻辑卷信息**
```bash
lvdisplay
```
重点关注：
- `centos-home`（`/home` 对应的逻辑卷）
- `centos-root`（`/` 对应的逻辑卷）

### **1.3 查看卷组（VG）剩余空间**
```bash
vgdisplay
```
检查 `Free PE / Size`，确保有足够空间扩展 `/`（如果没有，需要缩减 `/home`）。



## **步骤 3：备份 `/home` 数据**
XFS 无法直接缩小，必须先备份数据，删除原逻辑卷，再重建一个更小的 `/home` 逻辑卷，最后将剩余空间分配给 `/`。

```bash
sudo mkdir /tmp/home_backup
sudo cp -a /home/* /tmp/home_backup/  # 保留所有权限和属性
```

---

## **步骤 4：卸载 `/home` 并删除原逻辑卷**
### **4.1 卸载 `/home`**
```bash
sudo umount /home
```
如果提示 `target is busy`，用 `lsof` 查找并终止占用进程：
```bash
sudo lsof /home
sudo kill -9 <PID>  # 终止相关进程
sudo umount /home   # 再次尝试卸载
```

### **4.2 删除 `/home` 逻辑卷**
```bash
sudo lvremove /dev/mapper/centos-home
```
确认操作（输入 `y`）。

---

## **步骤 5：缩小 `/home` 逻辑卷并扩展 `/` 逻辑卷**
### **5.1 创建新的、更小的 `/home` 逻辑卷（如 10G）**
```bash
sudo lvcreate -L 10G -n home centos  # 新建 10G 的 /home 逻辑卷
sudo mkfs.xfs /dev/mapper/centos-home  # 格式化为 XFS
```

### **5.2 将剩余空间分配给 `/` 逻辑卷**
查看卷组（VG）剩余空间：
```bash
sudo vgdisplay
```
输出中的 `Free PE / Size` 应该显示约 40G 可用空间（原 `/home` 47G - 新 `/home` 10G ≈ 37G，具体数值可能有差异）。

扩展 `/` 逻辑卷：
```bash
sudo lvextend -l +100%FREE /dev/mapper/centos-root  # 将所有剩余空间给 /
```

### **5.3 扩展 `/` 的文件系统（XFS）**
```bash
sudo xfs_growfs /  # 让 / 使用新增的空间
```
（如果是 ext4，用 `resize2fs /dev/mapper/centos-root`）

---

## **步骤 6：恢复 `/home` 数据并重新挂载**
### **6.1 挂载新的 `/home`**
```bash
sudo mount /dev/mapper/centos-home /home
```

### **6.2 恢复备份数据**
```bash
sudo cp -a /tmp/home_backup/* /home/
sudo chown -R username:username /home/username  # 恢复用户权限（替换 username）
```

### **6.3 更新 `/etc/fstab`（确保开机自动挂载）**
```bash
sudo blkid /dev/mapper/centos-home  # 获取新 UUID
sudo vi /etc/fstab
```
添加或修改 `/home` 的挂载行（示例）：
```
UUID=新UUID /home xfs defaults 0 0
```

---

## **步骤 7：验证空间分配**
```bash
df -h
```
输出应类似：
```
Filesystem               Size  Used Avail Use% Mounted on
/dev/mapper/centos-root   90G   15G   75G  17% /          # / 已扩展
/dev/mapper/centos-home   10G  3.2G  6.8G  32% /home      # /home 缩小
```

---

## **关键注意事项**
1. **备份数据**：操作前必须备份 `/home`，逻辑卷删除后数据无法恢复！
2. **XFS 限制**：  
   - XFS 只能扩大（`xfs_growfs`），不能直接缩小，必须重建逻辑卷。
3. **确保 `/home` 未挂载**：  
   所有调整操作必须在卸载状态下进行。
4. **如果 `/` 不是 LVM**：  
   如果 `/` 不是逻辑卷（如直接挂载 `/dev/sda2`），则无法扩展，需另建新分区并挂载到其他目录（如 `/data`）。

---

## **总结**
| 操作              | 命令                                            |
| ----------------- | ----------------------------------------------- |
| 备份 `/home`      | `cp -a /home/* /tmp/home_backup/`               |
| 卸载 `/home`      | `umount /home`                                  |
| 删除原逻辑卷      | `lvremove /dev/mapper/centos-home`              |
| 新建小的 `/home`  | `lvcreate -L 10G -n home centos`                |
| 扩展 `/`          | `lvextend -l +100%FREE /dev/mapper/centos-root` |
| 扩展 `/` 文件系统 | `xfs_growfs /`                                  |
| 恢复 `/home` 数据 | `cp -a /tmp/home_backup/* /home/`               |

完成后再用 `df -h` 检查空间分配是否符合预期。
