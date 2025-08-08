https://blog.csdn.net/wh_computers/article/details/99404893



lua下载地址

https://github.com/lua/lua

https://www.lua.org/versions.html

luarocks下载地址

https://github.com/luarocks/luarocks?tab=readme-ov-file



安装 luasql-pgsql插件

https://www.cnblogs.com/xuanmanstein/p/10699664.html



# 编译lua

最好不要下载最新的，驾驭不了。

```bash
cd lua-5.1.5
make linux test
make install
```



# 编译 luarocks

```bash
./configure --with-lua=/usr/local --with-lua-include=/usr/local/include
make
make install
make bootstrap
```



# luarocks安装插件

## 安装 `socket.http` 模块

luarocks install luasocket

## 安装pgsql插件

luarocks install luasql-postgres

安装时要求必须配置pgsql的环境：

```
Error: Could not find header file for PGSQL
  No file libpq-fe.h in /usr/local/include
  No file libpq-fe.h in /usr/include
  No file libpq-fe.h in /include
You may have to install PGSQL in your system and/or pass PGSQL_DIR or PGSQL_INCDIR to the luarocks command.
Example: luarocks install luasql-postgres PGSQL_DIR=/usr/local
```

我这里是肯定不会安装的，这里有解决思路：

https://www.cnblogs.com/xuanmanstein/p/10699664.html

https://blog.csdn.net/van38686061/article/details/102607444

安装pgsql的开发包

```bash
yum install postgresql-devel
```

再进行插件安装

```bash
luarocks install luasql-postgres
```

此时插件可以正常引入，但是连接会报错Failed to connect to database: LuaSQL: error connecting to database. PostgreSQL: SCRAM authentication requires libpq version 10 or above

需要更新libpq

**查看libpq版本**

```bash
pg_config --version
```

**对于 Ubuntu/Debian 系统**

```bash
sudo apt-get update
sudo apt-get install libpq-dev
```

**对于 CentOS/RHEL 系统**

```bash
# 安装 PostgreSQL Global Development Group (PGDG) 提供的 PostgreSQL 仓库配置包
yum install https://download.postgresql.org/pub/repos/yum/10/redhat/rhel-7-x86\_64/pgdg-centos10-10-2.noarch.rpm -y  
# 查看 postgresql 有哪些是可安装的
yum list | grep postgresql
# 
```

