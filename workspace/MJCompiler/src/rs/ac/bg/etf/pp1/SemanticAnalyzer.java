package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor {

	boolean greska = false;
	private Struct var_type = Tab.noType;
	private String tipDesignatora;
	int nVars;
	List<String> promenljive = new ArrayList<String>();
	int brPromenljivih = 0;
	int brKonstanti = 0;
	int brNizova = 0;
	int brLokalnihPromenljivih = 0;
	Obj currentMethod = null;
	int vrKonstante = 0;
	
	private Struct method_type=Tab.noType;

	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		greska = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, int kind, String name,
					Struct type, int adr, int level, Collection<Obj> locals) {
		StringBuilder msg = new StringBuilder(message);

		switch (kind) {
		case Obj.Con:
			msg.append("Con ");
			break;
		case Obj.Var:
			msg.append("Var ");
			break;
		case Obj.Type:
			msg.append("Type ");
			break;
		case Obj.Meth:
			msg.append("Meth ");
			break;
		case Obj.Fld:
			msg.append("Fld ");
			break;
		case Obj.Prog:
			msg.append("Prog ");
			break;
		}
		msg.append(name);
		msg.append(": ");
		//Obj objlist = locals;
		switch (type.getKind()) {
		case Struct.None:
			msg.append("notype");
			break;
		case Struct.Int:
			msg.append("int");
			break;
		case Struct.Char:
			msg.append("char");
			break;
		case Struct.Array:
			msg.append("Arr of ");
			switch (type.getElemType().getKind()) {
			case Struct.None:
				msg.append("notype");
				break;
			case Struct.Int:
				msg.append("int");
				break;
			case Struct.Char:
				msg.append("char");
				break;
			case Struct.Array:
				msg.append("Arr of ");
				switch (type.getElemType().getKind()) {
        		case Struct.None: msg.append("notype"); break;
        		case Struct.Int: msg.append("int"); break;
        		case Struct.Char: msg.append("char"); break;
        		case Struct.Array: msg.append("Arr of "); 
        		case Struct.Class: msg.append("Class"); break;
           }

				break;
			}
		}
		msg.append(", ");
		msg.append(adr);
		msg.append(", ");
		msg.append(level + " ");
	
		log.info(msg.toString());

	}

	public void visit(ProgId progId) {
		progId.obj = Tab.insert(Obj.Prog, progId.getId(), Tab.noType);
        Tab.openScope();
	}	
	
	public void visit(Program program) {
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgId().obj);
		Tab.closeScope();
	}

	public void visit(TypeIdent type) {
		//System.out.println("TIPPPPPP");
		Obj novi = Tab.find(type.getTypeName());
		//System.out.println(novi.getKind());
		if (novi.getKind() == Obj.Type) {
			var_type = type.struct = novi.getType();
		}
		else if(type.getTypeName().equals("bool")) {
			var_type = type.struct = new Struct(Struct.Bool);
		}
		else {
			report_error("Greska na " + type.getLine() + ": " + 
						type.getTypeName() + " nije tip", null);
			var_type = type.struct = Tab.noType;
		}

	}

	public void visit(VarParts varDecl) {
		Obj varNode = Tab.insert(Obj.Var, varDecl.getVarName(), var_type);
		if(promenljive.contains(varDecl.getVarName())) {
			 report_error("Greska u liniji " + varDecl.getLine()+
					 ": Ime za promenljivu vec postoji.",null);
		}
		else {
			promenljive.add(varDecl.getVarName());
			if(varNode.getLevel() == 0) {
				brPromenljivih++;
			}
			if(varNode.getLevel() == 1){
				brLokalnihPromenljivih++;
			}
		}
		//Obj obj = Tab.find(varDecl.getVarName());
	}

	public void visit(VarPartArray varDecl) {
		Obj varNode = Tab.insert(Obj.Var, varDecl.getVarName(), new Struct(Struct.Array,var_type));
		if(promenljive.contains(varDecl.getVarName())) {
			 report_error("Greska u liniji " + varDecl.getLine()+
					 ": Ime za promenljivu vec postoji.",null);
		}
		else {
			promenljive.add(varDecl.getVarName());
			if(varNode.getLevel() == 0) {
				brNizova++;
			}
			if(varNode.getLevel() == 1) {
				brLokalnihPromenljivih++;
			}
		}
	}

	public void visit(ConstParts constDecl) {
		Obj varNode = Tab.insert(Obj.Con, constDecl.getConstName(), var_type);
		if(promenljive.contains(constDecl.getConstName())) {
			 report_error("Greska u liniji " + constDecl.getLine()+
					 ": Ime za promenljivu vec postoji.",null);
		}
		else {
			if(constDecl.getRhs().struct != var_type) {
				report_error("Greska u liniji " + constDecl.getLine()+
						 ": tipovi se ne slazu.",null);
			}
			else {
				varNode.setAdr(vrKonstante);
				promenljive.add(constDecl.getConstName());
				brKonstanti++;
			}
		}
	}
	
	public void visit(NumConst fn) {
		vrKonstante = fn.getN();
		fn.struct = Tab.intType;
	}

	public void visit(CharConst cn) {
		vrKonstante = cn.getC();
		cn.struct = Tab.charType;
	}
	
	public void visit(ArrayDesignator designator) {
		tipDesignatora = "array";
		Obj id = designator.getIdentExprListLsquare().obj;
		if (id.getType().getKind() != Struct.Array) {
              report_error("Greska u liniji " +designator.getLine()+
                           ": Ocekivan niz",null);
		}
		else {
			int tip = designator.getExpr().struct.getKind();
			if(tip == Struct.Array && tipDesignatora.equals("array")) {
				tip = designator.getExpr().struct.getElemType().getKind();
			}
			if(tip != Struct.Int) {
				 report_error("Greska u liniji " +designator.getLine()+
	                     ": Tip kod indeksiranja niza mora biti int",null);
			}
			designator.obj = new Obj(Obj.Elem, "", id.getType().getElemType());
		}
	}
	
	public void visit(DesignatorExpr de) {
		de.obj = de.getIdentExprList().obj;
	}
	
	
	public void visit(SimpleDesignator designator) {
		Obj obj = Tab.find(designator.getDesignatorName());
		//System.out.println(obj.getKind());
		if(obj != Tab.noObj) {
			report_info("Pretraga na " + designator.getLine() + "(" + obj.getName() 
						+ "), nadjeno ", obj.getKind(),
						obj.getName(), obj.getType(), 
						obj.getAdr(), obj.getLevel(), obj.getLocalSymbols());
			tipDesignatora = "simple";
		}
		else {
			System.out.println("Greska na " + designator.getLine() + ": " 
					+ designator.getDesignatorName() + " nije deklarisano");
			return;
		}
		designator.obj = obj;
	}
	
	public void visit(AddExpr expr) {
		Struct e = expr.getExpr().struct;
		Struct t = expr.getTerm().struct;
		
		int tipE = e.getKind();
		int tipT = t.getKind();

		if(tipE == Struct.Array) {
			tipE = e.getElemType().getKind();
		}
		if(tipT == Struct.Array) {
			tipT = t.getElemType().getKind();
		}
		//System.out.println("TipE je " + tipE);
		//System.out.println("Tip t je " + tipT);		
		if(tipE != tipT) {
			report_error("Greska na liniji "+ expr.getLine() + 
					" : nekompatibilni tipovi u izrazu za sabiranje.", null);
			expr.struct = Tab.noType;
		}
		else if(tipE != Struct.Int) {
			report_error("Greska na liniji "+ expr.getLine() + 
					" : tipovi u izrazu za sabiranje moraju biti int.", null);
		}
		else {
			expr.struct = Tab.intType;
    	}
	}
	
	
	public void visit(QuestExpr expr) {
		Struct e = expr.getExpr().struct;
		Struct t = expr.getTerm().struct;
		
		int tipE = e.getKind();
		int tipT = t.getKind();
		
		if(tipE == Struct.Array) {
			tipE = e.getElemType().getKind();
		}
		if(tipT == Struct.Array) {
			tipT = t.getElemType().getKind();
		}
		
		if(tipE != tipT) {
			report_error("Greska na liniji "+ expr.getLine() + 
					" : nekompatibilni tipovi u izrazu sa ??.", null);
			expr.struct = Tab.noType;
		}
		else if(tipE != Struct.Int) {
			report_error("Greska na liniji "+ expr.getLine() + 
					" : tipovi u izrazu sa ?? moraju biti int.", null);
		}
		else {
			expr.struct = Tab.intType;
    	}
	}

	public void visit(TermExpr expr) {
		/*if(expr.getTerm().struct.getKind() != Struct.Int) {
			report_error("Greska na liniji "+ expr.getLine() + 
					" : Term mora biti int.", null);
		}*/
		expr.struct = expr.getTerm().struct;
	}
	
	public void visit(MulopFactor mf) {
		Struct t = mf.getTerm().struct;
		Struct f = mf.getFactor().struct;
		
		int tipE = t.getKind();
		int tipT = f.getKind();
	
		if(tipE == Struct.Array) {
			tipE = t.getElemType().getKind();
		}
		if(tipT == Struct.Array) {
			tipT = f.getElemType().getKind();
		}
		
		if(tipE != Struct.Int || tipT != Struct.Int) {
			report_error("Greska na liniji "+ mf.getLine() + 
					" : tipovi u izrazu za mnozenje moraju biti int.", null);
		}
		else {
			mf.struct = Tab.intType;
    	}
	}
	
	public void visit(SimpleFactor sf) {
		sf.struct = sf.getFactor().struct;
	}
	
	public void visit(MinusFactor sf) {
		if(sf.getFactor().struct.getKind() != Struct.Int) {
			report_error("Greska na liniji "+ sf.getLine() + 
					" : za minus mora biti int.", null);
		}
		sf.struct = Tab.intType;
		//System.out.println("Minus je" + sf.getFactor().struct.getKind());
	}
	 
	public void visit(Dess d) {
		d.struct = d.getDesignator().obj.getType();
	}
	
	public void visit(FactorNum fn) {
		fn.struct = Tab.intType;
		//System.out.println("Broj je" + fn.getN());
	}
	
	public void visit(FactorChar fn) {
		fn.struct = Tab.charType;
	}
	
	public void visit(FactorBool fb) {
		fb.struct = new Struct(Struct.Bool);
	}
	
	public void visit(FactorExpr fn) {
		fn.struct = fn.getExpr().struct;
	}
	
	public void visit(FactorNew fn) {
		int tip = fn.getExpr().struct.getKind();
		if(tip == Struct.Array && tipDesignatora.equals("array")) {
			tip = fn.getExpr().struct.getElemType().getKind(); 
		}
		if(tip != Struct.Int) {
			report_error("Greska na liniji "+ fn.getLine() + 
						" : Tip u [] mora biti int.", null);
		}
		fn.struct = fn.getType().struct;
	}
	
	public void visit(DesignatorAssignment da) {
		if(da.getDesignator().obj.getKind() != Obj.Var && da.getDesignator().obj.getKind() != Obj.Elem) {
			report_error("Greska na liniji "+ da.getLine() + 
					" : Designator mora biti promenljiva.", null);
		}
		if(da.getDesignator().obj != null && da.getErrorExpr().struct != null) {
			int tip = da.getDesignator().obj.getType().getKind();
			if(tip == Struct.Array) {
				tip = da.getDesignator().obj.getType().getElemType().getKind();
			}
			if(tip != da.getErrorExpr().struct.getKind()) {
				report_error("Greska na liniji "+ da.getLine() + 
						" : tipovi nisu kompatibilni kod operatora dodele.", null);
			}
		}
		
	}
	
	public void visit(DesignatorInc di) {
		int tip = di.getDesignator().obj.getType().getKind();
		if(di.getDesignator().obj != null) {
			if(tip == Struct.Array && tipDesignatora.equals("array")) {
				tip = di.getDesignator().obj.getType().getElemType().getKind();
			}
			if(tip != Struct.Int) {
				report_error("Greska na liniji "+ di.getLine() + 
						" : tip za inkrement mora biti int.", null);
			}
			
		}
	}
	
	public void visit(DesignatorDec di) {
		int tip = di.getDesignator().obj.getType().getKind();
		if(di.getDesignator().obj != null) {
			if(tip == Struct.Array && tipDesignatora.equals("array")) {
				tip = di.getDesignator().obj.getType().getElemType().getKind();
			}
			if(tip != Struct.Int) {
				report_error("Greska na liniji "+ di.getLine() + 
						" : tip za dekrement mora biti int.", null);
			}
			
		}
	}
	
	public void visit(SingleStatementRead ssr) {
		int tip = ssr.getDesignator().obj.getType().getKind();
		if(ssr.getDesignator().obj != null) {
			if(tip == Struct.Array && tipDesignatora.equals("array")) {
				tip = ssr.getDesignator().obj.getType().getElemType().getKind();
			}
			if(tip != Struct.Int && tip != Struct.Char && tip != Struct.Bool) {
				report_error("Greska na liniji "+ ssr.getLine() + 
						" : tip za read mora biti int, char ili bool.", null);
			}
		}
	}
	
	public void visit(SingleStatementPrint ssr) {
		int tip = ssr.getExpr().struct.getKind();
		if(tip == Struct.Array && tipDesignatora.equals("array")) {
			tip = ssr.getExpr().struct.getElemType().getKind();
		}
		if(tip != Struct.Int && tip != Struct.Char && tip != Struct.Bool) {
			report_error("Greska na liniji "+ ssr.getLine() + 
					" : tip za print mora biti int, char ili bool.", null);
		}
	}
	
	public void visit(SinglePrintNoNumConst ssr) {
		Struct t = ssr.getExpr().struct;
		int tip = t.getKind();
		if(tip == Struct.Array && tipDesignatora.equals("array")) {
			tip = t.getElemType().getKind();
		}
		if(tip != Struct.Int && tip != Struct.Char && tip != Struct.Bool) {
			report_error("Greska na liniji "+ ssr.getLine() + 
					" : tip za print mora biti int, char ili bool.", null);
		}
	}
	
	public boolean passed() {
		return !greska;
	}
	
	public void visit(MethodDeclStart method_dec_start) {
		if (method_dec_start.getTypeVoid().obj.getName().equals("main")) {
			if (method_type!=Tab.noType) 
				report_error("Greska u liniji "+method_dec_start.getLine()+": metod main mora biti void",null);
		}
	}
	
	public void visit(MethodDeclaration methodDecl){
    	Tab.chainLocalSymbols(currentMethod);
    	Tab.closeScope();
    
    	currentMethod = null;
    }
	
	 public void visit(ReturnType methodTypeName){
		 method_type = methodTypeName.getType().struct; 
	    	currentMethod = Tab.insert(Obj.Meth, methodTypeName.getN(), methodTypeName.getType().struct);
	    	methodTypeName.obj = currentMethod;
	    	Tab.openScope();
			System.out.println("Obradjuje se funkcija " + methodTypeName.getN());
	  }
	 
	 public void visit(ReturnVoid methodTypeName){
		 	method_type = Tab.noType; 
	    	currentMethod = Tab.insert(Obj.Meth, methodTypeName.getN(), Tab.noType);
	    	methodTypeName.obj = currentMethod;
	    	Tab.openScope();
			System.out.println("Obradjuje se funkcija " + methodTypeName.getN());
	  }
	
	 public void visit(IdentExprListLsquare iel) {
			iel.obj = iel.getIdentExprList().obj;
	 }
	
	 public void visit(ExprFine ef) {
		 ef.struct = ef.getExpr().struct;
	 }
}
