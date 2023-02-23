package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
	
	public int nVars(Collection<Obj> o) {
		int n = 0;
		for (Obj k : o) {
			if(!k.getName().equals("main")) {
				n++;
			}
		}
		return n;
	}
	
	public void visit(Program program) {
		//br globalnih simbola
		Code.dataSize = nVars(program.getProgId().obj.getLocalSymbols());
	}
	
	public void visit(SingleStatementPrint print) {
		Struct t = print.getExpr().struct;
		Code.load(new Obj(Obj.Con, "", Tab.intType, print.getN1(), 0));
		if (t == Tab.intType || t.getKind() == Struct.Bool) {
			Code.put(Code.const_5); // sirina ispisa na e-stek, expr je vec na e-steku
			Code.put(Code.print);
			Code.load(new Obj(Obj.Con, "", Tab.intType, print.getN1(), 0));
			Code.put(Code.print);
		} else {
			//Code.put(Code.const_1); // sirina ispisa na e-stek, expr je vec na e-steku
			Code.put(Code.bprint);
		}	
	}
	
	public void visit(SinglePrintNoNumConst print) {
		Struct t = print.getExpr().struct;
		if (t == Tab.intType || t.getKind() == Struct.Bool) {
			Code.loadConst(5); // sirina ispisa na e-stek, expr je vec na e-steku
			Code.put(Code.print);
		} else {
			Code.loadConst(1); // sirina ispisa na e-stek, expr je vec na e-steku
			Code.put(Code.bprint);
		}	
	}
	
	public void visit(MethodDeclStart md) {
		// ako je metoda main
		if(md.getTypeVoid().obj.getName().equals("main")) {
			Code.mainPc = Code.pc;
		}
		Obj o = md.getTypeVoid().obj;
		o.setAdr(Code.pc);
		
		// svaka metoda pocinje enter instrukcijom koja je zaduzena
		// da napravi aktivacioni zapis na steku i da sve parametre sa
		// expr steka iskopira u aktivacioni zapis pozvane metode
		Code.put(Code.enter);
		Code.put(o.getLevel());
		Code.put(nVars(o.getLocalSymbols()));
	}
	
	public void visit(MethodDeclaration md) {
		Code.put(Code.exit); 
		Code.put(Code.return_);
	}
	
	 public void visit(Dess d) {
		Obj o = d.getDesignator().obj;
        Code.load(o);
	}
	
	public void visit(FactorNum fn) {
		Obj o = new Obj(Obj.Con, "", Tab.intType);
		o.setAdr(fn.getN());
		Code.load(o);
	}
	
	public void visit(FactorChar fn) {
		Obj o = new Obj(Obj.Con, "", Tab.charType);
		o.setAdr(fn.getC());
		Code.load(o);
	}
	
	public void visit(FactorBool fb) {
		Obj o;
		if(fb.getB().equals("true")) {
			o = new Obj(Obj.Con, "", new Struct(Struct.Bool),1,0);
		}
		else {
			o = new Obj(Obj.Con, "", new Struct(Struct.Bool),0,0);
		}
		
		Code.load(o);
	}
	
	public void visit(FactorNew operatorNew) {
		Code.put(Code.newarray);
        if ( operatorNew.getType().struct == Tab.charType ) 
			Code.put(0); 
        else 
			Code.put(1);
	}
	
	/*public void visit(SimpleDesignator sd) {
		Code.load(sd.obj);
	}
	*/
	
	public void visit(DesignatorAssignment da) {
		// designator prestavlja glob prom kojoj je moguce dodeliti vr
		// expr - na expr steku se vec nalazi br koji treba dodeliti
		
		//System.out.println("USAO U DESIGNATOR assignment na liniji " + da.getLine()
		//+ "tip objekta je " + da.getDesignator().obj.getKind());
		
		Code.store(da.getDesignator().obj);
	}
	
	public void visit(DesignatorInc di) {
		if(di.getDesignator().obj.getKind() == Obj.Elem) {
			di.getDesignator().traverseBottomUp(this);
		}
		Code.load(di.getDesignator().obj);
		Code.put(Code.const_1);
		Code.put(Code.add);
		Code.store(di.getDesignator().obj);
	}
	
	public void visit(DesignatorDec di) {
		if(di.getDesignator().obj.getKind() == Obj.Elem) {
			di.getDesignator().traverseBottomUp(this);
		}
		Code.load(di.getDesignator().obj);
		Code.put(Code.const_1);
		Code.put(Code.sub);
		Code.store(di.getDesignator().obj);
	}

	public void visit(AddExpr ae) {
		if(Plus.class == ae.getAddop().getClass()) {
			//System.out.println(Code.add + "u liniji" + ae.getLine());
			Code.put(Code.add);
		}
		else if(Minus.class == ae.getAddop().getClass()){
			Code.put(Code.sub);
		}
	}
	
	public void visit(MulopFactor mf) {
		if(Mul.class == mf.getMulop().getClass()) {
			Code.put(Code.mul);
		}
		else if(Div.class == mf.getMulop().getClass()) {
			Code.put(Code.div);
		}
		else if(Mod.class == mf.getMulop().getClass()) {
			Code.put(Code.rem);
		}
	}
	
	public void visit(IdentExprListLsquare iel) {
		//System.out.println("IDENT EXPR LIST LSQUARE na liniji" + iel.getLine());
		Obj o = iel.getIdentExprList().obj;
		Code.load(o);
		//System.out.println(iel.getIdentExprList().obj.getKind());
	}	

	public void visit(SingleStatementRead ssr) {
		int tip = ssr.getDesignator().obj.getType().getKind();
		
		if (tip == Struct.Int || tip == Struct.Bool) {
			Code.put(Code.read);
		} 
		else if (tip == Struct.Char){
			Code.put(Code.bread);
		}	
		
		Code.store(ssr.getDesignator().obj);
	}
	
	public void visit(QuestExpr expr) {
		//primer: 2 ?? 3
		Code.put(Code.dup2); // stek: 2 3 2 3
		
		Code.put(Code.pop); // stek: 2 3 2
		Code.put(Code.const_n+0); // stek: 2 3 2 0
		
		Code.putFalseJump(Code.eq, 0); // uporedjuje 2 i 0 i skace
									// na neku adr ako nisu jednaki, tj. 2!=0
									// stek: 2 3
		int pcJeq = Code.pc-2;
		
		// ako je 2 == 0, treba da bude desni izraz tj. 3
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop);
		
		Code.putJump(0);
		int pcJmp = Code.pc - 2;
		
		// u suprotnom na steku treba da bude levi izraz tj 2
		Code.fixup(pcJeq);
		Code.put(Code.pop); // stek: 2
		
		Code.fixup(pcJmp);
		
		// nastavlja dalje
	}
	
	 public void visit(MinusFactor minusTermExpr) {
	     Code.put(Code.neg);
	 }
	
}
