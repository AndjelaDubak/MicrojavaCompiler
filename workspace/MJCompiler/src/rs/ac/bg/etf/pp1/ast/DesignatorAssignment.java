// generated with ast extension for cup
// version 0.8
// 26/7/2022 12:41:11


package rs.ac.bg.etf.pp1.ast;

public class DesignatorAssignment extends DesignatorStatement {

    private Designator Designator;
    private ErrorExpr ErrorExpr;

    public DesignatorAssignment (Designator Designator, ErrorExpr ErrorExpr) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.ErrorExpr=ErrorExpr;
        if(ErrorExpr!=null) ErrorExpr.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public ErrorExpr getErrorExpr() {
        return ErrorExpr;
    }

    public void setErrorExpr(ErrorExpr ErrorExpr) {
        this.ErrorExpr=ErrorExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(ErrorExpr!=null) ErrorExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(ErrorExpr!=null) ErrorExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(ErrorExpr!=null) ErrorExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorAssignment(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ErrorExpr!=null)
            buffer.append(ErrorExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorAssignment]");
        return buffer.toString();
    }
}
