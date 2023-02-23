// generated with ast extension for cup
// version 0.8
// 26/7/2022 12:41:11


package rs.ac.bg.etf.pp1.ast;

public class ArrayDesignator extends IdentExprList {

    private IdentExprListLsquare IdentExprListLsquare;
    private Expr Expr;

    public ArrayDesignator (IdentExprListLsquare IdentExprListLsquare, Expr Expr) {
        this.IdentExprListLsquare=IdentExprListLsquare;
        if(IdentExprListLsquare!=null) IdentExprListLsquare.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public IdentExprListLsquare getIdentExprListLsquare() {
        return IdentExprListLsquare;
    }

    public void setIdentExprListLsquare(IdentExprListLsquare IdentExprListLsquare) {
        this.IdentExprListLsquare=IdentExprListLsquare;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IdentExprListLsquare!=null) IdentExprListLsquare.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IdentExprListLsquare!=null) IdentExprListLsquare.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IdentExprListLsquare!=null) IdentExprListLsquare.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ArrayDesignator(\n");

        if(IdentExprListLsquare!=null)
            buffer.append(IdentExprListLsquare.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ArrayDesignator]");
        return buffer.toString();
    }
}
