package xyz.chph.toy.bytecodegenerator.statement;

import xyz.chph.toy.bytecodegenerator.expression.ExpressionGenerator;
import xyz.chph.toy.domain.node.expression.Expression;
import xyz.chph.toy.domain.node.statement.IfStatement;
import xyz.chph.toy.domain.node.statement.Statement;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Optional;

public class IfStatementGenerator {
    private final StatementGenerator statementGenerator;
    private final ExpressionGenerator expressionGenerator;
    private final MethodVisitor methodVisitor;
    public IfStatementGenerator(StatementGenerator statementGenerator, ExpressionGenerator expressionGenerator, MethodVisitor methodVisitor) {
        this.statementGenerator = statementGenerator;
        this.expressionGenerator = expressionGenerator;
        this.methodVisitor = methodVisitor;
    }
    public void generate(IfStatement ifStatement) {
        Expression condition = ifStatement.getCondition();
        condition.accept(expressionGenerator);
        Label trueLabel = new Label();
        Label endLabel = new Label();
        methodVisitor.visitJumpInsn(Opcodes.IFNE, trueLabel);
        Optional<Statement> falseStatement = ifStatement.getFalseStatement();
        if (falseStatement.isPresent()) {
            falseStatement.get().accept(statementGenerator);
        }
        methodVisitor.visitJumpInsn(Opcodes.GOTO, endLabel);
        methodVisitor.visitLabel(trueLabel);
        ifStatement.getTrueStatement().accept(statementGenerator);
        methodVisitor.visitLabel(endLabel);
    }
}
