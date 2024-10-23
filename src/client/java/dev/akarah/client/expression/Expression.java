package dev.akarah.client.expression;

public sealed interface Expression {
    String toVarItemExpression();

    record Number(double value) implements Expression {
        @Override
        public String toVarItemExpression() {
            return Double.toString(value);
        }
    }

    record StringExpr(String value) implements Expression {
        @Override
        public String toVarItemExpression() {
            return value;
        }
    }

    record StyledText(String value) implements Expression {
        @Override
        public String toVarItemExpression() {
            return value;
        }
    }

    record Variable(String variableName) implements Expression {
        @Override
        public String toVarItemExpression() {
            return "%var(" + variableName + ")";
        }
    }

    record Deref(Expression subexpression) implements Expression {
        @Override
        public String toVarItemExpression() {
            return "%var(" + subexpression.toVarItemExpression() + ")";
        }
    }

    record Add(Expression left, Expression right) implements Expression {
        @Override
        public String toVarItemExpression() {
            return "%math(" + left.toVarItemExpression() + "+" + right.toVarItemExpression() + ")";
        }
    }

    record Sub(Expression left, Expression right) implements Expression {
        @Override
        public String toVarItemExpression() {
            return "%math(" + left.toVarItemExpression() + "-" + right.toVarItemExpression() + ")";
        }
    }

    record Mul(Expression left, Expression right) implements Expression {
        @Override
        public String toVarItemExpression() {
            return "%math(" + left.toVarItemExpression() + "*" + right.toVarItemExpression() + ")";
        }
    }

    record Div(Expression left, Expression right) implements Expression {
        @Override
        public String toVarItemExpression() {
            return "%math(" + left.toVarItemExpression() + "/" + right.toVarItemExpression() + ")";
        }
    }

    record Subscript(Expression left, Expression right) implements Expression {
        @Override
        public String toVarItemExpression() {
            var leftText = switch (left) {
                case Variable var -> var.variableName();
                default -> left.toVarItemExpression();
            };
            return switch (right) {
                case Number n -> "%index(" + leftText + "," + right.toVarItemExpression() + ")";
                case StringExpr n -> "%entry(" + leftText + "," + right.toVarItemExpression() + ")";
                case Add a -> "%index(" + leftText + "," + right.toVarItemExpression() + ")";
                case Sub s -> "%index(" + leftText + "," + right.toVarItemExpression() + ")";
                case Mul m -> "%index(" + leftText + "," + right.toVarItemExpression() + ")";
                case Div d -> "%index(" + leftText + "," + right.toVarItemExpression() + ")";
                default -> "!ExpressionError!";
            };
        }
    }
}
