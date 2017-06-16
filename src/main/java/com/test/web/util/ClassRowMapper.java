package com.test.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 青葱的熊猫 on 2017/5/27.
 * 根据SELECT-SQL返回的数据自动封装实体对象
 * 其实傻了,直接使用
 * List<Map<String, Object>> list = getJdbcTemplate().queryForList("select * from player")
 * 在使用JSON封装MAP,在把JSON反转为VO,就可以轻松解决问题了。但是也有诸多不灵活之处
 */
public class ClassRowMapper<T> implements RowMapper<T> {
    private Class<T> clazz;

    private String setStr = "set";

    private List<String> listField = new ArrayList<String>();

    private Map<String, Object> mapValue = new HashMap<String, Object>();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 入参为对象类型是为了解决需要保存原有对象属性值
     */
    public ClassRowMapper(Class<T> clazz){
        this.clazz = clazz;
    }

    /**
     * 获取属性类型
     * @param type
     * @return
     */
    private Class<?> getClassType(Class<?> type){
        if ((type == long.class) || (type == Long.class)) {
            return Long.class;
        } else if ((type == int.class) || (type == Integer.class)) {
            return Integer.class;
        } else if ((type == char.class) || (type == Character.class)) {
            return Character.class;
        } else if ((type == short.class) || (type == Short.class)) {
            return Short.class;
        } else if ((type == double.class) || (type == Double.class)) {
            return Double.class;
        } else if ((type == float.class) || (type == Float.class)) {
            return Float.class;
        } else if ((type == boolean.class) || (type == Boolean.class)) {
            return Boolean.class;
        } else if (type == String.class) {
            return String.class;
        } else if (type == BigDecimal.class) {
            return BigDecimal.class;
        } else if (type == java.sql.Date.class) {
            return Date.class;
        } else if (type == java.sql.Timestamp.class || type == java.util.Date.class) {
            return Date.class;
        }
        return Object.class;
    }



    /**
     * 首字母大写
     * char[] cs=name.toCharArray();
     * cs[0]-=32;
     * return String.valueOf(cs);  //不是不用，不过首先需要检查下ascii是否大写
     * @param name 带转换字符串
     * @return
     */
    private String captureName(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return  name;
    }

    /**
     * 根据结果集获取所有SET方法
     * 追加一句代码可获取所有SET\GET方法
     * @param rs JDBC结果集
     */
    private void findMethod(ResultSet rs){
        try {
            Object value;
            String fieldName;
            //查询结果元数据信息
            ResultSetMetaData rsmd = rs.getMetaData();
            //查询结果列数
            int count = rsmd.getColumnCount();
            for(int i=1;i<=count;i++){
                fieldName = rsmd.getColumnLabel(i);
                value = rs.getObject(i);
                listField.add(fieldName);
                mapValue.put(fieldName, value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否存在于结果集遍历出的方法
     * @param methodName 待验证方法名
     * @return true:存在|false:不存在
     */
    private boolean booleanField(String methodName){
        boolean flag = true;
        if(!listField.contains(methodName)){
            flag = false;
        }
        return flag;
    }

    /**
     * 分装从结果集获取的数据到实体对象
     * @param rs
     * @param value
     * @return
     * @throws SQLException
     */
    public T mapRow(ResultSet rs, int value) throws SQLException {
        try {
            String field;
            Method method;
            String methodName;
            this.findMethod(rs);
            Field[] fields = this.clazz.getDeclaredFields();
            T t = clazz.newInstance();
            for(Field f : fields){
                field = f.getName().toUpperCase();
                methodName = setStr + this.captureName(f.getName());
                if(this.booleanField(field)){
                    method = this.clazz.getMethod(methodName,
                            this.getClassType(f.getType()));
                    method.invoke(t, mapValue.get(field));
                }
            }
            return t;
        } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
