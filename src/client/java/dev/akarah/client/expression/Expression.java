package dev.akarah.client.expression;

public sealed interface Expression {
    String toVarItemExpression();
    boolean hasComponentChild();
    boolean hasStringChild();

    record Number(double value) implements Expression {
        @Override
        public String toVarItemExpression() {
            return Double.toString(value);
        }

        @Override
        public boolean hasComponentChild() {
            return false;
        }

        @Override
        public boolean hasStringChild() {
            return false;
        }
    }

    record StringExpr(String value) implements Expression {
        @Override
        public String toVarItemExpression() {
            return value;
        }

        @Override
        public boolean hasComponentChild() {
            return false;
        }

        @Override
        public boolean hasStringChild() {
            return true;
        }
    }

    record StyledText(String value) implements Expression {
        @Override
        public String toVarItemExpression() {
            return value;
        }

        @Override
        public boolean hasComponentChild() {
            return true;
        }

        @Override
        public boolean hasStringChild() {
            return false;
        }
    }

    record Variable(String variableName) implements Expression {
        @Override
        public String toVarItemExpression() {
            return "%var(" + variableName + ")";
        }

        @Override
        public boolean hasComponentChild() {
            return false;
        }

        @Override
        public boolean hasStringChild() {
            return false;
        }
    }

    record Add(Expression left, Expression right) implements Expression {
        @Override
        public String toVarItemExpression() {
            if(hasComponentChild() || hasStringChild())
                return left.toVarItemExpression() + right.toVarItemExpression();
            return "%math(" + left.toVarItemExpression() + "+" + right.toVarItemExpression() + ")";
        }

        @Override
        public boolean hasComponentChild() {
            return left.hasComponentChild() || right.hasComponentChild();
        }

        @Override
        public boolean hasStringChild() {
            return left.hasStringChild() || right.hasStringChild();
        }
    }

    record Sub(Expression left, Expression right) implements Expression {
        @Override
        public String toVarItemExpression() {
            return "%math(" + left.toVarItemExpression() + "-" + right.toVarItemExpression() + ")";
        }

        @Override
        public boolean hasComponentChild() {
            return left.hasComponentChild() || right.hasComponentChild();
        }

        @Override
        public boolean hasStringChild() {
            return left.hasStringChild() || right.hasStringChild();
        }
    }

    record Mul(Expression left, Expression right) implements Expression {
        @Override
        public String toVarItemExpression() {
            return "%math(" + left.toVarItemExpression() + "*" + right.toVarItemExpression() + ")";
        }

        @Override
        public boolean hasComponentChild() {
            return left.hasComponentChild() || right.hasComponentChild();
        }

        @Override
        public boolean hasStringChild() {
            return left.hasStringChild() || right.hasStringChild();
        }
    }

    record Div(Expression left, Expression right) implements Expression {
        @Override
        public String toVarItemExpression() {
            return "%math(" + left.toVarItemExpression() + "/" + right.toVarItemExpression() + ")";
        }

        @Override
        public boolean hasComponentChild() {
            return left.hasComponentChild() || right.hasComponentChild();
        }

        @Override
        public boolean hasStringChild() {
            return left.hasStringChild() || right.hasStringChild();
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

        @Override
        public boolean hasComponentChild() {
            return left.hasComponentChild() || right.hasComponentChild();
        }

        @Override
        public boolean hasStringChild() {
            return left.hasStringChild() || right.hasStringChild();
        }
    }
}
