// generated with ast extension for cup
// version 0.8
// 26/7/2022 12:41:11


package rs.ac.bg.etf.pp1.ast;

public class LocalVarLists extends LocalVarList {

    private LocalVarList LocalVarList;
    private VarPart VarPart;

    public LocalVarLists (LocalVarList LocalVarList, VarPart VarPart) {
        this.LocalVarList=LocalVarList;
        if(LocalVarList!=null) LocalVarList.setParent(this);
        this.VarPart=VarPart;
        if(VarPart!=null) VarPart.setParent(this);
    }

    public LocalVarList getLocalVarList() {
        return LocalVarList;
    }

    public void setLocalVarList(LocalVarList LocalVarList) {
        this.LocalVarList=LocalVarList;
    }

    public VarPart getVarPart() {
        return VarPart;
    }

    public void setVarPart(VarPart VarPart) {
        this.VarPart=VarPart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(LocalVarList!=null) LocalVarList.accept(visitor);
        if(VarPart!=null) VarPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LocalVarList!=null) LocalVarList.traverseTopDown(visitor);
        if(VarPart!=null) VarPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LocalVarList!=null) LocalVarList.traverseBottomUp(visitor);
        if(VarPart!=null) VarPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("LocalVarLists(\n");

        if(LocalVarList!=null)
            buffer.append(LocalVarList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarPart!=null)
            buffer.append(VarPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [LocalVarLists]");
        return buffer.toString();
    }
}
