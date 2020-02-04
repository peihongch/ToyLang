package xyz.chph.toy.bytecodegenerator.expression;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import xyz.chph.toy.domain.node.expression.Expression;
import xyz.chph.toy.domain.node.expression.arthimetic.*;
import xyz.chph.toy.domain.type.BuiltInType;
import xyz.chph.toy.domain.type.Type;

public class ArithmeticExpressionGenerator {
    private final ExpressionGenerator expressionGenerator;
    private final MethodVisitor methodVisitor;

    public ArithmeticExpressionGenerator(ExpressionGenerator expressionGenerator, MethodVisitor methodVisitor) {
        this.expressionGenerator = expressionGenerator;
        this.methodVisitor = methodVisitor;
    }

    public void generate(Addition expression) {
        if (expression.getType().equals(BuiltInType.STRING)) {
            generateStringAppend(expression);
            return;
        }
        evaluateArithmeticComponents(expression);
        Type type = expression.getType();
        methodVisitor.visitInsn(type.getAddOpcode());
    }

    public void generate(Substraction expression) {
        Type type = expression.getType();
        evaluateArithmeticComponents(expression);
        methodVisitor.visitInsn(type.getSubstractOpcode());
    }

    public void generate(Multiplication expression) {
        evaluateArithmeticComponents(expression);
        Type type = expression.getType();
        methodVisitor.visitInsn(type.getMultiplyOpcode());
    }

    public void generate(Division expression) {
        evaluateArithmeticComponents(expression);
        Type type = expression.getType();
        methodVisitor.visitInsn(type.getDivideOpcode());
    }

    private void evaluateArithmeticComponents(ArthimeticExpression expression) {
        Expression leftExpression = expression.getLeftExpression();
        Expression rightExpression = expression.getRightExpression();
        leftExpression.accept(expressionGenerator);
        rightExpression.accept(expressionGenerator);
    }

    private void generateStringAppend(Addition expression) {
        methodVisitor.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
        methodVisitor.visitInsn(Opcodes.DUP);
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        Expression leftExpression = expression.getLeftExpression();
        leftExpression.accept(expressionGenerator);
        String leftExprDescriptor = leftExpression.getType().getDescriptor();
        String descriptor = "(" + leftExprDescriptor + ")Ljava/lang/StringBuilder;";
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", descriptor, false);
        Expression rightExpression = expression.getRightExpression();
        rightExpression.accept(expressionGenerator);
        String rightExprDescriptor = rightExpression.getType().getDescriptor();
        descriptor = "(" + rightExprDescriptor + ")Ljava/lang/StringBuilder;";
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", descriptor, false);
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
    }
}
