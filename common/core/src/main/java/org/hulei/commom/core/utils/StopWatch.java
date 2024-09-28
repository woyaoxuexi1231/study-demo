package org.hulei.commom.core.utils;

import java.text.NumberFormat;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.utils.segmentid
 * @className: StopWatch
 * @description:
 * @author: h1123
 * @createDate: 2023/11/4 14:48
 */

public class StopWatch extends org.springframework.util.StopWatch {

    @Override
    public String prettyPrint() {
        boolean isKeepLive = true;
        try {
            TaskInfo[] taskInfos = this.getTaskInfo();
            if (taskInfos.length < 1) {
                isKeepLive = false;
            }
        } catch (Exception e) {
            isKeepLive = false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        sb.append("StopWatch '").append(this.getId()).append("': running time = ").append(this.getTotalTimeSeconds()).append(" s");
        sb.append('\n');
        if (!isKeepLive) {
            sb.append("No task info kept");
        } else {
            sb.append("---------------------------------------------\n");
            sb.append("s         %     Task name\n");
            sb.append("---------------------------------------------\n");
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMinimumFractionDigits(3);
            nf.setGroupingUsed(false);
            NumberFormat pf = NumberFormat.getPercentInstance();
            pf.setMinimumIntegerDigits(2);
            pf.setMinimumFractionDigits(2);
            pf.setGroupingUsed(false);
            for (TaskInfo task : this.getTaskInfo()) {
                sb.append(nf.format(task.getTimeSeconds())).append("  ");
                sb.append(pf.format((double) task.getTimeNanos() / this.getTotalTimeNanos())).append("  ");
                sb.append(task.getTaskName()).append("\n");
            }
        }
        return sb.toString();
    }

    public StopWatch() {
        super();
    }

    public StopWatch(String id) {
        super(id);
    }
}
