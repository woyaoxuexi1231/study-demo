package org.hulei.jdk.jdk.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;


public class JvmUtil {

    public static long getMxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * 获取进程 id
     *
     * @return 进程 id
     */
    public static int getProcessID() {
        //  ManagementFactory是一个在运行时管理和监控 Java VM 的工厂类
        //  它能提供很多管理 JVM 的静态接口的运行时实例，比如 RuntimeMXBean
        //  RuntimeMXBean 是 Java 虚拟机的运行时管理接口.
        //  取得 JVM 运行管理实例，到管理接口句柄
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        //  取得 jVM 运行管理实例的名称，也是 JVM 运行实例的名称
        String jvmInstanceName = runtimeMXBean.getName();
        return Integer.parseInt(jvmInstanceName.split("@")[0]);
    }

    /**
     * 判断当前的系统信息
     *
     * @return 是否位 windows 系统
     */
    public static boolean isWin() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().startsWith("win");
    }
}
