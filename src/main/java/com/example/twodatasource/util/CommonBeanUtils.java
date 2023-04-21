package com.example.twodatasource.util;

import cn.hutool.core.bean.BeanUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author GEP
 * @DESCRIPTION 基本类型和String , Date转换工具类
 * @create 2019/5/24 0024
 */
@Slf4j
public class CommonBeanUtils {

    private final static Map<Class, Function<Object, String>> STRING_CONVERT_MAP = new HashMap<>();

    private final static Map<Class, Function<Object, Integer>> INTEGER_CONVERT_MAP = new HashMap<>();

    private final static Map<Class, Function<Object, Long>> LONG_CONVERT_MAP = new HashMap<>();

    private final static Map<Class, Function<Object, Byte>> BYTE_CONVERT_MAP = new HashMap<>();

    private final static Map<Class, Function<Object, Short>> SHORT_CONVERT_MAP = new HashMap<>();

    private final static Map<Class, Function<Object, Double>> DOUBLE_CONVERT_MAP = new HashMap<>();

    private final static Map<Class, Function<Object, Float>> FLOAT_CONVERT_MAP = new HashMap<>();

    private final static Function<Object, String> DEFAULT_STRING_CONVERT = String::valueOf;

    private final static Byte B_FALSE = 0;

    private final static Byte B_TRUE = 1;

    private final static Short S_FALSE = 0;

    private final static Short S_TRUE = 1;

    private final static Class[] REFERENCE_CLASSES = new Class[]{Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class};
    private final static Class[] BASE_CLASSES = {byte.class, short.class, int.class, long.class, float.class, double.class};

    private final static HashSet<Class> ALL_CLASS = new HashSet<>();

    static {

        ALL_CLASS.addAll(Arrays.asList(REFERENCE_CLASSES));
        ALL_CLASS.addAll(Arrays.asList(BASE_CLASSES));
        ALL_CLASS.add(String.class);
        ALL_CLASS.add(Boolean.class);
        ALL_CLASS.add(boolean.class);
        ALL_CLASS.add(Character.class);
        ALL_CLASS.add(char.class);
        ALL_CLASS.add(Date.class);


        putStrConvert(Date.class, date -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            return simpleDateFormat.format(date);
        });
        putStrConvert(Object[].class, Arrays::toString);

        putByteConvert(String.class, Byte::parseByte);
        putByteConvert(Boolean.class, flag -> flag ? B_TRUE : B_FALSE);
        putByteConvert(boolean.class, flag -> flag ? B_TRUE : B_FALSE);

        putShortConvert(String.class, Short::parseShort);
        putShortConvert(Boolean.class, flag -> flag ? S_TRUE : S_FALSE);
        putShortConvert(boolean.class, flag -> flag ? S_TRUE : S_FALSE);

        putIntegerConvert(String.class,Integer::parseInt);
        putIntegerConvert(Boolean.class, flag -> flag ? 1 : 0);
        putIntegerConvert(boolean.class, flag -> flag ? 1 : 0);
        putIntegerConvert(Character.class, Character::charValue);
        putIntegerConvert(char.class, c -> (int) c);

        putLongConvert(String.class, Long::parseLong);
        putLongConvert(Boolean.class, flag -> flag ? 1L : 0L);
        putLongConvert(boolean.class, flag -> flag ? 1L : 0L);
        putLongConvert(Character.class, Character::charValue);
        putLongConvert(char.class, c -> (long) c);
        putLongConvert(Date.class, Date::getTime);

        putFloatConvert(String.class,Float::parseFloat);
        putFloatConvert(Boolean.class, flag -> flag ? 1F : 0F);
        putFloatConvert(boolean.class, flag -> flag ? 1F : 0F);
        putFloatConvert(Character.class,Character::charValue);
        putFloatConvert(char.class, c -> (float) c);
        putFloatConvert(Date.class, date -> (float) date.getTime());

        putDoubleConvert(String.class, Double::parseDouble);
        putDoubleConvert(Boolean.class, flag -> flag ? 1d : 0d);
        putDoubleConvert(boolean.class, flag -> flag ? 1d : 0d);
        putDoubleConvert(Character.class, Character::charValue);
        putDoubleConvert(char.class, c -> (double) c);
        putDoubleConvert(Date.class, date -> (double) date.getTime());

    }

    private static <T> void putStrConvert(Class<T> tClass, Function<T, Object> function) {
        STRING_CONVERT_MAP.put(tClass, (Function) function);
    }

    private static <T> void putShortConvert(Class<T> tClass, Function<T, Object> function) {
        SHORT_CONVERT_MAP.put(tClass, (Function) function);
    }


    private static <T> void putByteConvert(Class<T> tClass, Function<T, Object> function) {
        BYTE_CONVERT_MAP.put(tClass, (Function) function);
    }

    private static <T> void putDoubleConvert(Class<T> tClass, Function<T, Object> function) {
        DOUBLE_CONVERT_MAP.put(tClass, (Function) function);
    }


    private static <T> void putFloatConvert(Class<T> tClass, Function<T, Object> function) {
        FLOAT_CONVERT_MAP.put(tClass, (Function) function);
    }

    private static <T> void putLongConvert(Class<T> tClass, Function<T, Object> function) {
        LONG_CONVERT_MAP.put(tClass, (Function) function);
    }


    private static <T> void putIntegerConvert(Class<T> tClass, Function<T, Object> function) {
        INTEGER_CONVERT_MAP.put(tClass, (Function) function);
    }


    public static String convertString(Object value) {
        Objects.requireNonNull(value);
        return STRING_CONVERT_MAP.getOrDefault(value.getClass(), DEFAULT_STRING_CONVERT).apply(value);
    }


    public static Integer convertInteger(Object value) {
        return convert(value, Integer.class, INTEGER_CONVERT_MAP, (data) -> {
            if (data instanceof Number) {
                return ((Number) data).intValue();
            }
            return (Integer) null;
        });
    }


    private static <T> T convert(Object value, Class<T> type, Map<Class, Function<Object, T>> functionMap, Function<Object, T> defaultNumberFunction) {
        Objects.requireNonNull(value);
        if (ALL_CLASS.contains(value.getClass())) {
            if (Arrays.asList(BASE_CLASSES).contains(value.getClass())) {
                return functionMap.getOrDefault(value.getClass(), (data) -> (T) data).apply(value);
            } else {
                return functionMap.getOrDefault(value.getClass(), defaultNumberFunction).apply(value);
            }
        }
        return null;
    }

    public static Short convertShort(Object value) {
        return convert(value, Short.class, SHORT_CONVERT_MAP, (data) -> {
            if (data instanceof Number) {
                return ((Number) data).shortValue();
            }
            return (Short) null;
        });
    }


    public static Byte convertByte(Object value) {
        return convert(value, Byte.class, BYTE_CONVERT_MAP, (data) -> {
            if (data instanceof Number) {
                return ((Number) data).byteValue();
            }
            return (Byte) null;
        });
    }


    public static Double convertDouble(Object value) {
        return convert(value, Double.class, DOUBLE_CONVERT_MAP, (data) -> {
            if (data instanceof Number) {
                return ((Number) data).doubleValue();
            }
            return (Double) null;
        });
    }

    public static Float convertFloat(Object value) {
        return convert(value, Float.class, FLOAT_CONVERT_MAP, (data) -> {
            if (data instanceof Number) {
                return ((Number) data).floatValue();
            }
            return (Float) null;
        });
    }

    public static Long convertLong(Object value) {
        return convert(value, Long.class, LONG_CONVERT_MAP, (data) -> {
            if (data instanceof Number) {
                return ((Number) data).longValue();
            }
            return (Long) null;
        });
    }

    /**
     * 对象属性拷贝
     * PS:注意这里的不能使用内部类进行对象属性的数值拷贝
     *
     * @param source source源对象
     * @param tClass target目标对象类
     * @param <S>    源对象
     * @param <T>    目标对象
     * @return 将源对象进行属性value拷贝
     */
    @SneakyThrows({IllegalAccessException.class, InstantiationException.class})
    public static <S, T> T beanCopy(S source, Class<T> tClass) {
        T target = tClass.newInstance();
        BeanUtil.copyProperties(source, target);
        return target;
    }

    /**
     * 将list源对象转换为目标对象list
     *
     * @param sourceList 源list对象
     * @param tClass     目标对象
     * @param <S>        源对象
     * @param <T>        目标对象
     * @return 目标对象list集合
     */
    public static <S, T> List<T> beansInListCopy(List<S> sourceList, Class<T> tClass) {
        return sourceList.stream()
                .map(source -> {
                    T target = null;
                    try {
                        target = tClass.newInstance();
                        BeanUtil.copyProperties(source, target);
                    } catch (InstantiationException | IllegalAccessException e) {
                        log.error("list中属性拷贝异常",e);
                    }
                    return target;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}





