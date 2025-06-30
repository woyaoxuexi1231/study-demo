## 安装，配置

**Gitea**：一个轻量级的Git服务，支持自建和托管，界面友好，适合小型团队使用。

**Gogs**：类似于Gitea，也是一个轻量级的Git服务，但界面更为简洁，适合个人和小团队。

**GitStack**：一个集成了Git和Bash的Windows服务，方便在Windows环境中使用Git。

**Gogs**：一个轻量级的Git服务，支持自建和托管，界面友好，适合小型团队使用。

**GitLab CE**：GitLab的社区版，完全开源，功能强大，适合大型团队使用。



[使用源代码安装 | Gitea Documentation](https://docs.gitea.com/zh-cn/installation/install-from-source)

过程异常艰辛

要从源代码进行构建，系统必须预先安装以下程序：

- `go` 1.22 或更高版本，请参阅 [这里](https://go.dev/dl/)
- `node` 18 或更高版本，并且安装 `npm`, 请参阅 [这里](https://nodejs.org/zh-cn/download/)
- `make`, 请参阅 [这里](https://docs.gitea.com/zh-cn/development/hacking-on-gitea)

参考了几篇文章：

[Linux7.9安装nodejs v18以上需要升级gcc glibc具体步骤_您的系统中【gnu libc】的版本过低,无法兼容nodejs v18以上的版本,已为您隐藏不兼-CSDN博客](https://blog.csdn.net/qq_44546355/article/details/135148883)

[解决：centos7 中node: /lib64/libm.so.6: version `GLIBC_2.27‘ not found (required by node)-CSDN博客](https://blog.csdn.net/nilm61/article/details/134266633)



[建议] 启动程序改个名字 改成 gitea 



[日志配置]

```ini
[log]
MODE = console, file
LEVEL = Info
ROOT_PATH = /usr/local/gitea/log

[log.console]
MODE = console
LEVEL = Info
STACKTRACE_LEVEL = None

[log.file]
MODE = file
LEVEL = Info
STACKTRACE_LEVEL = None
FILE_NAME = gitea.log
LOG_ROTATE = true
MAX_SIZE = 100
DAILY_ROTATE = true
MAX_DAYS = 7
```



## 问题

### [配置git信息]

```shell
git config --global user.name hulei
git config --global user.email 154347188@qq.com
git config --global http.proxy "socks5://192.168.3.2:21231"
git config --global https.proxy "socks5://192.168.3.2:21231"


git config --global --unset http.proxy
git config --global --unset https.proxy
```

### [http拉取代码]

```cmd
C:\dataz\Project>git clone http://192.168.3.101:3000/hulei/study-gitea
Cloning into 'study-gitea'...
fatal: unable to access 'http://192.168.3.101:3000/hulei/study-gitea/': The requested URL returned error: 502
```

这个应该是网络问题，配置好git的 Personal access tokens 就行

```shell
# 拉取代码是报错的
[root@node-101 ~]# git clone https://github.com/woyaoxuexi1231/study-demo.git
Cloning into 'study-demo'...
Username for 'https://github.com': 154347188@qq.com
Password for 'https://154347188@qq.com@github.com': 
remote: Support for password authentication was removed on August 13, 2021.
remote: Please see https://docs.github.com/get-started/getting-started-with-git/about-remote-repositories#cloning-with-https-urls for information on currently recommended modes of authentication.
fatal: Authentication failed for 'https://github.com/woyaoxuexi1231/study-demo.git/'
```

https://docs.github.com/en/get-started/getting-started-with-git/about-remote-repositories#cloning-with-https-urls

![image-20241127213309592](C:\dataz\Project\study-demo\note\images\image-20241127213309592.png)

基于密码登录的已经被移除了。

参考文章：[Git 如何使用个人访问令牌克隆、拉取和推送存储库|极客教程](https://geek-docs.com/git/git-questions/238_git_how_to_use_personal_access_token_to_clone_pull_and_push_a_repo.html)

javaee-qiukai-git: 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

```shel
[root@node-101 devjar]# git clone https://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@github.com/woyaoxuexi1231/study-demo.git

Cloning into 'study-demo'...
remote: Enumerating objects: 23162, done.
remote: Counting objects: 100% (4380/4380), done.
remote: Compressing objects: 100% (1793/1793), done.
error: RPC failed; curl 18 transfer closed with outstanding read data remaining
error: 3 bytes of body are still expected
fetch-pack: unexpected disconnect while reading sideband packet
fatal: early EOF
fatal: fetch-pack: invalid index-pack output
```

**没解决**



### [ssh拉取代码]

```cmd
C:\dataz\Project>git clone git@192.168.3.101:hulei/study-gitea.git
Cloning into 'study-gitea'...
git@192.168.3.101's password:
fatal: 'hulei/study-gitea.git' does not appear to be a git repository
fatal: Could not read from remote repository
```

配置ssh

```shell
# linux上生成 ssh-key
[root@node-101 devjar]# ssh-keygen -t rsa -C "154347188@qq.com"
Generating public/private rsa key pair.
Enter file in which to save the key (/root/.ssh/id_rsa): 
Created directory '/root/.ssh'.
Enter passphrase (empty for no passphrase): 
Enter same passphrase again: 
Your identification has been saved in /root/.ssh/id_rsa.
Your public key has been saved in /root/.ssh/id_rsa.pub.
The key fingerprint is:
SHA256:3K7ChGQbjMB+4m0y7S1TncniVjD4PXzm5I4kyONHOyU 154347188@qq.com
The key's randomart image is:
+---[RSA 2048]----+
|.                |
|..               |
|.. o.            |
| o.o=o . .       |
|. =o.+B S .      |
| +.++E.@ =       |
|  =+=+*.B .      |
|  .+.B= .+       |
|   .= .oo.       |
+----[SHA256]-----+

// 进入ssh目录
$ cd ~/.ssh
// 查看ssh 公钥  进行复制
$ cat id_rsa.pub


### 这里完事后在 github 去配置ssh公钥 


## 配置完之后，使用这个验证是否成功
ssh -T git@github.com 
```





