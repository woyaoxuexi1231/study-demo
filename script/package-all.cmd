@echo off
REM 设置错误级别为0
set ERRORLEVEL=0

REM 定义你的项目路径
set PROJECT_PATH=C:\project\study-demo
set MAVEN_PATH=C:\Program Files\JetBrains\IntelliJ IDEA 2024.2.5\plugins\maven\lib\maven3\bin

REM 进入项目目录
cd /d %PROJECT_PATH%\dependencies-parent

REM 执行Maven打包命令
echo maven package...
%MAVEN_PATH\mvn clean install -DskipTests

REM 检查Maven命令是否成功执行
if %ERRORLEVEL% neq 0 (
    echo Maven package failed %ERRORLEVEL%
) else (
    echo Maven package successful
)

REM 暂停以便查看结果
pause