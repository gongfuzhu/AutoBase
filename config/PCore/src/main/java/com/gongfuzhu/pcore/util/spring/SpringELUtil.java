package com.gongfuzhu.pcore.util.spring;


import com.gongfuzhu.pcore.util.bean.BeanUtil;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * EL 表达式
 */
public class SpringELUtil {


    /**
     * 执行el表达式
     *
     * @param beanObj
     * @param expression
     * @param <T>
     * @return
     */
    public static <T> T parseExpression(Object beanObj, String expression) {
        Map<String, Object> ret = (beanObj instanceof Map) ? (Map<String, Object>) beanObj : BeanUtil.bean2Map(beanObj);
        return parseExpression(ret, expression);
    }


    /**
     * 执行el表达式
     *
     * @param val
     * @param expression
     * @param <T>
     * @return
     */
    public static <T> T parseExpression(Map<String, Object> val, String expression) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(expression);
        EvaluationContext ctx = null;
        if (val != null) {
            ctx = new StandardEvaluationContext();
            //在上下文中设置变量，变量名为user，内容为user对象
            for (Map.Entry<String, Object> entry : val.entrySet()) {
                ctx.setVariable(entry.getKey(), entry.getValue());
            }
        }
        return (T) (ctx == null ? exp.getValue() : exp.getValue(ctx));
    }


    /**
     * 执行el表达式
     *
     * @param expression
     * @param <T>
     * @return
     */
    public static <T> T parseExpression(String expression) {
        return parseExpression(null, expression);
    }

}
