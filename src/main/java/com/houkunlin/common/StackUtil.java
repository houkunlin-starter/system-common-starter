package com.houkunlin.common;

/**
 * @author HouKunLin
 */
public class StackUtil {
    private static final String CLASS_NAME = StackUtil.class.getName();

    private StackUtil() {
    }

    /**
     * 获得调用栈中的方法代码。
     * 例如有一个方法 test()，想要在 test() 方法中拿到是谁调用了它，则可以使用此方法
     *
     * @return 方法代码
     */
    public static StackTraceElement getParentStackTraceElement() {
        return getParentStackTraceElement(StackUtil.class);
    }

    /**
     * 获得调用栈中的堆栈跟踪元素。
     * 例如有一个方法 test()，想要在 test() 方法中拿到是谁调用了它，则可以使用此方法
     *
     * @param currentClass 调用此方法的class对象
     * @return 方法代码
     */
    public static StackTraceElement getParentStackTraceElement(Class<?> currentClass) {
        final String className = currentClass.getName();
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length <= 3) {
            return null;
        }
        for (int i = 2; i < stackTrace.length; i++) {
            final StackTraceElement traceElement = stackTrace[i];
            final String elementClassName = traceElement.getClassName();
            if (!className.equals(elementClassName) && !CLASS_NAME.equals(elementClassName)) {
                return traceElement;
            }
        }
        return null;
    }
}
