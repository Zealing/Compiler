/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1024;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    // Count nested comments
    private int comment_level = 0;

    private int curr_lineno = 1;
    int get_curr_lineno() {
        return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
        filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
        return filename;
    }

    Symbol maxLengthExceeded() {
      if (string_buf.length() >= MAX_STR_CONST) {
        yybegin(STRING_ERROR);
        return new Symbol(TokenConstants.ERROR, "String constant too long");
      } else {
        return null;
      }
    }
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
    case YYINITIAL:
        /* nothing special to do in the initial state */
        break;
    case BLOCK_COMMENT:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in Comment");
    case STRING:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in String");
    case STRING_ERROR:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in String");
    case STRING_BACKSLASH:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in String");
        /* If necessary, add code for other states here, e.g:
           case COMMENT:
           ...
           break;
        */
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup

%state SINGLE_COMMENT
%state BLOCK_COMMENT
%state STRING
%state STRING_ERROR
%state STRING_BACKSLASH

CHARACTERS = [^\0]
WHITESPACE = [ \n\f\r\t\x0B]

A = [aA]
B = [bB]
C = [cC]
D = [dD]
E = [eE]
F = [fF]
G = [gG]
H = [hH]
I = [iI]
J = [jJ]
K = [kK]
L = [lL]
M = [mM]
N = [nN]
O = [oO]
P = [pP]
Q = [qQ]
R = [rR]
S = [sS]
T = [tT]
U = [uU]
V = [vV]
W = [wW]
X = [xX]
Y = [yY]
Z = [zZ]
%%

<YYINITIAL>{C}{L}{A}{S}{S} { return new Symbol(TokenConstants.CLASS); }
<YYINITIAL>{E}{L}{S}{E} { return new Symbol(TokenConstants.ELSE); }
<YYINITIAL>f{A}{L}{S}{E} { return new Symbol(TokenConstants.BOOL_CONST, false); }
<YYINITIAL>{F}{I} { return new Symbol(TokenConstants.FI); }
<YYINITIAL>{I}{F} { return new Symbol(TokenConstants.IF); }
<YYINITIAL>{I}{N} { return new Symbol(TokenConstants.IN); }
<YYINITIAL>{I}{N}{H}{E}{R}{I}{T}{S} { return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL>{I}{S}{V}{O}{I}{D} { return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL>{L}{E}{T} { return new Symbol(TokenConstants.LET); }
<YYINITIAL>{L}{O}{O}{P} { return new Symbol(TokenConstants.LOOP); }
<YYINITIAL>{P}{O}{O}{L} { return new Symbol(TokenConstants.POOL); }
<YYINITIAL>{T}{H}{E}{N} { return new Symbol(TokenConstants.THEN); }
<YYINITIAL>{W}{H}{I}{L}{E} { return new Symbol(TokenConstants.WHILE); }
<YYINITIAL>{C}{A}{S}{E} { return new Symbol(TokenConstants.CASE); }
<YYINITIAL>{E}{S}{A}{C} { return new Symbol(TokenConstants.ESAC); }
<YYINITIAL>{N}{E}{W} { return new Symbol(TokenConstants.NEW); }
<YYINITIAL>{O}{F} { return new Symbol(TokenConstants.OF); }
<YYINITIAL>{N}{O}{T} { return new Symbol(TokenConstants.NOT); }
<YYINITIAL>t{R}{U}{E} { return new Symbol(TokenConstants.BOOL_CONST, true); }

<YYINITIAL>"<-" { return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL>";" { return new Symbol(TokenConstants.SEMI); }
<YYINITIAL>":" { return new Symbol(TokenConstants.COLON); }
<YYINITIAL>"," { return new Symbol(TokenConstants.COMMA); }
<YYINITIAL>"." { return new Symbol(TokenConstants.DOT); }
<YYINITIAL>"=>" { return new Symbol(TokenConstants.DARROW); }
<YYINITIAL>"+" { return new Symbol(TokenConstants.PLUS); }
<YYINITIAL>"-" { return new Symbol(TokenConstants.MINUS); }
<YYINITIAL>"*" { return new Symbol(TokenConstants.MULT); }
<YYINITIAL>"/" { return new Symbol(TokenConstants.DIV); }
<YYINITIAL>"~" { return new Symbol(TokenConstants.NEG); }
<YYINITIAL>"<" { return new Symbol(TokenConstants.LT); }
<YYINITIAL>"<=" { return new Symbol(TokenConstants.LE); }
<YYINITIAL>"=" { return new Symbol(TokenConstants.EQ); }
<YYINITIAL>"(" { return new Symbol(TokenConstants.LPAREN); }
<YYINITIAL>")" { return new Symbol(TokenConstants.RPAREN); }
<YYINITIAL>"{" { return new Symbol(TokenConstants.LBRACE); }
<YYINITIAL>"}" { return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL>"@" { return new Symbol(TokenConstants.AT); }

<YYINITIAL>[0-9]+ { return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext())); }
<YYINITIAL>[A-Z][_0-9a-zA-Z]* { return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
<YYINITIAL>[a-z][_0-9a-zA-Z]* { return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }

<YYINITIAL>"\"" { string_buf.setLength(0); yybegin(STRING); }

<STRING>\" { yybegin(YYINITIAL); return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(string_buf.toString())); }
<STRING>"\b" { if(maxLengthExceeded() != null) { return maxLengthExceeded(); } string_buf.append('\b'); }
<STRING>"\t" { if(maxLengthExceeded() != null) { return maxLengthExceeded(); } string_buf.append('\t'); }
<STRING>"\n"|\\\n { if(maxLengthExceeded() != null) { return maxLengthExceeded(); } string_buf.append('\n'); }
<STRING>"\f" { if(maxLengthExceeded() != null) { return maxLengthExceeded(); } string_buf.append('\f'); }
<STRING>\\ { if(maxLengthExceeded() != null) { return maxLengthExceeded(); } yybegin(STRING_BACKSLASH); }
<STRING>\n { yybegin(YYINITIAL); return new Symbol(TokenConstants.ERROR, "Unterminated string constant"); }
<STRING>\0 { yybegin(STRING_ERROR); return new Symbol(TokenConstants.ERROR, "String contains null character"); }
<STRING>{CHARACTERS} { if(maxLengthExceeded() != null) { return maxLengthExceeded(); } string_buf.append(yytext()); }

<STRING_BACKSLASH>\0 { yybegin(STRING_ERROR); return new Symbol(TokenConstants.ERROR, "String contains null character"); }
<STRING_BACKSLASH>{CHARACTERS} { yybegin(STRING); string_buf.append(yytext()); }

<STRING_ERROR>\n|\" { yybegin(YYINITIAL); }
<STRING_ERROR>{CHARACTERS} { }

<YYINITIAL>-- { yybegin(SINGLE_COMMENT); }
<YYINITIAL,BLOCK_COMMENT>"(*" { ++comment_level; yybegin(BLOCK_COMMENT); }

<SINGLE_COMMENT>\n { ++curr_lineno; yybegin(YYINITIAL); }
<BLOCK_COMMENT>"*)" { if(--comment_level == 0) { yybegin(YYINITIAL); } }

<SINGLE_COMMENT,BLOCK_COMMENT>. { }

<YYINITIAL>"*)" { return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }

\n                              { ++curr_lineno; }
{WHITESPACE}                    { }
.                               { /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  return new Symbol(TokenConstants.ERROR, yytext()); }
