package utils;

import static core.RdfsConstants._1MB;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 18-5-21 下午4:52
 * @desc :
 */
public class TimeUtil {

    public static long start() {
        return System.currentTimeMillis();
    }


    public static double getPeriod(long start) {
        long period = System.currentTimeMillis() - start;
        return period / 1000.0;
    }

    public static void showSpeed(double period, long totalSize) {

        String format1 = "Total file size is %.2f KB, transmission time is %.2f s, speed is %.2f KB/s\n";
        String format2 = "Total file size is %.2f MB, transmission time is %.2f s, speed is %.2f MB/s\n";
        if (totalSize < _1MB) {
            double size = 1.0 * totalSize;
            double speed = size / period;
            System.out.printf(format1, 1.0 * totalSize, period, speed);
        } else {
            double size = 1.0 * totalSize / _1MB;
            double speed = size / period;
            System.out.printf(format2, (1.0 * totalSize) / _1MB, period, speed);
        }

    }
}
