#!/bin/bash

# 指定 jar 文件名
JAR_FILE="springboot-js.jar"

# 定义一个函数来查找并终止进程
terminate_jar_processes() {
  # 查找包含指定 jar 文件名的 Java 进程
  PIDS=$(ps aux | grep "$JAR_FILE" | grep -v grep | awk '{print $2}')

  # 检查是否有进程ID
  if [ -z "$PIDS" ]; then
    echo "No processes found running the jar file: $JAR_FILE"
  else
    # 终止进程
    for PID in $PIDS; do
      echo "Terminating process with PID: $PID"
      kill $PID
      # 检查是否需要使用 -9 强制终止
      if ! ps aux | grep -q " $PID "; then
        echo "Process $PID terminated successfully."
      else
        echo "Process $PID did not terminate gracefully. Killing forcefully."
        kill -9 $PID
        echo "Process $PID killed forcefully."
      fi
    done
  fi
}

# 使用 timeout 命令限制脚本执行时间
terminate_jar_processes

# 检查 timeout 是否因为超时而终止了脚本
if [ $? -eq 124 ]; then
  echo "Script timed out after 1 minute."
else
  echo "Script completed within the time limit."
fi

nohup java -jar "$JAR_FILE" > /dev/null 2>&1 &
