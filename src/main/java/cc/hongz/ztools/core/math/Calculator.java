package cc.hongz.ztools.core.math;

import cc.hongz.ztools.core.util.CharUtil;
import cc.hongz.ztools.core.util.NumberUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Stack;

/**
 * 数学表达式计算工具类.
 * <p>
 * 供算术验证码解析形如 {@code 7+3=} 的表达式，将中缀式转后缀式再求值.
 *
 * @author looly
 * @since 5.4.3
 */
public class Calculator {
    /**
     * 后缀式栈.
     */
    private final Stack<String> postfixStack = new Stack<>();
    /**
     * 运算符优先级表（以 ASCII - 40 为索引）.
     */
    private final int[] operatPriority =
            new int[]{0, 3, 2, 1, -1, 1, 0, 2};

    /**
     * 静态入口：计算表达式的值.
     *
     * @param expression 表达式
     * @return 计算结果
     */
    public static double conversion(final String expression) {
        return (new Calculator()).calculate(expression);
    }

    /**
     * 按照给定的表达式计算.
     *
     * @param expression 要计算的表达式，例如 {@code 5+12*(3+5)/7}
     * @return 计算结果
     */
    public double calculate(final String expression) {
        prepare(transform(expression));

        final Stack<String> resultStack = new Stack<>();
        Collections.reverse(postfixStack);
        String firstValue;
        String secondValue;
        String currentOp;
        while (false == postfixStack.isEmpty()) {
            currentOp = postfixStack.pop();
            if (false == isOperator(currentOp.charAt(0))) {
                currentOp = currentOp.replace("~", "-");
                resultStack.push(currentOp);
            } else {
                secondValue = resultStack.pop();
                firstValue = resultStack.pop();

                firstValue = firstValue.replace("~", "-");
                secondValue = secondValue.replace("~", "-");

                final BigDecimal tempResult = calculate(firstValue,
                        secondValue, currentOp.charAt(0));
                resultStack.push(tempResult.toString());
            }
        }

        return NumberUtil.mul(resultStack.toArray(new String[0]))
                .doubleValue();
    }

    /**
     * 数据准备阶段，将中缀表达式转换为后缀式栈.
     *
     * @param expression 表达式
     */
    private void prepare(final String expression) {
        final Stack<Character> opStack = new Stack<>();
        opStack.push(',');
        final char[] arr = expression.toCharArray();
        int currentIndex = 0;
        int count = 0;
        char currentOp;
        char peekOp;
        for (int i = 0; i < arr.length; i++) {
            currentOp = arr[i];
            if (isOperator(currentOp)) {
                if (count > 0) {
                    postfixStack.push(new String(arr, currentIndex, count));
                }
                peekOp = opStack.peek();
                if (currentOp == ')') {
                    while (opStack.peek() != '(') {
                        postfixStack.push(String.valueOf(opStack.pop()));
                    }
                    opStack.pop();
                } else {
                    while (currentOp != '(' && peekOp != ','
                            && compare(currentOp, peekOp)) {
                        postfixStack.push(String.valueOf(opStack.pop()));
                        peekOp = opStack.peek();
                    }
                    opStack.push(currentOp);
                }
                count = 0;
                currentIndex = i + 1;
            } else {
                count++;
            }
        }
        if (count > 1 || (count == 1 && currentIndex < arr.length
                && !isOperator(arr[currentIndex]))) {
            postfixStack.push(new String(arr, currentIndex, count));
        }

        while (opStack.peek() != ',') {
            postfixStack.push(String.valueOf(opStack.pop()));
        }
    }

    /**
     * 判断是否为算术符号.
     *
     * @param c 字符
     * @return 是否为算术符号
     */
    private boolean isOperator(final char c) {
        return c == '+' || c == '-' || c == '*' || c == '/'
                || c == '(' || c == ')' || c == '%';
    }

    /**
     * 比较运算符优先级.
     * <p>
     * 若 peek 优先级高于或等于 cur 则返回 true.
     *
     * @param cur  当前运算符
     * @param peek 栈顶运算符
     * @return peek 是否不低于 cur
     */
    private boolean compare(final char cur, final char peek) {
        final int offset = 40;
        char c = cur;
        char p = peek;
        if (c == '%') {
            c = '/';
        }
        if (p == '%') {
            p = '/';
        }

        return operatPriority[p - offset] >= operatPriority[c - offset];
    }

    /**
     * 按照给定的算术运算符做计算.
     *
     * @param firstValue  第一个值
     * @param secondValue 第二个值
     * @param currentOp   算术符
     * @return 结果
     * @throws ArithmeticException 除数或模数为 0 时
     */
    private BigDecimal calculate(final String firstValue,
            final String secondValue, final char currentOp) {
        final BigDecimal first = NumberUtil.toBigDecimal(firstValue);
        final BigDecimal second = NumberUtil.toBigDecimal(secondValue);

        if ((currentOp == '/' || currentOp == '%')
                && second.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException(
                    "Division by zero: cannot divide " + firstValue
                    + " by zero");
        }

        final BigDecimal result;
        switch (currentOp) {
            case '+':
                result = NumberUtil.add(firstValue, secondValue);
                break;
            case '-':
                result = NumberUtil.sub(firstValue, secondValue);
                break;
            case '*':
                result = NumberUtil.mul(firstValue, secondValue);
                break;
            case '/':
                result = NumberUtil.div(firstValue, secondValue);
                break;
            case '%':
                result = NumberUtil.toBigDecimal(firstValue)
                        .remainder(NumberUtil.toBigDecimal(secondValue));
                break;
            default:
                throw new IllegalStateException(
                        "Unexpected value: " + currentOp);
        }
        return result;
    }

    /**
     * 将表达式中的一元负号转换为内部标记（~），便于后续解析.
     *
     * @param expression 表达式
     * @return 转换后的表达式
     */
    private static String transform(String expression) {
        expression = StrUtil.cleanBlank(expression);
        expression = StrUtil.removeSuffix(expression, "=");
        final char[] arr = expression.toCharArray();

        final StringBuilder out = new StringBuilder(arr.length);
        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];

            if (CharUtil.equals(c, 'x', true)) {
                out.append('*');
                continue;
            }

            if (c == '+' || c == '-') {
                final int outLen = out.length();
                if (outLen > 0) {
                    final char prevOut = out.charAt(outLen - 1);
                    if (prevOut == 'e' || prevOut == 'E') {
                        if (c == '-') {
                            out.append('~');
                        }
                        continue;
                    }
                }

                int j = i - 1;
                while (j >= 0 && Character.isWhitespace(arr[j])) {
                    j--;
                }
                final boolean unaryContext = (j < 0)
                        || isPrevCharOperatorOrLeftParen(arr[j]);

                if (unaryContext) {
                    int k = i;
                    int minusCount = 0;
                    while (k < arr.length
                            && (arr[k] == '+' || arr[k] == '-')) {
                        if (arr[k] == '-') {
                            minusCount++;
                        }
                        k++;
                    }
                    final boolean netNegative =
                            (minusCount % 2 == 1);
                    if (netNegative) {
                        out.append('~');
                    }
                    i = k - 1;
                } else {
                    out.append(c);
                }
                continue;
            }
            out.append(c);
        }

        final String result = out.toString();
        final char[] resArr = result.toCharArray();
        if (resArr.length >= 2 && resArr[0] == '~' && resArr[1] == '(') {
            resArr[0] = '-';
            return "0" + new String(resArr);
        } else {
            return result;
        }
    }

    /**
     * 判断字符是否为运算符或左括号（用于判定一元负号上下文）.
     *
     * @param c 字符
     * @return 是否为运算符或左括号
     */
    private static boolean isPrevCharOperatorOrLeftParen(final char c) {
        return c == '%' || c == '+' || c == '-' || c == '*'
                || c == '/' || c == '(';
    }
}
