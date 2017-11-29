package com.smtsvs.thermopylae2.util;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.service.IoService;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;

/**
 * 帮助工具类.
 *
 * @author Jin Yuan
 */
public class Util {

    /**
     * tcp传输buffer的大小.
     */
    public static final int DEFAULT_IO_BUF_SIZE = 64 * 1024;
    /**
     * 最小流水. 流水大于SRL_END，从此开始.
     */
    private static int SRL_START = 100000000;
    /**
     * 最大流水. 流水大于此时，从SRL_START开始.
     */
    private static final int SRL_END = 999999990;
    /**
     * 当前流水.
     */
    private static final AtomicInteger SRL = new AtomicInteger(
        Util.SRL_START);

    static {
        // 初始化srl
        Util.SRL.set(Util.SRL_START
            + (int)(System.currentTimeMillis() % (Util.SRL_START / 10)));
    }

    /**
     * 工具类，无需实例.
     */
    private Util() {}

    /**
     * 一个byte转到无符号int.
     *
     * @param src
     *        src
     * @return int
     */
    public static int byte2UnsignedInt(final byte src) {
        int iResult = src;
        if (iResult < 0)
            iResult += 256;
        return iResult;
    }

    /**
     * byte数组到int,数组长度最大4.
     *
     * @param src
     *        src
     * @return int
     */
    public static int byte2Int(final byte[] src) {
        if (src == null)
            return 0;
        if (src.length > 4)
            throw new IllegalArgumentException("int不能大于4byte");
        final byte[] temp = new byte[4];
        System.arraycopy(src, 0, temp, 4 - src.length, src.length);
        int result = 0;
        int iMove;
        int iTemp;
        for (int i = 0; i < 4; i++) {
            iMove = 8 * (3 - i);
            iTemp = Util.byte2UnsignedInt(temp[i]);
            result += iTemp << iMove;
        }
        return result;
    }

    /**
     * int到byte数组.
     *
     * @param src
     *        src
     * @return rst
     */
    public static byte[] int2byte(int src) {
        final byte[] result = new byte[4];
        int iMove;
        for (int i = 0; i < 4; i++) {
            iMove = 8 * (3 - i);
            result[i] = (byte)(src >> iMove);
            src -= result[i] << iMove;
        }
        return result;
    }

    /**
     * 打日志用.
     *
     * @param service
     *        service
     */
    public static void addLogFilter(final IoService service) {
        final LoggingFilter logging = new LoggingFilter();
       // logging.setMessageReceivedLogLevel(LogLevel.DEBUG);
        //logging.setMessageSentLogLevel(LogLevel.DEBUG);
        
        logging.setMessageReceivedLogLevel(LogLevel.INFO);
        logging.setMessageSentLogLevel(LogLevel.INFO);
        logging.setExceptionCaughtLogLevel(LogLevel.INFO);
        logging.setSessionClosedLogLevel(LogLevel.INFO);
        logging.setSessionCreatedLogLevel(LogLevel.INFO);
        logging.setSessionIdleLogLevel(LogLevel.INFO);
        logging.setSessionOpenedLogLevel(LogLevel.INFO);
        service.getFilterChain().addLast("FILTER_LOGGING", logging);
    }

    /**
     * 造一个流水.
     *
     * @param prex
     *        流水的前缀
     * @return 流水
     */
    public static String buildSrl(final String prex) {
        final int srl = Util.SRL.getAndIncrement();
        if (srl >= Util.SRL_END) {
            synchronized (Util.SRL) {
                if (Util.SRL.get() >= Util.SRL_END)
                    Util.SRL.set(Util.SRL_START);
            }
            return Util.buildSrl(prex);
        }
        return prex + '.' + srl;
    }
}
