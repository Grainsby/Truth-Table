import java.util.Scanner;
import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/*
Graham Rainsby

Sample Output:
enter the logical statements in this format: (['(A and C) or not D','(B or C) and (D or A)','(not C and B) or (not D and not B)']):
(["(A and C) or not D","(B or C) and (D or A)","(not C and B) or (not D and not B)","A and B"])
A C D B 
--------
T F F T 


*/

public class ProgrammingOne {
	public static void main(String[] args) throws ScriptException {
		 Scanner scan = new Scanner(System.in);
		 ScriptEngineManager manager = new ScriptEngineManager ();
	     ScriptEngine engine = manager.getEngineByName ("js");
	     System.out.println("enter the logical statements in this format: (['(A and C) or not D','(B or C) and (D or A)','(not C and B) or (not D and not B)']):");
		 String inputValues = scan.nextLine();//input must not have spaces after commas.
		 inputValues = inputValues.substring(2, inputValues.length()-2);//gets rid of brackets and parenthesis.
		 String[] input = inputValues.split(",");
		 
		 // (A and C) or not D, (B or C) and (D or A) , (not C and B) or (not D and not B)
		 
		 for (int i = 0; i<input.length; i++) {
			 input[i] = input[i].substring(1, input[i].length()-1);//remove the quotes
		 }
		 Object[] realValues = Deduce(input);
		 String[] script = (String[]) realValues[0];
		 ArrayList<Character> variables= (ArrayList<Character>) realValues[1];
		 engine.put("variables", variables);
		 String[] forLoop = new String[variables.size()];
		 String command = "";
		 String endBrackets = "";
		 for (int i = 0; i<variables.size(); i++) {
			 engine.put(variables.get(i).toString(), boolean.class);
			 forLoop[i] = "for ( " +variables.get(i).toString().toLowerCase() +" = 0;"
			 		+ variables.get(i).toString().toLowerCase() + "<2;"
			 		+variables.get(i).toString().toLowerCase() + "++) {\n" + 
					 variables.get(i).toString() + "= false;\n" + 
			 		"if (" +variables.get(i).toString().toLowerCase() + " == 1) {\n" + 
			 		variables.get(i).toString()+ "= true;\n}";
			 command = command + forLoop[i];
			 endBrackets = endBrackets +"}";//stores the correct amount of endbrackets
			 }
		 
		 String ifStatment = "";
		 String endBracketsIf = "";
		 engine.put("numOfVariables", variables.size());
		 String[] valuesThatWork = new String[(int) Math.pow(2, variables.size())];//total possible values
		 engine.put("valuesThatWork", new String[(int) Math.pow(2, variables.size())]);
		 for (int i = 0; i<script.length; i++) {
			 ifStatment = ifStatment + ("if (" + script[i]+ ") {");
			 endBracketsIf = endBracketsIf +"}";//stores the correct amount of endbrackets for the if statments 
		 }
		 engine.put("m", 0);
		 String finalLine = "";
		 for (int i = 0; i<variables.size(); i++)  {
			 finalLine = finalLine + variables.get(i) + ".toString() + ' ' +";
		 }
		 finalLine = finalLine.substring(0, finalLine.length()-1);
		 
		 engine.eval(command +ifStatment // full test
			 		+ "valuesThatWork[m] = " + finalLine + ";"
			 		+ "m++;"
					 +endBrackets+endBracketsIf);
		 String[] workingValues = (String[]) engine.get("valuesThatWork");
		 
		 //return the final answer key
		 String dashes = "";
		 for (int i = 0; i<variables.size(); i++) {
			 System.out.print(variables.get(i) + " ");
			 dashes = dashes + "--";
		 }
		 System.out.println("\n" +dashes);
		 int a = 0;
		 while (workingValues[a] != null) {
			 String[] eachVariable = workingValues[a].split(" ");
			 for (int j = 0; j<eachVariable.length; j++) {
				 System.out.print(eachVariable[j].toUpperCase().charAt(0) + " ");
			 }
			 System.out.println();
			 a++;
		 }
		 scan.close();
	}//script engine 
	
	public static Object[] Deduce(String[] a) {
		String[][] predicates = new String[a.length][];
		String[] syntax = new String[a.length];//converted correct syntax stored here
		ArrayList<Character> variables= new ArrayList<Character>();
		for (int k = 0; k <a.length;k++ ) {
			syntax[k] = "";
			predicates[k] = a[k].split(" ");
			for(int i = 0; i<predicates[k].length; i++) {
				String parenthesis = "";//parenthesis 
				if (!Character.isLetter(predicates[k][i].charAt(0))) {//only need to account for the front parenthesis
					parenthesis = "(";
					predicates[k][i] = predicates[k][i].substring(1, predicates[k][i].length());
				}
				if (predicates[k][i].equals("not")) {
					syntax[k] = syntax[k] + parenthesis +"!";
				}
				if (predicates[k][i].equals("and")) {
					syntax[k] = syntax[k] +parenthesis +"&&";
				}
				if (predicates[k][i].equals("or")) {
					syntax[k] = syntax[k] +parenthesis +"||";
				}
				if (Character.isUpperCase(predicates[k][i].charAt(0))) {//gets the variables
					syntax[k] = syntax[k] +parenthesis +predicates[k][i];
					if (similarFrequencies(variables,predicates[k][i].charAt(0))) {
						variables.add(predicates[k][i].charAt(0));
					}
					
				}
				
			}
		} //180320079
		//740560
		return new Object[] {syntax,variables};

	}
	public static boolean similarFrequencies(ArrayList<Character> a, char b) {
		//checks to see if the letter that is about to be inputed into the array is not already in the array. returns true if it is not already in the array.
		boolean dup = true;
		for (int i = 0; i<a.size(); i++) {
				if (a.get(i) == b) {
					dup = false;
				}
			}
		return dup;
	}
}
