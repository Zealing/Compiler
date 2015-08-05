/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING = 3;
	private final int STRING_ERROR = 4;
	private final int BLOCK_COMMENT = 2;
	private final int SINGLE_COMMENT = 1;
	private final int YYINITIAL = 0;
	private final int STRING_BACKSLASH = 5;
	private final int yy_state_dtrans[] = {
		0,
		66,
		87,
		91,
		94,
		97
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NOT_ACCEPT,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NOT_ACCEPT,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NOT_ACCEPT,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NOT_ACCEPT,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NOT_ACCEPT,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"60,62:8,63,59,63:2,61,62:18,63,62,56,62:5,32,33,29,28,24,21,25,30,37:10,23," +
"22,20,26,27,62,36,38,39,40,41,42,7,39,43,44,39:2,45,39,46,47,48,39,49,50,12" +
",51,52,53,39:3,62,57,62:2,54,62,3,58,1,15,5,6,55,10,8,55:2,2,55,9,14,16,55," +
"11,4,18,19,13,17,55:3,34,62,35,31,62,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,175,
"0,1,2,3,4,5,1:4,6,1:2,7,1:2,8,1:4,9,1:3,10:2,11,10,1:6,10:7,12,10:7,1:5,13," +
"1:10,14,15,16,12:2,17,12:8,10,12:5,18,19,20,21,8,22,23,24,25,26,27,28,29,30" +
",31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55" +
",56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80" +
",81,82,83,84,85,86,87,88,89,90,91,92,93,94,10,12,95,96,97,98,99,100,101,102" +
",103")[0];

	private int yy_nxt[][] = unpackFromString(104,64,
"1,2,124,164:2,166,67,3,88,126,164:2,68,164,92,164,168,170,172,164,4,5,6,7,8" +
",9,10,11,12,13,14,15,16,17,18,19,20,21,165:2,167,165,169,165,89,125,127,93," +
"171,165:4,173,11,164,22,11,164,23,11,24,11,24,-1:65,164,174,128,164:16,-1:1" +
"7,164,128,164:6,174,164:10,-1:2,164,-1:6,165:7,69,165:11,-1:17,165:7,69,165" +
":11,-1:2,165,-1:26,29,-1:4,30,-1:58,31,-1:69,32,-1:69,33,-1:59,34,-1:71,21," +
"-1:27,164:19,-1:17,164:19,-1:2,164,-1:6,164:9,154,164:9,-1:17,164:6,154,164" +
":12,-1:2,164,-1:6,165:19,-1:17,165:19,-1:2,165,-1:11,58,-1:2,59,-1:8,60,-1:" +
"39,61,59,-1:4,1,50:58,51,50,24,50:2,-1,164:2,136,164:4,25,164:11,-1:17,164," +
"136,164:5,25,164:11,-1:2,164,-1:6,165:9,129,165:9,-1:17,165:6,129,165:12,-1" +
":2,165,-1:6,165:9,151,165:9,-1:17,165:6,151,165:12,-1:2,165,-1:38,52,-1:30," +
"1,50:28,86,50:2,90,50:26,23,50,24,50:2,-1,164:3,138,164,26:2,164,27,164:10," +
"-1:17,164:9,27,164:3,138,164:5,-1:2,164,-1:6,165:3,139,165,70:2,165,71,165:" +
"10,-1:17,165:9,71,165:3,139,165:5,-1:2,165,-1:5,1,53:55,54,55,53,56,57,53:3" +
",-1,164:5,28:2,164:12,-1:17,164:19,-1:2,164,-1:6,165:5,72:2,165:12,-1:17,16" +
"5:19,-1:2,165,-1:5,1,62:55,63,62:2,63,11,62:3,-1,164:11,35,164:5,35,164,-1:" +
"17,164:19,-1:2,164,-1:6,165:11,73,165:5,73,165,-1:17,165:19,-1:2,165,-1:5,1" +
",64:59,65,64:3,-1,164:16,36,164:2,-1:17,164:16,36,164:2,-1:2,164,-1:6,165:1" +
"6,74,165:2,-1:17,165:16,74,165:2,-1:2,165,-1:6,164:11,37,164:5,37,164,-1:17" +
",164:19,-1:2,164,-1:6,165:11,75,165:5,75,165,-1:17,165:19,-1:2,165,-1:6,164" +
":4,38,164:14,-1:17,164:5,38,164:13,-1:2,164,-1:6,165:8,42,165:10,-1:17,165:" +
"9,42,165:9,-1:2,165,-1:6,164:15,39,164:3,-1:17,164:11,39,164:7,-1:2,164,-1:" +
"6,165:4,76,165:14,-1:17,165:5,76,165:13,-1:2,165,-1:6,164:4,40,164:14,-1:17" +
",164:5,40,164:13,-1:2,164,-1:6,165:4,78,165:14,-1:17,165:5,78,165:13,-1:2,1" +
"65,-1:6,41,164:18,-1:17,164:3,41,164:15,-1:2,164,-1:6,79,165:18,-1:17,165:3" +
",79,165:15,-1:2,165,-1:6,164,43,164:17,-1:17,164:8,43,164:10,-1:2,164,-1:6," +
"165:15,77,165:3,-1:17,165:11,77,165:7,-1:2,165,-1:6,164:8,80,164:10,-1:17,1" +
"64:9,80,164:9,-1:2,164,-1:6,165,81,165:17,-1:17,165:8,81,165:10,-1:2,165,-1" +
":6,164:4,44,164:14,-1:17,164:5,44,164:13,-1:2,164,-1:6,165:3,82,165:15,-1:1" +
"7,165:13,82,165:5,-1:2,165,-1:6,164:3,45,164:15,-1:17,164:13,45,164:5,-1:2," +
"164,-1:6,165:4,83,165:14,-1:17,165:5,83,165:13,-1:2,165,-1:6,164:4,46,164:1" +
"4,-1:17,164:5,46,164:13,-1:2,164,-1:6,165:14,84,165:4,-1:17,165:4,84,165:14" +
",-1:2,165,-1:6,164:4,47,164:14,-1:17,164:5,47,164:13,-1:2,164,-1:6,165:3,85" +
",165:15,-1:17,165:13,85,165:5,-1:2,165,-1:6,164:14,48,164:4,-1:17,164:4,48," +
"164:14,-1:2,164,-1:6,164:3,49,164:15,-1:17,164:13,49,164:5,-1:2,164,-1:6,16" +
"4:4,95,164:8,130,164:5,-1:17,164:5,95,164:4,130,164:8,-1:2,164,-1:6,165:4,9" +
"6,165:8,141,165:5,-1:17,165:5,96,165:4,141,165:8,-1:2,165,-1:6,164:4,98,164" +
":8,100,164:5,-1:17,164:5,98,164:4,100,164:8,-1:2,164,-1:6,165:4,99,165:8,10" +
"1,165:5,-1:17,165:5,99,165:4,101,165:8,-1:2,165,-1:6,164:3,102,164:15,-1:17" +
",164:13,102,164:5,-1:2,164,-1:6,165:4,103,165:14,-1:17,165:5,103,165:13,-1:" +
"2,165,-1:6,164:13,104,164:5,-1:17,164:10,104,164:8,-1:2,164,-1:6,165:2,147," +
"165:16,-1:17,165,147,165:17,-1:2,165,-1:6,164:3,106,164:15,-1:17,164:13,106" +
",164:5,-1:2,164,-1:6,165:3,105,165:15,-1:17,165:13,105,165:5,-1:2,165,-1:6," +
"164:2,108,164:16,-1:17,164,108,164:17,-1:2,164,-1:6,165:3,107,165:15,-1:17," +
"165:13,107,165:5,-1:2,165,-1:6,164,150,164:17,-1:17,164:8,150,164:10,-1:2,1" +
"64,-1:6,165:2,109,165:16,-1:17,165,109,165:17,-1:2,165,-1:6,164:12,152,164:" +
"6,-1:17,164:15,152,164:3,-1:2,164,-1:6,165:12,149,165:6,-1:17,165:15,149,16" +
"5:3,-1:2,165,-1:6,164:13,110,164:5,-1:17,164:10,110,164:8,-1:2,164,-1:6,165" +
":13,111,165:5,-1:17,165:10,111,165:8,-1:2,165,-1:6,164:7,156,164:11,-1:17,1" +
"64:7,156,164:11,-1:2,164,-1:6,165:13,113,165:5,-1:17,165:10,113,165:8,-1:2," +
"165,-1:6,164:4,112,164:14,-1:17,164:5,112,164:13,-1:2,164,-1:6,165:7,153,16" +
"5:11,-1:17,165:7,153,165:11,-1:2,165,-1:6,164:18,114,-1:17,164:14,114,164:4" +
",-1:2,164,-1:6,165:3,115,165:15,-1:17,165:13,115,165:5,-1:2,165,-1:6,164:3," +
"116,164:15,-1:17,164:13,116,164:5,-1:2,164,-1:6,165:13,155,165:5,-1:17,165:" +
"10,155,165:8,-1:2,165,-1:6,164:3,118,164:15,-1:17,164:13,118,164:5,-1:2,164" +
",-1:6,165:4,157,165:14,-1:17,165:5,157,165:13,-1:2,165,-1:6,164:13,158,164:" +
"5,-1:17,164:10,158,164:8,-1:2,164,-1:6,165,117,165:17,-1:17,165:8,117,165:1" +
"0,-1:2,165,-1:6,164:4,160,164:14,-1:17,164:5,160,164:13,-1:2,164,-1:6,165:7" +
",119,165:11,-1:17,165:7,119,165:11,-1:2,165,-1:6,164,120,164:17,-1:17,164:8" +
",120,164:10,-1:2,164,-1:6,165:10,159,165:8,-1:17,165:12,159,165:6,-1:2,165," +
"-1:6,164:7,122,164:11,-1:17,164:7,122,164:11,-1:2,164,-1:6,165:7,161,165:11" +
",-1:17,165:7,161,165:11,-1:2,165,-1:6,164:10,162,164:8,-1:17,164:12,162,164" +
":6,-1:2,164,-1:6,165:11,121,165:5,121,165,-1:17,165:19,-1:2,165,-1:6,164:7," +
"163,164:11,-1:17,164:7,163,164:11,-1:2,164,-1:6,164:11,123,164:5,123,164,-1" +
":17,164:19,-1:2,164,-1:6,164,132,164,134,164:15,-1:17,164:8,132,164:4,134,1" +
"64:5,-1:2,164,-1:6,165,131,133,165:16,-1:17,165,133,165:6,131,165:10,-1:2,1" +
"65,-1:6,164:13,140,164:5,-1:17,164:10,140,164:8,-1:2,164,-1:6,165,135,165,1" +
"37,165:15,-1:17,165:8,135,165:4,137,165:5,-1:2,165,-1:6,164:9,142,164:9,-1:" +
"17,164:6,142,164:12,-1:2,164,-1:6,165:13,143,165:5,-1:17,165:10,143,165:8,-" +
"1:2,165,-1:6,164:9,144,146,164:8,-1:17,164:6,144,164:5,146,164:6,-1:2,164,-" +
"1:6,165:9,145,165:9,-1:17,165:6,145,165:12,-1:2,165,-1:6,164:2,148,164:16,-" +
"1:17,164,148,164:17,-1:2,164,-1:5");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

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
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -3:
						break;
					case 3:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -4:
						break;
					case 4:
						{ return new Symbol(TokenConstants.LT); }
					case -5:
						break;
					case 5:
						{ return new Symbol(TokenConstants.MINUS); }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.SEMI); }
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.COLON); }
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.COMMA); }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.DOT); }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.EQ); }
					case -11:
						break;
					case 11:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  return new Symbol(TokenConstants.ERROR, yytext()); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.PLUS); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.MULT); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.DIV); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.NEG); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -19:
						break;
					case 19:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -20:
						break;
					case 20:
						{ return new Symbol(TokenConstants.AT); }
					case -21:
						break;
					case 21:
						{ return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext())); }
					case -22:
						break;
					case 22:
						{ string_buf.setLength(0); yybegin(STRING); }
					case -23:
						break;
					case 23:
						{ ++curr_lineno; }
					case -24:
						break;
					case 24:
						{ }
					case -25:
						break;
					case 25:
						{ return new Symbol(TokenConstants.FI); }
					case -26:
						break;
					case 26:
						{ return new Symbol(TokenConstants.IF); }
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.IN); }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.OF); }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -30:
						break;
					case 30:
						{ return new Symbol(TokenConstants.LE); }
					case -31:
						break;
					case 31:
						{ yybegin(SINGLE_COMMENT); }
					case -32:
						break;
					case 32:
						{ return new Symbol(TokenConstants.DARROW); }
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }
					case -34:
						break;
					case 34:
						{ ++comment_level; yybegin(BLOCK_COMMENT); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.LET); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.NEW); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.NOT); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.CASE); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.LOOP); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.ELSE); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.ESAC); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.THEN); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.POOL); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.BOOL_CONST, true); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.CLASS); }
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.BOOL_CONST, false); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.WHILE); }
					case -48:
						break;
					case 48:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -49:
						break;
					case 49:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -50:
						break;
					case 50:
						{ }
					case -51:
						break;
					case 51:
						{ ++curr_lineno; yybegin(YYINITIAL); }
					case -52:
						break;
					case 52:
						{ if(--comment_level == 0) { yybegin(YYINITIAL); } }
					case -53:
						break;
					case 53:
						{ if(maxLengthExceeded() != null) { return maxLengthExceeded(); } string_buf.append(yytext()); }
					case -54:
						break;
					case 54:
						{ yybegin(YYINITIAL); return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(string_buf.toString())); }
					case -55:
						break;
					case 55:
						{ if(maxLengthExceeded() != null) { return maxLengthExceeded(); } yybegin(STRING_BACKSLASH); }
					case -56:
						break;
					case 56:
						{ yybegin(YYINITIAL); return new Symbol(TokenConstants.ERROR, "Unterminated string constant"); }
					case -57:
						break;
					case 57:
						{ yybegin(STRING_ERROR); return new Symbol(TokenConstants.ERROR, "String contains null character"); }
					case -58:
						break;
					case 58:
						{ if(maxLengthExceeded() != null) { return maxLengthExceeded(); } string_buf.append('\f'); }
					case -59:
						break;
					case 59:
						{ if(maxLengthExceeded() != null) { return maxLengthExceeded(); } string_buf.append('\n'); }
					case -60:
						break;
					case 60:
						{ if(maxLengthExceeded() != null) { return maxLengthExceeded(); } string_buf.append('\t'); }
					case -61:
						break;
					case 61:
						{ if(maxLengthExceeded() != null) { return maxLengthExceeded(); } string_buf.append('\b'); }
					case -62:
						break;
					case 62:
						{ }
					case -63:
						break;
					case 63:
						{ yybegin(YYINITIAL); }
					case -64:
						break;
					case 64:
						{ yybegin(STRING); string_buf.append(yytext()); }
					case -65:
						break;
					case 65:
						{ yybegin(STRING_ERROR); return new Symbol(TokenConstants.ERROR, "String contains null character"); }
					case -66:
						break;
					case 67:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -67:
						break;
					case 68:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -68:
						break;
					case 69:
						{ return new Symbol(TokenConstants.FI); }
					case -69:
						break;
					case 70:
						{ return new Symbol(TokenConstants.IF); }
					case -70:
						break;
					case 71:
						{ return new Symbol(TokenConstants.IN); }
					case -71:
						break;
					case 72:
						{ return new Symbol(TokenConstants.OF); }
					case -72:
						break;
					case 73:
						{ return new Symbol(TokenConstants.LET); }
					case -73:
						break;
					case 74:
						{ return new Symbol(TokenConstants.NEW); }
					case -74:
						break;
					case 75:
						{ return new Symbol(TokenConstants.NOT); }
					case -75:
						break;
					case 76:
						{ return new Symbol(TokenConstants.CASE); }
					case -76:
						break;
					case 77:
						{ return new Symbol(TokenConstants.LOOP); }
					case -77:
						break;
					case 78:
						{ return new Symbol(TokenConstants.ELSE); }
					case -78:
						break;
					case 79:
						{ return new Symbol(TokenConstants.ESAC); }
					case -79:
						break;
					case 80:
						{ return new Symbol(TokenConstants.THEN); }
					case -80:
						break;
					case 81:
						{ return new Symbol(TokenConstants.POOL); }
					case -81:
						break;
					case 82:
						{ return new Symbol(TokenConstants.CLASS); }
					case -82:
						break;
					case 83:
						{ return new Symbol(TokenConstants.WHILE); }
					case -83:
						break;
					case 84:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -84:
						break;
					case 85:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -85:
						break;
					case 86:
						{ }
					case -86:
						break;
					case 88:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -87:
						break;
					case 89:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -88:
						break;
					case 90:
						{ }
					case -89:
						break;
					case 92:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -90:
						break;
					case 93:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -91:
						break;
					case 95:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -92:
						break;
					case 96:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -93:
						break;
					case 98:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -94:
						break;
					case 99:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -95:
						break;
					case 100:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -96:
						break;
					case 101:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -97:
						break;
					case 102:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -98:
						break;
					case 103:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -99:
						break;
					case 104:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -100:
						break;
					case 105:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -101:
						break;
					case 106:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -102:
						break;
					case 107:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -103:
						break;
					case 108:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -104:
						break;
					case 109:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -105:
						break;
					case 110:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -106:
						break;
					case 111:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -107:
						break;
					case 112:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -108:
						break;
					case 113:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -109:
						break;
					case 114:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -110:
						break;
					case 115:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -111:
						break;
					case 116:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -112:
						break;
					case 117:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -113:
						break;
					case 118:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -114:
						break;
					case 119:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -115:
						break;
					case 120:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -116:
						break;
					case 121:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -117:
						break;
					case 122:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -118:
						break;
					case 123:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -119:
						break;
					case 124:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -120:
						break;
					case 125:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -121:
						break;
					case 126:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -122:
						break;
					case 127:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -123:
						break;
					case 128:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -124:
						break;
					case 129:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -125:
						break;
					case 130:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -126:
						break;
					case 131:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -127:
						break;
					case 132:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -128:
						break;
					case 133:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -129:
						break;
					case 134:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -130:
						break;
					case 135:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -131:
						break;
					case 136:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -132:
						break;
					case 137:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -133:
						break;
					case 138:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -134:
						break;
					case 139:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -135:
						break;
					case 140:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -136:
						break;
					case 141:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -137:
						break;
					case 142:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -138:
						break;
					case 143:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -139:
						break;
					case 144:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -140:
						break;
					case 145:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -141:
						break;
					case 146:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -142:
						break;
					case 147:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -143:
						break;
					case 148:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -144:
						break;
					case 149:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -145:
						break;
					case 150:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -146:
						break;
					case 151:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -147:
						break;
					case 152:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -148:
						break;
					case 153:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -149:
						break;
					case 154:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -150:
						break;
					case 155:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -151:
						break;
					case 156:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -152:
						break;
					case 157:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -153:
						break;
					case 158:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -154:
						break;
					case 159:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -155:
						break;
					case 160:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -156:
						break;
					case 161:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -157:
						break;
					case 162:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -158:
						break;
					case 163:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -159:
						break;
					case 164:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -160:
						break;
					case 165:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -161:
						break;
					case 166:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -162:
						break;
					case 167:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -163:
						break;
					case 168:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -164:
						break;
					case 169:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -165:
						break;
					case 170:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -166:
						break;
					case 171:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -167:
						break;
					case 172:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -168:
						break;
					case 173:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -169:
						break;
					case 174:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -170:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
