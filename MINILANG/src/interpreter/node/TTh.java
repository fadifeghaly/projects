/* This file was generated by SableCC (http://www.sablecc.org/). */

package interpreter.node;

import interpreter.analysis.*;

@SuppressWarnings("nls")
public final class TTh extends Token
{
    public TTh()
    {
        super.setText("header");
    }

    public TTh(int line, int pos)
    {
        super.setText("header");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TTh(getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTTh(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TTh text.");
    }
}
