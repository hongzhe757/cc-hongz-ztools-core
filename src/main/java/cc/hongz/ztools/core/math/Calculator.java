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
 * <p>
 * 设计模式：工具类（Utility Class）对外仅暴露 {@link #conversion(String)} 静态入口；
 * 实例内部以栈结构扮演解释器（Interpreter），逐步解析运算符优先级与一元负号.
 *
 * @author trainliang, looly
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
	private final int[] operatPriority = new int[]{0, 3, 2, 1, -1, 1, 0, 2};

	/**
	 * 静态入口：计算表达式的值.
	 *
	 * @param expression 表达式
	 * @return 计算结果
	 */
	public static double conversion(String expression) {
		return (new Calculator()).calculate(expression);
	}

	/**
	 * 按照给定的表达式计算.
	 *
	 * @param expression 要计算的表达式，例如 {@code 5+12*(3+5)/7}
	 * @return 计算结果
	 */
	public double calculate(String expression) {
		prepare(transform(expression));

		final Stack<String> resultStack = new Stack<>();
		// 将后缀式栈反转后再参与运算
		Collections.reverse(postfixStack);
		String firstValue, secondValue, currentOp;
		while (false == postfixStack.isEmpty()) {
			currentOp = postfixStack.pop();
			if (false == isOperator(currentOp.charAt(0))) {
				// 操作数：将内部一元负号标记还原为负号
				currentOp = currentOp.replace("~", "-");
				resultStack.push(currentOp);
			} else {
				// 运算符：弹出两个操作数求值
				secondValue = resultStack.pop();
				firstValue = resultStack.pop();

				firstValue = firstValue.replace("~", "-");
				secondValue = secondValue.replace("~", "-");

				final BigDecimal tempResult = calculate(firstValue, secondValue, currentOp.charAt(0));
				resultStack.push(tempResult.toString());
			}
		}

		// 结果栈若有多个数字，按乘法连乘（兼容省略 * 的写法，如 (1+2)3）
		return NumberUtil.mul(resultStack.toArray(new String[0])).doubleValue();
	}

	/**
	 * 数据准备阶段，将中缀表达式转换为后缀式栈.
	 *
	 * @param expression 表达式
	 */
	private void prepare(String expression) {
		final Stack<Character> opStack = new Stack<>();
		// 逗号作为栈底哨兵，优先级最低
		opStack.push(',');
		final char[] arr = expression.toCharArray();
		int currentIndex = 0;
		int count = 0;
		char currentOp, peekOp;
		for (int i = 0; i < arr.length; i++) {
			currentOp = arr[i];
			if (isOperator(currentOp)) {
				if (count > 0) {
					// 写入两运算符之间的操作数
					postfixStack.push(new String(arr, currentIndex, count));
				}
				peekOp = opStack.peek();
				if (currentOp == ')') {
					// 右括号：弹出运算符至左括号
					while (opStack.peek() != '(') {
						postfixStack.push(String.valueOf(opStack.pop()));
					}
					opStack.pop();
				} else {
					while (currentOp != '(' && peekOp != ',' && compare(currentOp, peekOp)) {
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
		// 防止数组越界：写入末尾剩余操作数
		if (count > 1 || (count == 1 && currentIndex < arr.length && !isOperator(arr[currentIndex]))) {
			postfixStack.push(new String(arr, currentIndex, count));
		}

		while (opStack.peek() != ',') {
			// 将运算符栈剩余元素写入后缀式栈
			postfixStack.push(String.valueOf(opStack.pop()));
		}
	}

	/**
	 * 判断是否为算术符号.
	 *
	 * @param c 字符
	 * @return 是否为算术符号
	 */
	private boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')' || c == '%';
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
	private boolean compare(char cur, char peek) {
		final int offset = 40;
		if (cur == '%') {
			// 取模视为最高优先级
			cur = 47;
		}
		if (peek == '%') {
			peek = 47;
		}

		return operatPriority[peek - offset] >= operatPriority[cur - offset];
	}

	/**
	 * 按照给定的算术运算符做计算.
	 *
	 * @param firstValue  第一个值
	 * @param secondValue 第二个值
	 * @param currentOp   算术符，只支持 {@code +}、{@code -}、{@code *}、{@code /}、{@code %}
	 * @return 结果
	 * @throws ArithmeticException 除数或模数为 0 时
	 */
	private BigDecimal calculate(String firstValue, String secondValue, char currentOp) {
		final BigDecimal first = NumberUtil.toBigDecimal(firstValue);
		final BigDecimal second = NumberUtil.toBigDecimal(secondValue);

		// 除零检查
		if ((currentOp == '/' || currentOp == '%') && second.compareTo(BigDecimal.ZERO) == 0) {
			throw new ArithmeticException("Division by zero: cannot divide " + firstValue + " by zero");
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
				result = NumberUtil.toBigDecimal(firstValue).remainder(NumberUtil.toBigDecimal(secondValue));
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + currentOp);
		}
		return result;
	}

	/**
	 * 将表达式中的一元负号转换为内部标记（~），便于后续解析。
	 * 规则说明：
	 *  - 科学计数法整体识别为数字，e/E 后的 + 或 - 属于指数符号，不参与一元符号折叠。
	 *  - 一元 + / - 仅在表达式开头或运算符、左括号之后生效；可折叠连续符号，如 --3、+-3 -> ~3。
	 * 示例：
	 *  - 输入：-2+-1*(-3E-2)-(-1)
	 *  - 输出：~2+~1*(~3E~2)-(~1)
	 */
	private static String transform(String expression) {
		expression = StrUtil.cleanBlank(expression);
		expression = StrUtil.removeSuffix(expression, "=");
		final char[] arr = expression.toCharArray();

		final StringBuilder out = new StringBuilder(arr.length);
		for (int i = 0; i < arr.length; i++) {
			char c = arr[i];

			// 将 x 或 X 视为乘法符
			if (CharUtil.equals(c, 'x', true)) {
				out.append('*');
				continue;
			}

			// 判断 + / - 是科学计数法符号、二元运算符还是一元运算符
			if (c == '+' || c == '-') {
				int outLen = out.length();
				if (outLen > 0) {
					char prevOut = out.charAt(outLen - 1);
					if (prevOut == 'e' || prevOut == 'E') {
						// e/E 后：+ 可丢弃；- 用 ~ 标记，后续还原
						if (c == '-') {
							out.append('~');
						}
						continue;
					}
				}

				// 查找前一个非空字符，判断是否为一元上下文
				int j = i - 1;
				while (j >= 0 && Character.isWhitespace(arr[j])) j--;
				boolean unaryContext = (j < 0) || isPrevCharOperatorOrLeftParen(arr[j]);

				if (unaryContext) {
					// 合并连续一元符号（如 --3、+-3）
					int k = i;
					int minusCount = 0;
					while (k < arr.length && (arr[k] == '+' || arr[k] == '-')) {
						if (arr[k] == '-') minusCount++;
						k++;
					}
					boolean netNegative = (minusCount % 2 == 1);
					if (netNegative) {
						// 用 ~ 标记一元负号
						out.append('~');
					}
					i = k - 1;
				} else {
					// 二元运算符
					out.append(c);
				}
				continue;
			}
			out.append(c);
		}

		// ~(  开头转为 0-( 形式
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
	private static boolean isPrevCharOperatorOrLeftParen(char c) {
		return c == '%' || c == '+' || c == '-' || c == '*' || c == '/' || c == '(';
	}
}
