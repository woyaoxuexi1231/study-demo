好的，以下是三种常见的日志文件共享方式的详细步骤：NFS、SMB 和 SSH/SCP。

### 1. NFS 共享

NFS（Network File System）是一种分布式文件系统协议，允许用户通过网络访问共享文件。以下是配置 NFS 共享和挂载的步骤。

#### 在日志文件服务器上配置 NFS 共享

1. **安装 NFS 服务器**:
   ```bash
   sudo apt-get install nfs-kernel-server   # Debian/Ubuntu
   sudo yum install nfs-utils               # Red Hat/CentOS
   ```

2. **配置共享目录**:
   编辑 `/etc/exports` 文件，添加共享目录：
   ```bash
   sudo nano /etc/exports
   ```
   添加如下行：
   ```
   /path/to/your/logfiles *(rw,sync,no_subtree_check)
   ```
   将 `/path/to/your/logfiles` 替换为实际的日志文件目录。

3. **启动 NFS 服务**:
   ```bash
   sudo systemctl start nfs-kernel-server  # Debian/Ubuntu
   sudo systemctl start nfs-server         # Red Hat/CentOS
   ```

4. **设置开机自启**（可选）:
   ```bash
   sudo systemctl enable nfs-kernel-server  # Debian/Ubuntu
   sudo systemctl enable nfs-server         # Red Hat/CentOS
   ```

#### 在 Filebeat 服务器上挂载 NFS 共享

1. **安装 NFS 客户端**:
   ```bash
   sudo apt-get install nfs-common          # Debian/Ubuntu
   sudo yum install nfs-utils               # Red Hat/CentOS
   ```

2. **创建挂载点**:
   创建一个目录用于挂载 NFS 共享：
   ```bash
   sudo mkdir /mnt/logfiles
   ```

3. **挂载 NFS 共享**:
   ```bash
   sudo mount -t nfs <logfile-server-ip>:/path/to/your/logfiles /mnt/logfiles
   
   sudo mount -t nfs 192.168.80.140:/var/lib/docker/volumes/26e54132cd61f944d04566e38c9758a44121fe97778404e65b78b573980809aa/_data /home/es/es/filebeat-8.11.4-linux-x86_64/loadlogs
   ```
   将 `<logfile-server-ip>` 替换为日志文件服务器的 IP 地址，将 `/path/to/your/logfiles` 替换为实际的日志文件目录。

4. **设置开机自动挂载**（可选）:
   编辑 `/etc/fstab` 文件，添加如下行：
   ```bash
   sudo nano /etc/fstab
   ```
   添加如下行：
   ```
   <logfile-server-ip>:/path/to/your/logfiles /mnt/logfiles nfs defaults 0 0
   192.168.80.140:/var/lib/docker/volumes/26e54132cd61f944d04566e38c9758a44121fe97778404e65b78b573980809aa/_data /home/es/es/filebeat-8.11.4-linux-x86_64/loadlogs nfs defaults 0 0
   
   ```

### 2. SMB 共享

SMB（Server Message Block）是 Windows 系统中常用的文件共享协议，Linux 系统可以通过 Samba 来访问 SMB 共享。

#### 在日志文件服务器上配置 Samba 共享

1. **安装 Samba**:
   ```bash
   sudo apt-get install samba              # Debian/Ubuntu
   sudo yum install samba                  # Red Hat/CentOS
   ```

2. **配置共享目录**:
   编辑 `/etc/samba/smb.conf` 文件，添加共享目录：
   ```bash
   sudo nano /etc/samba/smb.conf
   ```
   在文件末尾添加如下配置：
   ```ini
   [logfiles]
   path = /path/to/your/logfiles
   writable = yes
   guest ok = yes
   read only = no
   ```
   将 `/path/to/your/logfiles` 替换为实际的日志文件目录。

3. **重启 Samba 服务**:
   ```bash
   sudo systemctl restart smbd
   ```

#### 在 Filebeat 服务器上挂载 SMB 共享

1. **安装 cifs-utils**:
   ```bash
   sudo apt-get install cifs-utils         # Debian/Ubuntu
   sudo yum install cifs-utils             # Red Hat/CentOS
   ```

2. **创建挂载点**:
   创建一个目录用于挂载 SMB 共享：
   ```bash
   sudo mkdir /mnt/logfiles
   ```

3. **挂载 SMB 共享**:
   ```bash
   sudo mount -t cifs -o guest //<logfile-server-ip>/logfiles /mnt/logfiles
   ```
   将 `<logfile-server-ip>` 替换为日志文件服务器的 IP 地址。

4. **设置开机自动挂载**（可选）:
   编辑 `/etc/fstab` 文件，添加如下行：
   ```bash
   sudo nano /etc/fstab
   ```
   添加如下行：
   ```
   //<logfile-server-ip>/logfiles /mnt/logfiles cifs defaults,guest 0 0
   ```

### 3. SSH/SCP 传输

SSH/SCP 是通过加密通道传输文件的方法，适合小规模的日志文件传输。

#### 在日志文件服务器上配置 SSH 访问

1. **确保 SSH 服务已安装并运行**:
   通常 SSH 服务默认已安装并运行，如果没有：
   ```bash
   sudo apt-get install openssh-server    # Debian/Ubuntu
   sudo yum install openssh-server        # Red Hat/CentOS
   ```

2. **创建 SSH 密钥对**:
   在 Filebeat 服务器上生成 SSH 密钥对：
   ```bash
   ssh-keygen -t rsa -b 4096
   ```
   按照提示生成密钥对（通常在 `~/.ssh/` 目录下生成 `id_rsa` 和 `id_rsa.pub` 文件）。

3. **将公钥复制到日志文件服务器**:
   ```bash
   ssh-copy-id user@<logfile-server-ip>
   ```
   将 `user` 替换为日志文件服务器的用户名，将 `<logfile-server-ip>` 替换为日志文件服务器的 IP 地址。

#### 在 Filebeat 服务器上配置定期传输

1. **创建一个脚本定期传输日志文件**:
   创建一个脚本文件，例如 `/usr/local/bin/transfer_logs.sh`：
   ```bash
   sudo nano /usr/local/bin/transfer_logs.sh
   ```
   添加如下内容：
   ```bash
   #!/bin/bash
   scp user@<logfile-server-ip>:/path/to/your/logfiles/*.log /mnt/logfiles/
   ```
   将 `user` 替换为日志文件服务器的用户名，将 `<logfile-server-ip>` 替换为日志文件服务器的 IP 地址，将
   `/path/to/your/logfiles` 替换为实际的日志文件目录。

2. **设置脚本可执行权限**:
   ```bash
   sudo chmod +x /usr/local/bin/transfer_logs.sh
   ```

3. **设置定时任务**:
   使用 `crontab` 设置定时任务，例如每小时执行一次：
   ```bash
   crontab -e
   ```
   添加如下行：
   ```
   0 * * * * /usr/local/bin/transfer_logs.sh
   ```

### 总结

通过以上步骤，您可以选择适合您环境的日志文件共享方式，并配置 Filebeat 读取远程服务器上的日志文件并将其发送到 Elasticsearch
中。