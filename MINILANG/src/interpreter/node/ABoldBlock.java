/* This file was generated by SableCC (http://www.sablecc.org/). */

package interpreter.node;

import java.util.*;
import interpreter.analysis.*;

@SuppressWarnings("nls")
public final class ABoldBlock extends PBlock
{
    private TLBrk _lBrk_;
    private TBold _bold_;
    private TRBrk _rBrk_;
    private final LinkedList<PInline> _inlines_ = new LinkedList<PInline>();

    public ABoldBlock()
    {
        // Constructor
    }

    public ABoldBlock(
        @SuppressWarnings("hiding") TLBrk _lBrk_,
        @SuppressWarnings("hiding") TBold _bold_,
        @SuppressWarnings("hiding") TRBrk _rBrk_,
        @SuppressWarnings("hiding") List<?> _inlines_)
    {
        // Constructor
        setLBrk(_lBrk_);

        setBold(_bold_);

        setRBrk(_rBrk_);

        setInlines(_inlines_);

    }

    @Override
    public Object clone()
    {
        return new ABoldBlock(
            cloneNode(this._lBrk_),
            cloneNode(this._bold_),
            cloneNode(this._rBrk_),
            cloneList(this._inlines_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseABoldBlock(this);
    }

    public TLBrk getLBrk()
    {
        return this._lBrk_;
    }

    public void setLBrk(TLBrk node)
    {
        if(this._lBrk_ != null)
        {
            this._lBrk_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._lBrk_ = node;
    }

    public TBold getBold()
    {
        return this._bold_;
    }

    public void setBold(TBold node)
    {
        if(this._bold_ != null)
        {
            this._bold_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._bold_ = node;
    }

    public TRBrk getRBrk()
    {
        return this._rBrk_;
    }

    public void setRBrk(TRBrk node)
    {
        if(this._rBrk_ != null)
        {
            this._rBrk_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._rBrk_ = node;
    }

    public LinkedList<PInline> getInlines()
    {
        return this._inlines_;
    }

    public void setInlines(List<?> list)
    {
        for(PInline e : this._inlines_)
        {
            e.parent(null);
        }
        this._inlines_.clear();

        for(Object obj_e : list)
        {
            PInline e = (PInline) obj_e;
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
            this._inlines_.add(e);
        }
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._lBrk_)
            + toString(this._bold_)
            + toString(this._rBrk_)
            + toString(this._inlines_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._lBrk_ == child)
        {
            this._lBrk_ = null;
            return;
        }

        if(this._bold_ == child)
        {
            this._bold_ = null;
            return;
        }

        if(this._rBrk_ == child)
        {
            this._rBrk_ = null;
            return;
        }

        if(this._inlines_.remove(child))
        {
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._lBrk_ == oldChild)
        {
            setLBrk((TLBrk) newChild);
            return;
        }

        if(this._bold_ == oldChild)
        {
            setBold((TBold) newChild);
            return;
        }

        if(this._rBrk_ == oldChild)
        {
            setRBrk((TRBrk) newChild);
            return;
        }

        for(ListIterator<PInline> i = this._inlines_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PInline) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        throw new RuntimeException("Not a child.");
    }
}
