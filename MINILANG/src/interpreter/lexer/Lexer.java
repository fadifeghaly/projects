/* This file was generated by SableCC (http://www.sablecc.org/). */

package interpreter.lexer;

import java.io.*;
import interpreter.node.*;

@SuppressWarnings("nls")
public class Lexer
{
    protected Token token;
    protected State state = State.INITIAL;

    private IPushbackReader in;
    private int line;
    private int pos;
    private boolean cr;
    private boolean eof;
    private final StringBuffer text = new StringBuffer();

    @SuppressWarnings("unused")
    protected void filter() throws LexerException, IOException
    {
        // Do nothing
    }

    public Lexer(@SuppressWarnings("hiding") final PushbackReader in)
    {
        this.in = new IPushbackReader() {

            private PushbackReader pushbackReader = in;
            
            @Override
            public void unread(int c) throws IOException {
                pushbackReader.unread(c);
            }
            
            @Override
            public int read() throws IOException {
                return pushbackReader.read();
            }
        };
    }
 
    public Lexer(@SuppressWarnings("hiding") IPushbackReader in)
    {
        this.in = in;
    }
 
    public Token peek() throws LexerException, IOException
    {
        while(this.token == null)
        {
            this.token = getToken();
            filter();
        }

        return this.token;
    }

    public Token next() throws LexerException, IOException
    {
        while(this.token == null)
        {
            this.token = getToken();
            filter();
        }

        Token result = this.token;
        this.token = null;
        return result;
    }

    protected Token getToken() throws IOException, LexerException
    {
        int dfa_state = 0;

        int start_pos = this.pos;
        int start_line = this.line;

        int accept_state = -1;
        int accept_token = -1;
        int accept_length = -1;
        int accept_pos = -1;
        int accept_line = -1;

        @SuppressWarnings("hiding") int[][][] gotoTable = Lexer.gotoTable[this.state.id()];
        @SuppressWarnings("hiding") int[] accept = Lexer.accept[this.state.id()];
        this.text.setLength(0);

        while(true)
        {
            int c = getChar();

            if(c != -1)
            {
                switch(c)
                {
                case 10:
                    if(this.cr)
                    {
                        this.cr = false;
                    }
                    else
                    {
                        this.line++;
                        this.pos = 0;
                    }
                    break;
                case 13:
                    this.line++;
                    this.pos = 0;
                    this.cr = true;
                    break;
                default:
                    this.pos++;
                    this.cr = false;
                    break;
                }

                this.text.append((char) c);

                do
                {
                    int oldState = (dfa_state < -1) ? (-2 -dfa_state) : dfa_state;

                    dfa_state = -1;

                    int[][] tmp1 =  gotoTable[oldState];
                    int low = 0;
                    int high = tmp1.length - 1;

                    while(low <= high)
                    {
                        // int middle = (low + high) / 2;
                        int middle = (low + high) >>> 1;
                        int[] tmp2 = tmp1[middle];

                        if(c < tmp2[0])
                        {
                            high = middle - 1;
                        }
                        else if(c > tmp2[1])
                        {
                            low = middle + 1;
                        }
                        else
                        {
                            dfa_state = tmp2[2];
                            break;
                        }
                    }
                }while(dfa_state < -1);
            }
            else
            {
                dfa_state = -1;
            }

            if(dfa_state >= 0)
            {
                if(accept[dfa_state] != -1)
                {
                    accept_state = dfa_state;
                    accept_token = accept[dfa_state];
                    accept_length = this.text.length();
                    accept_pos = this.pos;
                    accept_line = this.line;
                }
            }
            else
            {
                if(accept_state != -1)
                {
                    switch(accept_token)
                    {
                    case 0:
                        {
                            @SuppressWarnings("hiding") Token token = new0(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 1:
                        {
                            @SuppressWarnings("hiding") Token token = new1(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 2:
                        {
                            @SuppressWarnings("hiding") Token token = new2(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 3:
                        {
                            @SuppressWarnings("hiding") Token token = new3(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 4:
                        {
                            @SuppressWarnings("hiding") Token token = new4(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 5:
                        {
                            @SuppressWarnings("hiding") Token token = new5(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 6:
                        {
                            @SuppressWarnings("hiding") Token token = new6(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 7:
                        {
                            @SuppressWarnings("hiding") Token token = new7(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 8:
                        {
                            @SuppressWarnings("hiding") Token token = new8(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 9:
                        {
                            @SuppressWarnings("hiding") Token token = new9(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 10:
                        {
                            @SuppressWarnings("hiding") Token token = new10(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 11:
                        {
                            @SuppressWarnings("hiding") Token token = new11(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 12:
                        {
                            @SuppressWarnings("hiding") Token token = new12(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 13:
                        {
                            @SuppressWarnings("hiding") Token token = new13(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 14:
                        {
                            @SuppressWarnings("hiding") Token token = new14(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 15:
                        {
                            @SuppressWarnings("hiding") Token token = new15(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 16:
                        {
                            @SuppressWarnings("hiding") Token token = new16(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 17:
                        {
                            @SuppressWarnings("hiding") Token token = new17(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 18:
                        {
                            @SuppressWarnings("hiding") Token token = new18(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 19:
                        {
                            @SuppressWarnings("hiding") Token token = new19(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 20:
                        {
                            @SuppressWarnings("hiding") Token token = new20(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 21:
                        {
                            @SuppressWarnings("hiding") Token token = new21(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 22:
                        {
                            @SuppressWarnings("hiding") Token token = new22(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    }
                }
                else
                {
                    if(this.text.length() > 0)
                    {
                        throw new LexerException(
                            new InvalidToken(this.text.substring(0, 1), start_line + 1, start_pos + 1),
                            "[" + (start_line + 1) + "," + (start_pos + 1) + "]" +
                            " Unknown token: " + this.text);
                    }

                    @SuppressWarnings("hiding") EOF token = new EOF(
                        start_line + 1,
                        start_pos + 1);
                    return token;
                }
            }
        }
    }

    Token new0(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TLPar(line, pos); }
    Token new1(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TRPar(line, pos); }
    Token new2(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TLBrk(line, pos); }
    Token new3(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TRBrk(line, pos); }
    Token new4(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TDash(line, pos); }
    Token new5(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TTilde(line, pos); }
    Token new6(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TAuthor(line, pos); }
    Token new7(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TTitle(line, pos); }
    Token new8(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TDate(line, pos); }
    Token new9(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TImage(line, pos); }
    Token new10(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TLink(line, pos); }
    Token new11(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TBold(line, pos); }
    Token new12(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TItalic(line, pos); }
    Token new13(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TEmail(line, pos); }
    Token new14(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TPara(line, pos); }
    Token new15(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TList(line, pos); }
    Token new16(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TTable(line, pos); }
    Token new17(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TFontSize(line, pos); }
    Token new18(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TTr(line, pos); }
    Token new19(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TTd(line, pos); }
    Token new20(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TTh(line, pos); }
    Token new21(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TBlank(text, line, pos); }
    Token new22(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TPlainText(text, line, pos); }

    private int getChar() throws IOException
    {
        if(this.eof)
        {
            return -1;
        }

        int result = this.in.read();

        if(result == -1)
        {
            this.eof = true;
        }

        return result;
    }

    private void pushBack(int acceptLength) throws IOException
    {
        int length = this.text.length();
        for(int i = length - 1; i >= acceptLength; i--)
        {
            this.eof = false;

            this.in.unread(this.text.charAt(i));
        }
    }

    protected void unread(@SuppressWarnings("hiding") Token token) throws IOException
    {
        @SuppressWarnings("hiding") String text = token.getText();
        int length = text.length();

        for(int i = length - 1; i >= 0; i--)
        {
            this.eof = false;

            this.in.unread(text.charAt(i));
        }

        this.pos = token.getPos() - 1;
        this.line = token.getLine() - 1;
    }

    private String getText(int acceptLength)
    {
        StringBuffer s = new StringBuffer(acceptLength);
        for(int i = 0; i < acceptLength; i++)
        {
            s.append(this.text.charAt(i));
        }

        return s.toString();
    }

    private static int[][][][] gotoTable;
/*  {
        { // INITIAL
            {{9, 9, 1}, {10, 10, 2}, {13, 13, 3}, {32, 32, 4}, {33, 39, 5}, {40, 40, 6}, {41, 41, 7}, {42, 44, 5}, {45, 45, 8}, {46, 71, 5}, {72, 72, 9}, {73, 90, 5}, {91, 91, 10}, {92, 92, 5}, {93, 93, 11}, {94, 96, 5}, {97, 97, 12}, {98, 98, 13}, {99, 99, 5}, {100, 100, 14}, {101, 101, 15}, {102, 103, 5}, {104, 104, 16}, {105, 105, 17}, {106, 107, 5}, {108, 108, 18}, {109, 111, 5}, {112, 112, 19}, {113, 113, 5}, {114, 114, 20}, {115, 115, 5}, {116, 116, 21}, {117, 125, 5}, {126, 126, 22}, },
            {{9, 39, -2}, {42, 44, 5}, {46, 90, 5}, {92, 92, 5}, {94, 125, 5}, },
            {{9, 125, -3}, },
            {{9, 125, -3}, },
            {{9, 125, -3}, },
            {{9, 9, 23}, {10, 10, 24}, {13, 13, 25}, {32, 32, 26}, {33, 125, -3}, },
            {},
            {},
            {},
            {{9, 44, -7}, {46, 48, 5}, {49, 49, 27}, {50, 90, 5}, {92, 125, -3}, },
            {},
            {},
            {{9, 92, -7}, {94, 116, 5}, {117, 117, 28}, {118, 125, 5}, },
            {{9, 92, -7}, {94, 110, 5}, {111, 111, 29}, {112, 125, 5}, },
            {{9, 92, -7}, {94, 96, 5}, {97, 97, 30}, {98, 125, 5}, },
            {{9, 92, -7}, {94, 108, 5}, {109, 109, 31}, {110, 125, 5}, },
            {{9, 92, -7}, {94, 100, 5}, {101, 101, 32}, {102, 125, 5}, },
            {{9, 108, -17}, {109, 109, 33}, {110, 115, 5}, {116, 116, 34}, {117, 125, 5}, },
            {{9, 92, -7}, {94, 104, 5}, {105, 105, 35}, {106, 125, 5}, },
            {{9, 96, -16}, {97, 97, 36}, {98, 125, 5}, },
            {{9, 110, -15}, {111, 111, 37}, {112, 125, 5}, },
            {{9, 96, -16}, {97, 97, 38}, {98, 104, 5}, {105, 105, 39}, {106, 125, 5}, },
            {},
            {{9, 125, -7}, },
            {{9, 125, -7}, },
            {{9, 125, -7}, },
            {{9, 125, -7}, },
            {{9, 125, -7}, },
            {{9, 92, -7}, {94, 115, 5}, {116, 116, 40}, {117, 125, 5}, },
            {{9, 92, -7}, {94, 107, 5}, {108, 108, 41}, {109, 125, 5}, },
            {{9, 115, -30}, {116, 116, 42}, {117, 125, 5}, },
            {{9, 96, -16}, {97, 97, 43}, {98, 125, 5}, },
            {{9, 96, -16}, {97, 97, 44}, {98, 125, 5}, },
            {{9, 96, -16}, {97, 97, 45}, {98, 125, 5}, },
            {{9, 96, -16}, {97, 97, 46}, {98, 125, 5}, },
            {{9, 92, -7}, {94, 109, 5}, {110, 110, 47}, {111, 114, 5}, {115, 115, 48}, {116, 125, 5}, },
            {{9, 92, -7}, {94, 113, 5}, {114, 114, 49}, {115, 125, 5}, },
            {{9, 92, -7}, {94, 118, 5}, {119, 119, 50}, {120, 125, 5}, },
            {{9, 92, -7}, {94, 97, 5}, {98, 98, 51}, {99, 125, 5}, },
            {{9, 115, -30}, {116, 116, 52}, {117, 125, 5}, },
            {{9, 92, -7}, {94, 103, 5}, {104, 104, 53}, {105, 125, 5}, },
            {{9, 92, -7}, {94, 99, 5}, {100, 100, 54}, {101, 125, 5}, },
            {{9, 96, -16}, {97, 97, 55}, {98, 100, 5}, {101, 101, 56}, {102, 125, 5}, },
            {{9, 104, -20}, {105, 105, 57}, {106, 125, 5}, },
            {{9, 99, -43}, {100, 100, 58}, {101, 125, 5}, },
            {{9, 92, -7}, {94, 102, 5}, {103, 103, 59}, {104, 125, 5}, },
            {{9, 107, -31}, {108, 108, 60}, {109, 125, 5}, },
            {{9, 92, -7}, {94, 106, 5}, {107, 107, 61}, {108, 125, 5}, },
            {{9, 115, -30}, {116, 116, 62}, {117, 125, 5}, },
            {{9, 96, -16}, {97, 97, 63}, {98, 125, 5}, },
            {{9, 125, -7}, },
            {{9, 107, -31}, {108, 108, 64}, {109, 125, 5}, },
            {{9, 107, -31}, {108, 108, 65}, {109, 125, 5}, },
            {{9, 110, -15}, {111, 111, 66}, {112, 125, 5}, },
            {{9, 125, -7}, },
            {{9, 125, -7}, },
            {{9, 125, -7}, },
            {{9, 107, -31}, {108, 108, 67}, {109, 125, 5}, },
            {{9, 100, -18}, {101, 101, 68}, {102, 125, 5}, },
            {{9, 100, -18}, {101, 101, 69}, {102, 125, 5}, },
            {{9, 104, -20}, {105, 105, 70}, {106, 125, 5}, },
            {{9, 125, -7}, },
            {{9, 125, -7}, },
            {{9, 102, -47}, {103, 103, 71}, {104, 125, 5}, },
            {{9, 100, -18}, {101, 101, 72}, {102, 125, 5}, },
            {{9, 100, -18}, {101, 101, 73}, {102, 125, 5}, },
            {{9, 113, -38}, {114, 114, 74}, {115, 125, 5}, },
            {{9, 125, -7}, },
            {{9, 113, -38}, {114, 114, 75}, {115, 125, 5}, },
            {{9, 125, -7}, },
            {{9, 92, -7}, {94, 98, 5}, {99, 99, 76}, {100, 125, 5}, },
            {{9, 113, -38}, {114, 114, 77}, {115, 125, 5}, },
            {{9, 125, -7}, },
            {{9, 125, -7}, },
            {{9, 125, -7}, },
            {{9, 125, -7}, },
            {{9, 125, -7}, },
            {{9, 96, -16}, {97, 97, 78}, {98, 125, 5}, },
            {{9, 92, -7}, {94, 111, 5}, {112, 112, 79}, {113, 125, 5}, },
            {{9, 103, -42}, {104, 104, 80}, {105, 125, 5}, },
            {{9, 125, -7}, },
        }
    };*/

    private static int[][] accept;
/*  {
        // INITIAL
        {-1, 21, 21, 21, 21, 22, 0, 1, 4, 22, 2, 3, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 5, 22, 22, 22, 22, 17, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 18, 22, 22, 22, 11, 19, 8, 22, 22, 22, 22, 10, 15, 22, 22, 22, 22, 13, 22, 9, 22, 22, 16, 7, 6, 20, 12, 22, 22, 22, 14, },

    };*/

    public static class State
    {
        public final static State INITIAL = new State(0);

        private int id;

        private State(@SuppressWarnings("hiding") int id)
        {
            this.id = id;
        }

        public int id()
        {
            return this.id;
        }
    }

    static 
    {
        try
        {
            DataInputStream s = new DataInputStream(
                new BufferedInputStream(
                Lexer.class.getResourceAsStream("lexer.dat")));

            // read gotoTable
            int length = s.readInt();
            gotoTable = new int[length][][][];
            for(int i = 0; i < gotoTable.length; i++)
            {
                length = s.readInt();
                gotoTable[i] = new int[length][][];
                for(int j = 0; j < gotoTable[i].length; j++)
                {
                    length = s.readInt();
                    gotoTable[i][j] = new int[length][3];
                    for(int k = 0; k < gotoTable[i][j].length; k++)
                    {
                        for(int l = 0; l < 3; l++)
                        {
                            gotoTable[i][j][k][l] = s.readInt();
                        }
                    }
                }
            }

            // read accept
            length = s.readInt();
            accept = new int[length][];
            for(int i = 0; i < accept.length; i++)
            {
                length = s.readInt();
                accept[i] = new int[length];
                for(int j = 0; j < accept[i].length; j++)
                {
                    accept[i][j] = s.readInt();
                }
            }

            s.close();
        }
        catch(Exception e)
        {
            throw new RuntimeException("The file \"lexer.dat\" is either missing or corrupted.");
        }
    }
}
