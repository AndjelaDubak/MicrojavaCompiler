// generated with ast extension for cup
// version 0.8
// 26/7/2022 12:41:11


package rs.ac.bg.etf.pp1.ast;

public class LocalVarDecls extends LocalVarDecl {

    private Type Type;
    private LocalVarList LocalVarList;

    public LocalVarDecls (Type Type, LocalVarList LocalVarList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.LocalVarList=LocalVarList;
        if(LocalVarList!=null) LocalVarList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public LocalVarList getLocalVarList() {
        return LocalVarList;
    }

    public void setLocalVarList(LocalVarList LocalVarList) {
        this.LocalVarList=LocalVarList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(LocalVarList!=null) LocalVarList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(LocalVarList!=null) LocalVarList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(LocalVarList!=null) LocalVarList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("LocalVarDecls(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(LocalVarList!=null)
            buffer.append(LocalVarList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [LocalVarDecls]");
        return buffer.toString();
    }
}
