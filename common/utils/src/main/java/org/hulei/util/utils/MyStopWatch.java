package org.hulei.util.utils;

import cn.hutool.core.io.FileUtil;

import java.text.NumberFormat;

/**
 * @author hulei
 * @since 2025/7/27 18:40
 */

public class MyStopWatch extends cn.hutool.core.date.StopWatch {

    @Override
    public String prettyPrint() {
        StringBuilder sb = new StringBuilder(shortSummary());
        sb.append(FileUtil.getLineSeparator());
        if (null == this.getTaskInfo()) {
            sb.append("No task info kept");
        } else {
            sb.append("---------------------------------------------").append(FileUtil.getLineSeparator());
            sb.append("s          %     Task name").append(FileUtil.getLineSeparator());  // 修改标题从ns到s
            sb.append("---------------------------------------------").append(FileUtil.getLineSeparator());

            final NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMinimumIntegerDigits(3);  // 减少整数位数要求，因为秒数通常较小
            nf.setMinimumFractionDigits(6); // 增加小数位数显示纳秒精度
            nf.setMaximumFractionDigits(6); // 限制小数位数
            nf.setGroupingUsed(false);

            final NumberFormat pf = NumberFormat.getPercentInstance();
            pf.setMinimumIntegerDigits(3);
            pf.setGroupingUsed(false);

            for (TaskInfo task : getTaskInfo()) {
                // 将纳秒转换为秒 (1秒 = 1_000_000_000纳秒)
                double seconds = task.getTimeNanos() / 1_000_000_000.0;
                sb.append(nf.format(seconds)).append("  ");
                sb.append(pf.format((double) task.getTimeNanos() / getTotalTimeNanos())).append("  ");
                sb.append(task.getTaskName()).append(FileUtil.getLineSeparator());
            }
        }
        return sb.toString();
    }
}
