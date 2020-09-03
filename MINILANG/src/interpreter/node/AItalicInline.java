/* This file was generated by SableCC (http://www.sablecc.org/). */

package interpreter.node;

import interpreter.analysis.*;

@SuppressWarnings("nls")
public final class AItalicInline extends PInline
{
    private TTilde _tilde_;
    private TItalic _italic_;
    private PTerminal _terminal_;

    public AItalicInline()
    {
        // Constructor
    }

    public AItalicInline(
        @SuppressWarnings("hiding") TTilde _tilde_,
        @SuppressWarnings("hiding") TItalic _italic_,
        @SuppressWarnings("hiding") PTerminal _terminal_)
    {
        // Constructor
        setTilde(_tilde_);

        setItalic(_italic_);

        setTerminal(_terminal_);

    }

    @Override
    public Object clone()
    {
        return new AItalicInline(
            cloneNode(this._tilde_),
            cloneNode(this._italic_),
            cloneNode(this._terminal_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAItalicInline(this);
    }

    public TTilde getTilde()
    {
        return this._tilde_;
    }

    public void setTilde(TTilde node)
    {
        if(this._tilde_ != null)
        {
            this._tilde_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tilde_ = node;
    }

    public TItalic getItalic()
    {
        return this._italic_;
    }

    public void setItalic(TItalic node)
    {
        if(this._italic_ != null)
        {
            this._italic_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._italic_ = node;
    }

    public PTerminal getTerminal()
    {
        return this._terminal_;
    }

    public void setTerminal(PTerminal node)
    {
        if(this._terminal_ != null)
        {
            this._terminal_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._terminal_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._tilde_)
            + toString(this._italic_)
            + toString(this._terminal_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._tilde_ == child)
        {
            this._tilde_ = null;
            return;
        }

        if(this._italic_ == child)
        {
            this._italic_ = null;
            return;
        }

        if(this._terminal_ == child)
        {
            this._terminal_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._tilde_ == oldChild)
        {
            setTilde((TTilde) newChild);
            return;
        }

        if(this._italic_ == oldChild)
        {
            setItalic((TItalic) newChild);
            return;
        }

        if(this._terminal_ == oldChild)
        {
            setTerminal((PTerminal) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}