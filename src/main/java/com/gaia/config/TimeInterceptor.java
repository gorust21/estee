package com.gaia.config;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;

@Intercepts(value = {@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class TimeInterceptor extends AbstractSqlParserHandler implements Interceptor {
    /**
     * 创建时间
     */
    private static final String CREATE_TIME = "createTime";
    /**
     * 更新时间
     */
    private static final String UPDATE_TIME = "updateTime";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        // SQL操作命令
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        // 获取新增或修改的对象参数
        Object parameter = invocation.getArgs()[1];
        // 获取对象中所有的私有成员变量（对应表字段）
        Field[] declaredFields = parameter.getClass().getDeclaredFields();
        if (parameter.getClass().getSuperclass() != null) {
            Field[] superField = parameter.getClass().getSuperclass().getDeclaredFields();
            declaredFields = ArrayUtils.addAll(declaredFields, superField);
        }
        // mybatis plus判断
        boolean plus= parameter.getClass().getDeclaredFields().length == 1 && parameter.getClass().getDeclaredFields()[0].getName().equals("serialVersionUID");

        //兼容mybatis plus
        if (plus) {
            Map<String, Object> updateParam = (Map<String, Object>) parameter;
            Class<?> updateParamType = updateParam.get("param1").getClass();
            declaredFields = updateParamType.getDeclaredFields();
            if (updateParamType.getSuperclass() != null) {
                Field[] superField = updateParamType.getSuperclass().getDeclaredFields();
                declaredFields = ArrayUtils.addAll(declaredFields, superField);
            }
        }
        String fieldName = null;
        for (Field field : declaredFields) {
            fieldName = field.getName();
            if (Objects.equals(CREATE_TIME, fieldName)) {
                if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                    field.setAccessible(true);
                    field.set(parameter, LocalDateTime.now());
                }
            }
            if (Objects.equals(UPDATE_TIME, fieldName)) {
                if (SqlCommandType.INSERT.equals(sqlCommandType) || SqlCommandType.UPDATE.equals(sqlCommandType)) {
                    field.setAccessible(true);
                    //兼容mybatis plus的update
                    if (plus) {
                        Map<String, Object> updateParam = (Map<String, Object>) parameter;
                        field.set(updateParam.get("param1"), LocalDateTime.now());
                    } else {
                        field.set(parameter, LocalDateTime.now());
                    }
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
