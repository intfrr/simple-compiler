/*
 * This class implements a Lexical Analyzer
 * Simple Compiler
 * LexicalAnalyzer Class
 */

package com.borromeo.analyzer;
 
import java.util.*;
import java.lang.*;
import java.io.*;

import com.borromeo.token.Tokens;

public class LexicalAnalyzer {
	
	private StringBuffer sb =  new StringBuffer(32);
	private Vector symbolTable = new Vector();
	private Vector tokenList = new Vector();

    public LexicalAnalyzer() {
    	
    }
    
    public void Scanner(String input, int lineCount){
    	
    	sb.delete(0,sb.length());
    	
    	for(int i=0; i<input.length(); i++){
			
			if(input.charAt(i) == '+' || input.charAt(i) == '-' || input.charAt(i) == '=' || input.charAt(i) == ';' || 
					input.charAt(i) == '(' || input.charAt(i) == ')' || input.charAt(i) == '/' && i == (input.length()-1) || 
					input.charAt(i) == 32 || input.charAt(i) == 9){
				LexemeIdentifier(sb,lineCount);
				LexemeIdentifier(input.trim().charAt(i),lineCount);
				sb.delete(0,sb.length());
			}
			else if(input.charAt(i) == '/' && input.charAt(i+1) == '/')
				break;
			else{
				sb.append(input.charAt(i));
			}
		}//end for
		if(sb.length() != 0){
			LexemeIdentifier(sb,lineCount);
		}
    }//end Scanner
    
	public static boolean convertToInt(String num){
    	int convertedNum = 0;
    	
    	try{
    		convertedNum = Integer.parseInt(num);
    		return true;
    		
    	}catch(NumberFormatException e){
    		return false;
    	}
    }//end convertToInt
    
    public void LexemeIdentifier(StringBuffer lex, int lineCount){
    	boolean check = false;
    	boolean validLexeme = false;
    	check = convertToInt(""+sb);
			if(check){
				//System.out.println("make new token -> lexeme:"+sb+" token: IntLiteral");
				addToTokenList(sb,"IntLiteral",lineCount);
			}		
			else{
				if(sb.length() != 0){
					validLexeme = validateLexeme(sb);
					if(validLexeme){
						identifyLexeme(""+sb,lineCount);	
					}
					else{
						//System.out.println("make new token -> lexeme:"+sb+" token: LEXICAL ERROR");	
						addToTokenList(sb,"LEXICAL ERROR",lineCount);
					}
				}
			}
    }
    
    public void LexemeIdentifier(char lex, int lineCount){
    	
    	switch(lex){
    		case '+': 	//System.out.println("make new token -> lexeme:"+lex+" token: PlusOp");
    					addToTokenList(lex,"PlusOp",lineCount);
    				  	break;
    		case '-': 	//System.out.println("make new token -> lexeme:"+lex+" token: MinusOp");
    				  	addToTokenList(lex,"MinusOp",lineCount);
    				  	break;
    		case '(':	//System.out.println("make new token -> lexeme:"+lex+" token: Lparen");
    				  	addToTokenList(lex,"Lparen",lineCount);
    				  	break;
    		case ')': 	//System.out.println("make new token -> lexeme:"+lex+" token: Rparen");
    				  	addToTokenList(lex,"Rparen",lineCount);
    				  	break;
    		case '=': 	//System.out.println("make new token -> lexeme:"+lex+" token: AssignOp");
    				  	addToTokenList(lex,"AssignOp",lineCount);
    				  	break;
    		case ';': 	//System.out.println("make new token -> lexeme:"+lex+" token: Semicolon");
    				  	addToTokenList(lex,"Semicolon",lineCount);
    				  	break;
    	}
    }//end LexemeAnalyzer for char
    
    public boolean validateLexeme(StringBuffer sb){
    	boolean check = convertToInt(""+sb.charAt(0));
    	if(check){
    		return false;
    	}
    	else if(sb.length() > 32){
    		return false;
    	}
    	else{
    		for(int i=0; i<sb.length(); i++){
    			if((sb.charAt(i) >= 48 && sb.charAt(i) <= 57) || (sb.charAt(i) >= 65 && sb.charAt(i) <= 90) || (sb.charAt(i) >= 97 && sb.charAt(i) <= 122) || sb.charAt(i) == 95)
    				continue;
    			else
    				return false;
    		}
    	}
    		
    	return true;
    }//end validateLexeme    
    
    public void identifyLexeme(String lexeme, int lineCount){
    
    	if(lexeme.toUpperCase().equals("BEGIN")){
    		//System.out.println("make new token -> lexeme:"+lexeme+" token: BeginSym");
    		addToTokenList(lexeme,"BeginSym",lineCount);
    	}
    	else if(lexeme.toUpperCase().equals("END")){
    		//System.out.println("make new token -> lexeme:"+lexeme+" token: EndSym");
    		addToTokenList(lexeme,"EndSym",lineCount);
    	}
    	else{
    		//System.out.println("make new token -> lexeme:"+lexeme+" token: Id");
    		boolean check = checkOccurence(lexeme);
    		
    		if(!check)
    			addToSymbolTable(lexeme,"Id");
    		
    		addToTokenList(lexeme,"Id",lineCount);
    	}
    	
    }//end identifyLexeme
    
    public void addToTokenList(StringBuffer lexeme, String token, int lineCount){
    	
    	Tokens newToken = new Tokens(lexeme, token, lineCount);
    	tokenList.add(newToken);
    	
    }//end addToTokenList() - String Buffer
    
     public void addToTokenList(String lexeme, String token, int lineCount){
     
     	Tokens newToken = new Tokens(lexeme, token, lineCount);
     	tokenList.add(newToken);
    	
    }//end addToTokenList() - String
    
     public void addToTokenList(char lexeme, String token, int lineCount){
     	
     	Tokens newToken = new Tokens(lexeme, token, lineCount);
     	tokenList.add(newToken);
    	
    }//end addToTokenList() - char
    
    public void addToSymbolTable(String lexeme, String token){
    	
    	Tokens newToken = new Tokens(lexeme, token);
     	symbolTable.add(newToken);
     	
    }//end addToSymbolTable
    
    public boolean checkOccurence(String lexeme){
    	Tokens lex = null;
    	for(int i=0; i<symbolTable.size(); i++){
    		lex = (Tokens)symbolTable.elementAt(i);
    		
    		if(lexeme.toUpperCase().equals(lex.getLexeme().toUpperCase())){
    			return true;
    		}
    	}
    	return false;
    }//end checkOccurence
    
    public void displayTokens(){
    	int spacesBetween = 0;
    	Tokens tok = null;
    	String tokenLength = "";
    	
    	System.out.println("Tokens              Lexeme");
    	for(int i=0; i<tokenList.size(); i++){
    		tok = (Tokens)tokenList.elementAt(i);
    		tokenLength = tok.getToken();
    		System.out.print(tokenLength);
    		if(tokenLength.length() < 20)
    			spacesBetween = 20 - tokenLength.length();
    		
    		for(int x=0; x<spacesBetween; x++){
    			System.out.print(" ");
    		}
    		System.out.println(tok.getLexeme());
    	}
    	System.out.println("EofSym              ");
    }//end displayTokens
    
    public void writeToFile() throws IOException{
    	int spacesBetween = 0;
    	Tokens tok = null;
    	String tokenLength = "";
    	BufferedWriter stdout = new BufferedWriter(new FileWriter("Input.tkn"));
    	
    	stdout.write("Tokens              Lexeme");
    	stdout.newLine();
    	for(int i=0; i<tokenList.size(); i++){
    		tok = (Tokens)tokenList.elementAt(i);
    		tokenLength = tok.getToken();
    		stdout.write(tok.getToken());
    		if(tokenLength.length() < 20)
    			spacesBetween = 20 - tokenLength.length();
    		
    		for(int x=0; x<spacesBetween; x++){
    			stdout.write(" ");
    		}
    		stdout.write(tok.getLexeme());
    		stdout.newLine();
    	}
    	stdout.write("EofSym              ");
    	stdout.close();
    }//end writeToFile
    
    /*********************************************** ADDTIONAL CODE FOR SYNTAX ANALYZER ***********************************************/
    
    public Vector getTokenList(){
    	Vector allTokens = new Vector();
    	Tokens tok = null; 
    	for(int i=0; i<tokenList.size(); i++){
    		tok = (Tokens)tokenList.elementAt(i);
    		allTokens.add(tok.getToken());
    	}
    	return allTokens;
    }//end getTokenList
    
    public Vector getTokens(){
    	return tokenList;
    }
    
}//end Lexical Analyzer