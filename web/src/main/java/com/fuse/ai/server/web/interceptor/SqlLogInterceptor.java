package com.fuse.ai.server.web.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * SQL日志拦截器 - 拦截所有SQL执行并记录日志
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update",
                args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
@Component
public class SqlLogInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger("SQL_LOGGER");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String TRACE_ID = "traceId";
    private static final long SLOW_SQL_THRESHOLD = 1000; // 慢SQL阈值1秒
    private static final int MAX_SQL_LENGTH = 1000; // SQL最大长度

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取执行信息
        Method method = invocation.getMethod();
        String methodName = method.getName();
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

        // 记录开始时间
        long startTime = System.currentTimeMillis();
        Object result = null;
        Throwable exception = null;

        try {
            // 执行SQL
            result = invocation.proceed();
            return result;
        } catch (Throwable e) {
            exception = e;
            throw e;
        } finally {
            try {
                // 记录SQL日志
                recordSqlLog(invocation, mappedStatement, methodName, startTime, result, exception);
            } catch (Exception e) {
                // 记录日志时发生异常，不干扰正常流程
                logger.error("记录SQL日志失败", e);
            }
        }
    }

    private void recordSqlLog(Invocation invocation,
                              MappedStatement mappedStatement,
                              String methodName,
                              long startTime,
                              Object result,
                              Throwable exception) {
        // 计算耗时
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;

        // 获取TraceId
        String traceId = MDC.get(TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = "SYSTEM-" + System.currentTimeMillis();
        }

        // 获取SQL信息
        Object parameter = invocation.getArgs().length > 1 ? invocation.getArgs()[1] : null;
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();

        // 获取SQL语句（带参数）
        String sqlWithParams = getSqlWithParameters(configuration, boundSql);

        // 获取SQL类型
        String sqlType = getSqlType(methodName);

        // 获取Mapper方法
        String mapperMethod = mappedStatement.getId();

        // 构建日志信息
        Map<String, Object> logData = new LinkedHashMap<>();
        logData.put("traceId", traceId);
        logData.put("mapperMethod", mapperMethod);
        logData.put("sqlType", sqlType);
        logData.put("costTime", costTime + "ms");
        logData.put("timestamp", DATE_FORMAT.format(new Date()));

        // 处理SQL语句
        String formattedSql = formatSql(sqlWithParams);
        logData.put("sql", formattedSql);

        // 如果是更新操作，记录影响行数
        if ("update".equals(methodName) && result instanceof Integer) {
            int affectedRows = (Integer) result;
            logData.put("affectedRows", affectedRows);
        }

        // 记录执行结果
        if (exception != null) {
            logData.put("error", getExceptionInfo(exception));
            logSqlError(logData, costTime);
        } else if (costTime > SLOW_SQL_THRESHOLD) {
            logData.put("slow", true);
            logSlowSql(logData, costTime);
        } else {
            logSqlSuccess(logData, costTime);
        }
    }

    private String getSqlWithParameters(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        // 原始SQL
        String sql = boundSql.getSql();

        // 如果没有参数，直接返回
        if (parameterObject == null || parameterMappings == null || parameterMappings.isEmpty()) {
            return sql;
        }

        try {
            // 替换参数占位符
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                // 简单类型参数
                sql = sql.replaceFirst("\\?", formatParameterValue(parameterObject));
            } else {
                // 复杂对象参数
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object value = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", formatParameterValue(value));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object value = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", formatParameterValue(value));
                    }
                }
            }
        } catch (Exception e) {
            // 参数替换失败，返回不带参数的SQL
            logger.warn("SQL参数替换失败，返回原始SQL", e);
        }

        return sql;
    }

    private String formatParameterValue(Object value) {
        if (value == null) {
            return "null";
        }

        if (value instanceof String) {
            return "'" + value.toString().replace("'", "''") + "'";
        } else if (value instanceof Date) {
            return "'" + DATE_FORMAT.format(value) + "'";
        } else if (value instanceof Number) {
            return value.toString();
        } else if (value instanceof Boolean) {
            return ((Boolean) value) ? "1" : "0";
        } else {
            return "'" + value.toString() + "'";
        }
    }

    private String formatSql(String sql) {
        if (sql == null) {
            return "";
        }

        // 移除多余空格和换行
        sql = sql.replaceAll("[\\s]+", " ").trim();

        // 限制SQL长度
        if (sql.length() > MAX_SQL_LENGTH) {
            sql = sql.substring(0, MAX_SQL_LENGTH) + "...";
        }

        return sql;
    }

    private String getSqlType(String methodName) {
        return switch (methodName) {
            case "update" -> "UPDATE";
            case "query" -> "SELECT";
            case "insert" -> "INSERT";
            case "delete" -> "DELETE";
            default -> "UNKNOWN";
        };
    }

    private String getExceptionInfo(Throwable exception) {
        if (exception == null) {
            return "未知异常";
        }

        if (exception instanceof SQLException sqlException) {
            return String.format("SQL错误[%s]: %s",
                    sqlException.getSQLState(), sqlException.getMessage());
        }

        return exception.getClass().getSimpleName() + ": " + exception.getMessage();
    }

    private void logSqlSuccess(Map<String, Object> logData, long costTime) {
        if (logger.isDebugEnabled()) {
            logger.debug("SQL执行成功: {}", formatLogData(logData));
        }
    }

    private void logSlowSql(Map<String, Object> logData, long costTime) {
        logger.warn("慢SQL警告({}ms): {}", costTime, formatLogData(logData));
    }

    private void logSqlError(Map<String, Object> logData, long costTime) {
        logger.error("SQL执行异常({}ms): {}", costTime, formatLogData(logData));
    }

    private String formatLogData(Map<String, Object> logData) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        boolean first = true;
        for (Map.Entry<String, Object> entry : logData.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append("\"").append(entry.getKey()).append("\": \"");

            Object value = entry.getValue();
            if (value != null) {
                String strValue = value.toString();
                // 转义双引号
                strValue = strValue.replace("\"", "\\\"");
                sb.append(strValue);
            }

            sb.append("\"");
            first = false;
        }

        sb.append("}");
        return sb.toString();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以配置属性
    }
}