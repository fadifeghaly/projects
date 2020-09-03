/* This file was generated by SableCC (http://www.sablecc.org/). */

package interpreter.node;

import interpreter.analysis.*;

@SuppressWarnings("nls")
public final class TEmail extends Token
{
    public TEmail()
    {
        super.setText("email");
    }

    public TEmail(int line, int pos)
    {
        super.setText("email");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TEmail(getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTEmail(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TEmail text.");
    }
}
