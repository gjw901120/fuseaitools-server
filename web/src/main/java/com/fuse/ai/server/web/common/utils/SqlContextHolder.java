package com.fuse.ai.server.web.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL上下文持有器 - 使用ThreadLocal收集单次请求内的SQL执行信息
 * 供RequestLogFilter在请求结束时汇总输出
 */
public class SqlContextHolder {

    private static final ThreadLocal<List<SqlLogEntry>> SQL_LOGS = ThreadLocal.withInitial(ArrayList::new);

    /**
     * 记录一条SQL执行信息
     */
    public static void addSqlLog(String sql, List<String> parameters, long costTime) {
        SQL_LOGS.get().add(new SqlLogEntry(sql, parameters, costTime));
    }

    /**
     * 获取当前请求的所有SQL日志
     */
    public static List<SqlLogEntry> getSqlLogs() {
        return SQL_LOGS.get();
    }

    /**
     * 获取SQL总数量
     */
    public static int getSqlCount() {
        return SQL_LOGS.get().size();
    }

    /**
     * 获取SQL总耗时
     */
    public static long getTotalCostTime() {
        return SQL_LOGS.get().stream().mapToLong(SqlLogEntry::getCostTime).sum();
    }

    /**
     * 清理ThreadLocal，必须在请求结束时调用
     */
    public static void clear() {
        SQL_LOGS.remove();
    }

    /**
     * SQL日志条目
     */
    public static class SqlLogEntry {
        private final String sql;
        private final List<String> parameters;
        private final long costTime;

        public SqlLogEntry(String sql, List<String> parameters, long costTime) {
            this.sql = sql;
            this.parameters = parameters;
            this.costTime = costTime;
        }

        public String getSql() {
            return sql;
        }

        public List<String> getParameters() {
            return parameters;
        }

        public long getCostTime() {
            return costTime;
        }
    }
}
