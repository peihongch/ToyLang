package xyz.chph.toy.exception;

import xyz.chph.toy.domain.node.statement.Statement;

public class LastStatementNotReturnableException extends RuntimeException {
    public LastStatementNotReturnableException(Statement lastStatement) {
        super("The statement " + lastStatement + " is a last statement in a functon, but it is not an expression!");
    }
}
