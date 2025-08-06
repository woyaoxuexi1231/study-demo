# minikube

**Minikube** 是一个轻量级的 **Kubernetes（K8s）本地开发工具**，用于在单机（如个人电脑）上快速搭建一个 **单节点的 Kubernetes 集群**，方便开发者进行本地测试和学习。

### **核心功能**

- **本地运行 Kubernetes**：无需云端服务器，在笔记本或开发机上模拟 K8s 环境。
- **支持多种驱动**：可以使用 Docker、Podman、VirtualBox、KVM 等作为底层虚拟化技术。
- **快速启动和销毁**：适合开发调试，避免影响生产环境。
- **支持主流 Kubernetes 功能**：
  - 部署 Pods、Services、Deployments。
  - 使用 `kubectl` 管理集群。
  - 测试 Helm Charts、Ingress、ConfigMap 等。



将当前用户添加到Docker组，以便在未使用root权限的情况下使用Docker：

```
sudo usermod -aG docker $USER
newgrp docker
```



安装kubectl
运行以下命令，将kubectl二进制文件下载并设置为可执行：

```
curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl
chmod +x kubectl
sudo mv kubectl /usr/local/bin/
```



安装Minikube
运行以下命令，下载并安装Minikube：

```
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube
```



初始化Minikube集群
该步骤将创建一个名为“minikube”的虚拟集群，并启动一个单节点Kubernetes集群。运行以下命令：

```
minikube start

# 我这里是 root 用户，我直接强制启动了
minikube start --force

# 查看状态
minikube status
kubectl get nodes
```



🚨 kubectl get node 可能报错 

"Unhandled Error" err="couldn't get current server API group list: Get \"https://192.168.49.2:8443/api?timeout=32s\": net/http: TLS handshake timeout"

💡可能有代理，关闭代理 unset http_proxy https_proxy





# k3s

**k3s** 是由 **Rancher Labs** 开发的一款轻量级 Kubernetes 发行版，专为 **边缘计算、IoT 设备和资源受限环境** 优化。它的名字中的 "3" 代表 "轻量"（比标准的 K8s 小一半），而 "s" 代表 "SQLite"（默认使用 SQLite 替代 etcd）。



安装



创建 master 节点

```
curl -sfL https://get.k3s.io | sh -

# cn专用的，上面的能用就用上面的
curl -sfL https://rancher-mirror.rancher.cn/k3s/k3s-install.sh | INSTALL_K3S_MIRROR=cn sh -

# 获取master的token，以便在后续的worker节点使用
cat /var/lib/rancher/k3s/server/node-token

```



创建 work 节点

```
curl -sfL https://get.k3s.io | K3S_URL=https://<主节点IP>:6443 K3S_TOKEN=<主节点TOKEN> sh -

curl -sfL https://get.k3s.io | K3S_URL=https://192.168.3.131:6443 K3S_TOKEN=K10bd617e7dda61df4a9e0c71d537c2a452b38dd9d09775ec5ff49a4593ee77f79f::server:9ecfe91cbf40a7e67f6ed0d71ec81b54 sh -
```

