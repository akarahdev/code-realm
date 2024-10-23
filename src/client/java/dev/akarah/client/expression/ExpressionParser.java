package dev.akarah.client.expression;

import com.mojang.brigadier.StringReader;
import joptsimple.ValueConversionException;

public class ExpressionParser {
    StringReader reader;

    public static String parse(String string) {
        var p = new ExpressionParser();
        p.reader = new StringReader(string);
        return p.parseExpression().toVarItemExpression();
    }

    public Expression parseExpression() {
        return parseFactor();
    }

    public Expression parseFactor() {
        var expr = parseTerm();
        while (true) {
            reader.skipWhitespace();
            if (reader.peek() == '*') {
                reader.skip();
                expr = new Expression.Mul(expr, parseTerm());
            } else if (reader.peek() == '/') {
                reader.skip();
                expr = new Expression.Div(expr, parseTerm());
            } else break;
        }
        return expr;
    }

    public Expression parseTerm() {
        var expr = parseNegation();
        while (true) {
            reader.skipWhitespace();
            if (reader.peek() == '+') {
                reader.skip();
                expr = new Expression.Add(expr, parseNegation());
            } else if (reader.peek() == '-') {
                reader.skip();
                expr = new Expression.Sub(expr, parseNegation());
            } else break;
        }
        return expr;
    }

    public Expression parseNegation() {
        Expression expr = null;
        while(true) {
            reader.skipWhitespace();
            if(reader.peek() == '-') {
                reader.skip();
                if(expr == null)
                    expr = parsePostfix();
                expr = new Expression.Mul(expr, new Expression.Number(-1));
            } else {
                if(expr == null)
                    expr = parsePostfix();
                break;
            }
        }
        return expr;
    }

    public Expression parsePostfix() {
        var expr = parseValue();
        while(true) {
            reader.skipWhitespace();
            if(reader.peek() == '[') {
                reader.skip();
                var inner = parseExpression();
                if(reader.peek() != ']')
                    throw new ValueConversionException("expected ']'");
                reader.read();
                expr = new Expression.Subscript(expr, inner);
            } else break;
        }
        return expr;
    }

    public Expression parseValue() {
        System.out.println(reader.getRemaining());
        try {
            reader.skipWhitespace();
            if (Character.isJavaIdentifierStart(reader.peek()) || reader.peek() == '%') {
                var sb = new StringBuilder();
                sb.append(reader.read());
                try {
                    while (Character.isJavaIdentifierPart(reader.peek()) || reader.peek() == ' ') {
                        sb.append(reader.read());
                    }
                } catch (IndexOutOfBoundsException exception) {
                }
                return new Expression.Variable(sb.toString().trim());
            } else if (Character.isDigit(reader.peek())) {
                var sb = new StringBuilder();
                sb.append(reader.read());

                try {
                    while (Character.isDigit(reader.peek()) || reader.peek() == ' ') {
                        sb.append(reader.read());
                    }
                } catch (IndexOutOfBoundsException exception) {
                }

                return new Expression.Number(Double.parseDouble(sb.toString().trim().replace(" ", "")));
            } else if(reader.peek() == '(') {
                reader.read();
                var expr = parseExpression();
                if(reader.peek() != ')')
                    throw new ValueConversionException("expected ')");
                reader.read();
                return expr;
            }
        } catch (StringIndexOutOfBoundsException exception) {}
        reader.skipWhitespace();
        System.out.println(reader.getRemaining());
        throw new ValueConversionException("expected a valid value in expression");
    }
}
