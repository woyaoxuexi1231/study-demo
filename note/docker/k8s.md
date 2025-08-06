[Kubernetes一小时轻松入门_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1Se411r7vY/?spm_id_from=333.337.search-card.all.click&vd_source=8d7ce9dd45b35258ee11a3c3ce982ea9)

[Kubernetes一小时入门课程 - 视频配套笔记 | GeekHour](https://geekhour.net/2023/12/23/kubernetes/)



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



## 安装



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

curl -sfL https://rancher-mirror.rancher.cn/k3s/k3s-install.sh | INSTALL_K3S_MIRROR=cn K3S_URL=https://192.168.3.131:6443 K3S_TOKEN=K10bd617e7dda61df4a9e0c71d537c2a452b38dd9d09775ec5ff49a4593ee77f79f::server:9ecfe91cbf40a7e67f6ed0d71ec81b54 sh -


# 添加一个 debug 参数
curl -sfL https://get.k3s.io | K3S_URL=https://192.168.3.131:6443 K3S_TOKEN=<TOKEN> sh -s - --debug
```



🚨 这里安装之后，可能会出现所有步骤全部完成吗，但是集群并没有节点

💡**各个机器的名字不能一样，所以要进行修改**

``````
sudo hostnamectl set-hostname "k3s-master"
sudo hostnamectl set-hostname "k3s-agent-1"
sudo hostnamectl set-hostname "k3s-agent-2"

#机器重启后集群状态就正常了
``````

## 使用



nginx.yaml

```yaml
apiVersion: apps/v1  # 使用的 API 版本，apps/v1 是 Deployment 的稳定版本
kind: Deployment     # 资源类型为 Deployment
metadata:
  name: nginx-deployment  # Deployment 的名称
spec:
  selector:          # 选择器，用于确定这个 Deployment 管理哪些 Pod
    matchLabels:
      app: nginx     # 匹配标签为 app=nginx 的 Pod
  replicas: 3        # 希望运行的 Pod 副本数量
  template:          # Pod 模板，用于创建 Pod
    metadata:
      labels:
        app: nginx   # Pod 的标签，必须与 selector 中的 matchLabels 匹配
    spec:
      containers:    # 容器定义
      - name: nginx  # 容器名称
        image: nginx:1.25  # 使用的 Docker 镜像及版本
        ports:
        - containerPort: 80  # 容器暴露的端口
```



开始创建容器

```bash
# 创建
kubectl create -f nginx-deployment.yaml

# 查看是否成功创建
kubectl get all

# 删除
kubectl delete -f nginx-deployment.yaml

# 更新
kubectl create -f nginx-deployment.yaml
```



使用 service 对外提供服务

nginx-service.yaml

```yam
apiVersion: v1  # 指定使用的 Kubernetes API 版本，这里是核心 API 的 v1
kind: Service   # 定义资源类型为 Service（用于网络访问）
metadata:       # 元数据，描述 Service 的信息
  name: nginx-service  # Service 的名称，集群内唯一
spec:           # 定义 Service 的具体规格
  type: NodePort       # Service 类型为 NodePort（在每个节点上开放端口，供外部访问）
  selector:            # 标签选择器，决定将哪些 Pod 纳入 Service 的范围
    app: nginx         # 匹配标签为 `app: nginx` 的 Pod（这些 Pod 通常由 Deployment/ReplicaSet 管理）
  ports:               # 定义端口映射规则
    - port: 80         # Service 暴露的端口（集群内部访问的端口）
      protocol: TCP    # 使用的协议（TCP 或 UDP，默认为 TCP）
      targetPort: 80   # 目标 Pod 的端口（Service 将流量转发到 Pod 的这个端口）
      nodePort: 30080  # 在每个节点上开放的端口（外部通过 `节点IP:30080` 访问）
```





```
# 创建服务
kubectl create service [flags] [options]

# 直接暴露已有服务
kubectl expose (-f FILENAME | TYPE NAME) [--port=port] [--protocol=TCP|UDP|SCTP] [--target-port=number-or-name]
[--name=name] [--external-ip=external-ip-of-service] [--type=type] [options]


# 创建
kubectl expose deployment nginx-deployment
# 查看详情
kubectl describe service nginx-deployment

# 使用配置文件创建
kubectl apply -f nginx-service.yaml
```



# 安装 Portainer

[Portainer](https://www.portainer.io/) 是一个轻量级的容器管理工具，
可以用来管理Docker和Kubernetes，
它提供了一个Web界面来方便我们管理容器，
官方网址: https://www.portainer.io/



```
kubectl apply -n portainer -f https://downloads.portainer.io/ce2-19/portainer.yaml

# 这样的话，容器在 portainer 命名空间下，查看时要通过 portainer 这个名字的容器来看，默认是default
kubectl get all -n portainer

# 如果超时没操作，portainer会要求重启
kubectl delete -f https://downloads.portainer.io/ce2-19/portainer.yaml

# https://192.168.3.131:30779 密码会要求12位，这里统一 4个123
```

