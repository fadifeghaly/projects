/* This file was generated by SableCC (http://www.sablecc.org/). */

package interpreter.node;

import interpreter.analysis.*;

@SuppressWarnings("nls")
public final class TItalic extends Token
{
    public TItalic()
    {
        super.setText("italic");
    }

    public TItalic(int line, int pos)
    {
        super.setText("italic");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TItalic(getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTItalic(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TItalic text.");
    }
}