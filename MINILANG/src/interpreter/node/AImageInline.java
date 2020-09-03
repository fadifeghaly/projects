/* This file was generated by SableCC (http://www.sablecc.org/). */

package interpreter.node;

import interpreter.analysis.*;

@SuppressWarnings("nls")
public final class AImageInline extends PInline
{
    private TTilde _tilde_;
    private TImage _image_;
    private PTerminal _terminal_;

    public AImageInline()
    {
        // Constructor
    }

    public AImageInline(
        @SuppressWarnings("hiding") TTilde _tilde_,
        @SuppressWarnings("hiding") TImage _image_,
        @SuppressWarnings("hiding") PTerminal _terminal_)
    {
        // Constructor
        setTilde(_tilde_);

        setImage(_image_);

        setTerminal(_terminal_);

    }

    @Override
    public Object clone()
    {
        return new AImageInline(
            cloneNode(this._tilde_),
            cloneNode(this._image_),
            cloneNode(this._terminal_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAImageInline(this);
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

    public TImage getImage()
    {
        return this._image_;
    }

    public void setImage(TImage node)
    {
        if(this._image_ != null)
        {
            this._image_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._image_ = node;
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
            + toString(this._image_)
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

        if(this._image_ == child)
        {
            this._image_ = null;
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

        if(this._image_ == oldChild)
        {
            setImage((TImage) newChild);
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
