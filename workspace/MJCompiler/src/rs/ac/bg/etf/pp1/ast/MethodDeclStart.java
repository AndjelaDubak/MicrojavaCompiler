// generated with ast extension for cup
// version 0.8
// 26/7/2022 12:41:11


package rs.ac.bg.etf.pp1.ast;

public class MethodDeclStart implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private TypeVoid TypeVoid;
    private FormPars FormPars;
    private LocalVarListDecl LocalVarListDecl;

    public MethodDeclStart (TypeVoid TypeVoid, FormPars FormPars, LocalVarListDecl LocalVarListDecl) {
        this.TypeVoid=TypeVoid;
        if(TypeVoid!=null) TypeVoid.setParent(this);
        this.FormPars=FormPars;
        if(FormPars!=null) FormPars.setParent(this);
        this.LocalVarListDecl=LocalVarListDecl;
        if(LocalVarListDecl!=null) LocalVarListDecl.setParent(this);
    }

    public TypeVoid getTypeVoid() {
        return TypeVoid;
    }

    public void setTypeVoid(TypeVoid TypeVoid) {
        this.TypeVoid=TypeVoid;
    }

    public FormPars getFormPars() {
        return FormPars;
    }

    public void setFormPars(FormPars FormPars) {
        this.FormPars=FormPars;
    }

    public LocalVarListDecl getLocalVarListDecl() {
        return LocalVarListDecl;
    }

    public void setLocalVarListDecl(LocalVarListDecl LocalVarListDecl) {
        this.LocalVarListDecl=LocalVarListDecl;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(TypeVoid!=null) TypeVoid.accept(visitor);
        if(FormPars!=null) FormPars.accept(visitor);
        if(LocalVarListDecl!=null) LocalVarListDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(TypeVoid!=null) TypeVoid.traverseTopDown(visitor);
        if(FormPars!=null) FormPars.traverseTopDown(visitor);
        if(LocalVarListDecl!=null) LocalVarListDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(TypeVoid!=null) TypeVoid.traverseBottomUp(visitor);
        if(FormPars!=null) FormPars.traverseBottomUp(visitor);
        if(LocalVarListDecl!=null) LocalVarListDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDeclStart(\n");

        if(TypeVoid!=null)
            buffer.append(TypeVoid.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormPars!=null)
            buffer.append(FormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(LocalVarListDecl!=null)
            buffer.append(LocalVarListDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDeclStart]");
        return buffer.toString();
    }
}
