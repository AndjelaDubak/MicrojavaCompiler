// generated with ast extension for cup
// version 0.8
// 26/7/2022 12:41:11


package rs.ac.bg.etf.pp1.ast;

public class LocalVarListsDecl extends LocalVarListDecl {

    private LocalVarListDecl LocalVarListDecl;
    private LocalVarDecl LocalVarDecl;

    public LocalVarListsDecl (LocalVarListDecl LocalVarListDecl, LocalVarDecl LocalVarDecl) {
        this.LocalVarListDecl=LocalVarListDecl;
        if(LocalVarListDecl!=null) LocalVarListDecl.setParent(this);
        this.LocalVarDecl=LocalVarDecl;
        if(LocalVarDecl!=null) LocalVarDecl.setParent(this);
    }

    public LocalVarListDecl getLocalVarListDecl() {
        return LocalVarListDecl;
    }

    public void setLocalVarListDecl(LocalVarListDecl LocalVarListDecl) {
        this.LocalVarListDecl=LocalVarListDecl;
    }

    public LocalVarDecl getLocalVarDecl() {
        return LocalVarDecl;
    }

    public void setLocalVarDecl(LocalVarDecl LocalVarDecl) {
        this.LocalVarDecl=LocalVarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(LocalVarListDecl!=null) LocalVarListDecl.accept(visitor);
        if(LocalVarDecl!=null) LocalVarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LocalVarListDecl!=null) LocalVarListDecl.traverseTopDown(visitor);
        if(LocalVarDecl!=null) LocalVarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LocalVarListDecl!=null) LocalVarListDecl.traverseBottomUp(visitor);
        if(LocalVarDecl!=null) LocalVarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("LocalVarListsDecl(\n");

        if(LocalVarListDecl!=null)
            buffer.append(LocalVarListDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(LocalVarDecl!=null)
            buffer.append(LocalVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [LocalVarListsDecl]");
        return buffer.toString();
    }
}
