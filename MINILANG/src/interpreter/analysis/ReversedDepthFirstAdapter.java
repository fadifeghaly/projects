/* This file was generated by SableCC (http://www.sablecc.org/). */

package interpreter.analysis;

import java.util.*;
import interpreter.node.*;

public class ReversedDepthFirstAdapter extends AnalysisAdapter
{
    public void inStart(Start node)
    {
        defaultIn(node);
    }

    public void outStart(Start node)
    {
        defaultOut(node);
    }

    public void defaultIn(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    public void defaultOut(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    @Override
    public void caseStart(Start node)
    {
        inStart(node);
        node.getEOF().apply(this);
        node.getPDocument().apply(this);
        outStart(node);
    }

    public void inADocument(ADocument node)
    {
        defaultIn(node);
    }

    public void outADocument(ADocument node)
    {
        defaultOut(node);
    }

    @Override
    public void caseADocument(ADocument node)
    {
        inADocument(node);
        {
            List<PBlock> copy = new ArrayList<PBlock>(node.getBlocks());
            Collections.reverse(copy);
            for(PBlock e : copy)
            {
                e.apply(this);
            }
        }
        outADocument(node);
    }

    public void inAAuthorBlock(AAuthorBlock node)
    {
        defaultIn(node);
    }

    public void outAAuthorBlock(AAuthorBlock node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAuthorBlock(AAuthorBlock node)
    {
        inAAuthorBlock(node);
        {
            List<PInline> copy = new ArrayList<PInline>(node.getInlines());
            Collections.reverse(copy);
            for(PInline e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getRBrk() != null)
        {
            node.getRBrk().apply(this);
        }
        if(node.getAuthor() != null)
        {
            node.getAuthor().apply(this);
        }
        if(node.getLBrk() != null)
        {
            node.getLBrk().apply(this);
        }
        outAAuthorBlock(node);
    }

    public void inATitleBlock(ATitleBlock node)
    {
        defaultIn(node);
    }

    public void outATitleBlock(ATitleBlock node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATitleBlock(ATitleBlock node)
    {
        inATitleBlock(node);
        {
            List<PInline> copy = new ArrayList<PInline>(node.getInlines());
            Collections.reverse(copy);
            for(PInline e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getRBrk() != null)
        {
            node.getRBrk().apply(this);
        }
        if(node.getTitle() != null)
        {
            node.getTitle().apply(this);
        }
        if(node.getLBrk() != null)
        {
            node.getLBrk().apply(this);
        }
        outATitleBlock(node);
    }

    public void inADateBlock(ADateBlock node)
    {
        defaultIn(node);
    }

    public void outADateBlock(ADateBlock node)
    {
        defaultOut(node);
    }

    @Override
    public void caseADateBlock(ADateBlock node)
    {
        inADateBlock(node);
        {
            List<PInline> copy = new ArrayList<PInline>(node.getInlines());
            Collections.reverse(copy);
            for(PInline e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getRBrk() != null)
        {
            node.getRBrk().apply(this);
        }
        if(node.getDate() != null)
        {
            node.getDate().apply(this);
        }
        if(node.getLBrk() != null)
        {
            node.getLBrk().apply(this);
        }
        outADateBlock(node);
    }

    public void inAFontSizeBlock(AFontSizeBlock node)
    {
        defaultIn(node);
    }

    public void outAFontSizeBlock(AFontSizeBlock node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFontSizeBlock(AFontSizeBlock node)
    {
        inAFontSizeBlock(node);
        {
            List<PInline> copy = new ArrayList<PInline>(node.getInlines());
            Collections.reverse(copy);
            for(PInline e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getRBrk() != null)
        {
            node.getRBrk().apply(this);
        }
        if(node.getFontSize() != null)
        {
            node.getFontSize().apply(this);
        }
        if(node.getLBrk() != null)
        {
            node.getLBrk().apply(this);
        }
        outAFontSizeBlock(node);
    }

    public void inABoldBlock(ABoldBlock node)
    {
        defaultIn(node);
    }

    public void outABoldBlock(ABoldBlock node)
    {
        defaultOut(node);
    }

    @Override
    public void caseABoldBlock(ABoldBlock node)
    {
        inABoldBlock(node);
        {
            List<PInline> copy = new ArrayList<PInline>(node.getInlines());
            Collections.reverse(copy);
            for(PInline e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getRBrk() != null)
        {
            node.getRBrk().apply(this);
        }
        if(node.getBold() != null)
        {
            node.getBold().apply(this);
        }
        if(node.getLBrk() != null)
        {
            node.getLBrk().apply(this);
        }
        outABoldBlock(node);
    }

    public void inAItalicBlock(AItalicBlock node)
    {
        defaultIn(node);
    }

    public void outAItalicBlock(AItalicBlock node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAItalicBlock(AItalicBlock node)
    {
        inAItalicBlock(node);
        {
            List<PInline> copy = new ArrayList<PInline>(node.getInlines());
            Collections.reverse(copy);
            for(PInline e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getRBrk() != null)
        {
            node.getRBrk().apply(this);
        }
        if(node.getItalic() != null)
        {
            node.getItalic().apply(this);
        }
        if(node.getLBrk() != null)
        {
            node.getLBrk().apply(this);
        }
        outAItalicBlock(node);
    }

    public void inAImageBlock(AImageBlock node)
    {
        defaultIn(node);
    }

    public void outAImageBlock(AImageBlock node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAImageBlock(AImageBlock node)
    {
        inAImageBlock(node);
        {
            List<PInline> copy = new ArrayList<PInline>(node.getInlines());
            Collections.reverse(copy);
            for(PInline e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getRBrk() != null)
        {
            node.getRBrk().apply(this);
        }
        if(node.getImage() != null)
        {
            node.getImage().apply(this);
        }
        if(node.getLBrk() != null)
        {
            node.getLBrk().apply(this);
        }
        outAImageBlock(node);
    }

    public void inALinkBlock(ALinkBlock node)
    {
        defaultIn(node);
    }

    public void outALinkBlock(ALinkBlock node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALinkBlock(ALinkBlock node)
    {
        inALinkBlock(node);
        {
            List<PInline> copy = new ArrayList<PInline>(node.getInlines());
            Collections.reverse(copy);
            for(PInline e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getRBrk() != null)
        {
            node.getRBrk().apply(this);
        }
        if(node.getLink() != null)
        {
            node.getLink().apply(this);
        }
        if(node.getLBrk() != null)
        {
            node.getLBrk().apply(this);
        }
        outALinkBlock(node);
    }

    public void inAEmailBlock(AEmailBlock node)
    {
        defaultIn(node);
    }

    public void outAEmailBlock(AEmailBlock node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAEmailBlock(AEmailBlock node)
    {
        inAEmailBlock(node);
        {
            List<PInline> copy = new ArrayList<PInline>(node.getInlines());
            Collections.reverse(copy);
            for(PInline e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getRBrk() != null)
        {
            node.getRBrk().apply(this);
        }
        if(node.getEmail() != null)
        {
            node.getEmail().apply(this);
        }
        if(node.getLBrk() != null)
        {
            node.getLBrk().apply(this);
        }
        outAEmailBlock(node);
    }

    public void inAParaBlock(AParaBlock node)
    {
        defaultIn(node);
    }

    public void outAParaBlock(AParaBlock node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAParaBlock(AParaBlock node)
    {
        inAParaBlock(node);
        {
            List<PInline> copy = new ArrayList<PInline>(node.getInlines());
            Collections.reverse(copy);
            for(PInline e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getRBrk() != null)
        {
            node.getRBrk().apply(this);
        }
        if(node.getPara() != null)
        {
            node.getPara().apply(this);
        }
        if(node.getLBrk() != null)
        {
            node.getLBrk().apply(this);
        }
        outAParaBlock(node);
    }

    public void inAListBlock(AListBlock node)
    {
        defaultIn(node);
    }

    public void outAListBlock(AListBlock node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAListBlock(AListBlock node)
    {
        inAListBlock(node);
        {
            List<PListItem> copy = new ArrayList<PListItem>(node.getItems());
            Collections.reverse(copy);
            for(PListItem e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getRBrk() != null)
        {
            node.getRBrk().apply(this);
        }
        if(node.getList() != null)
        {
            node.getList().apply(this);
        }
        if(node.getLBrk() != null)
        {
            node.getLBrk().apply(this);
        }
        outAListBlock(node);
    }

    public void inATableBlock(ATableBlock node)
    {
        defaultIn(node);
    }

    public void outATableBlock(ATableBlock node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATableBlock(ATableBlock node)
    {
        inATableBlock(node);
        {
            List<PTableRow> copy = new ArrayList<PTableRow>(node.getItems());
            Collections.reverse(copy);
            for(PTableRow e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getRBrk() != null)
        {
            node.getRBrk().apply(this);
        }
        if(node.getTable() != null)
        {
            node.getTable().apply(this);
        }
        if(node.getLBrk() != null)
        {
            node.getLBrk().apply(this);
        }
        outATableBlock(node);
    }

    public void inAListItem(AListItem node)
    {
        defaultIn(node);
    }

    public void outAListItem(AListItem node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAListItem(AListItem node)
    {
        inAListItem(node);
        if(node.getInlines() != null)
        {
            node.getInlines().apply(this);
        }
        if(node.getDash() != null)
        {
            node.getDash().apply(this);
        }
        outAListItem(node);
    }

    public void inATableRow(ATableRow node)
    {
        defaultIn(node);
    }

    public void outATableRow(ATableRow node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATableRow(ATableRow node)
    {
        inATableRow(node);
        if(node.getRPar() != null)
        {
            node.getRPar().apply(this);
        }
        {
            List<PTableItem> copy = new ArrayList<PTableItem>(node.getItems());
            Collections.reverse(copy);
            for(PTableItem e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getLPar() != null)
        {
            node.getLPar().apply(this);
        }
        if(node.getTr() != null)
        {
            node.getTr().apply(this);
        }
        if(node.getTilde() != null)
        {
            node.getTilde().apply(this);
        }
        outATableRow(node);
    }

    public void inATdTableItem(ATdTableItem node)
    {
        defaultIn(node);
    }

    public void outATdTableItem(ATdTableItem node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATdTableItem(ATdTableItem node)
    {
        inATdTableItem(node);
        if(node.getTerminal() != null)
        {
            node.getTerminal().apply(this);
        }
        if(node.getTd() != null)
        {
            node.getTd().apply(this);
        }
        if(node.getTilde() != null)
        {
            node.getTilde().apply(this);
        }
        outATdTableItem(node);
    }

    public void inAThTableItem(AThTableItem node)
    {
        defaultIn(node);
    }

    public void outAThTableItem(AThTableItem node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAThTableItem(AThTableItem node)
    {
        inAThTableItem(node);
        if(node.getTerminal() != null)
        {
            node.getTerminal().apply(this);
        }
        if(node.getTh() != null)
        {
            node.getTh().apply(this);
        }
        if(node.getTilde() != null)
        {
            node.getTilde().apply(this);
        }
        outAThTableItem(node);
    }

    public void inATerminalInline(ATerminalInline node)
    {
        defaultIn(node);
    }

    public void outATerminalInline(ATerminalInline node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATerminalInline(ATerminalInline node)
    {
        inATerminalInline(node);
        if(node.getTerminal() != null)
        {
            node.getTerminal().apply(this);
        }
        outATerminalInline(node);
    }

    public void inAAuthorInline(AAuthorInline node)
    {
        defaultIn(node);
    }

    public void outAAuthorInline(AAuthorInline node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAuthorInline(AAuthorInline node)
    {
        inAAuthorInline(node);
        if(node.getTerminal() != null)
        {
            node.getTerminal().apply(this);
        }
        if(node.getAuthor() != null)
        {
            node.getAuthor().apply(this);
        }
        if(node.getTilde() != null)
        {
            node.getTilde().apply(this);
        }
        outAAuthorInline(node);
    }

    public void inATitleInline(ATitleInline node)
    {
        defaultIn(node);
    }

    public void outATitleInline(ATitleInline node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATitleInline(ATitleInline node)
    {
        inATitleInline(node);
        if(node.getTerminal() != null)
        {
            node.getTerminal().apply(this);
        }
        if(node.getTitle() != null)
        {
            node.getTitle().apply(this);
        }
        if(node.getTilde() != null)
        {
            node.getTilde().apply(this);
        }
        outATitleInline(node);
    }

    public void inADateInline(ADateInline node)
    {
        defaultIn(node);
    }

    public void outADateInline(ADateInline node)
    {
        defaultOut(node);
    }

    @Override
    public void caseADateInline(ADateInline node)
    {
        inADateInline(node);
        if(node.getTerminal() != null)
        {
            node.getTerminal().apply(this);
        }
        if(node.getDate() != null)
        {
            node.getDate().apply(this);
        }
        if(node.getTilde() != null)
        {
            node.getTilde().apply(this);
        }
        outADateInline(node);
    }

    public void inAEmailInline(AEmailInline node)
    {
        defaultIn(node);
    }

    public void outAEmailInline(AEmailInline node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAEmailInline(AEmailInline node)
    {
        inAEmailInline(node);
        if(node.getTerminal() != null)
        {
            node.getTerminal().apply(this);
        }
        if(node.getEmail() != null)
        {
            node.getEmail().apply(this);
        }
        if(node.getTilde() != null)
        {
            node.getTilde().apply(this);
        }
        outAEmailInline(node);
    }

    public void inAImageInline(AImageInline node)
    {
        defaultIn(node);
    }

    public void outAImageInline(AImageInline node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAImageInline(AImageInline node)
    {
        inAImageInline(node);
        if(node.getTerminal() != null)
        {
            node.getTerminal().apply(this);
        }
        if(node.getImage() != null)
        {
            node.getImage().apply(this);
        }
        if(node.getTilde() != null)
        {
            node.getTilde().apply(this);
        }
        outAImageInline(node);
    }

    public void inALinkInline(ALinkInline node)
    {
        defaultIn(node);
    }

    public void outALinkInline(ALinkInline node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALinkInline(ALinkInline node)
    {
        inALinkInline(node);
        if(node.getTerminal() != null)
        {
            node.getTerminal().apply(this);
        }
        if(node.getLink() != null)
        {
            node.getLink().apply(this);
        }
        if(node.getTilde() != null)
        {
            node.getTilde().apply(this);
        }
        outALinkInline(node);
    }

    public void inABoldInline(ABoldInline node)
    {
        defaultIn(node);
    }

    public void outABoldInline(ABoldInline node)
    {
        defaultOut(node);
    }

    @Override
    public void caseABoldInline(ABoldInline node)
    {
        inABoldInline(node);
        if(node.getTerminal() != null)
        {
            node.getTerminal().apply(this);
        }
        if(node.getBold() != null)
        {
            node.getBold().apply(this);
        }
        if(node.getTilde() != null)
        {
            node.getTilde().apply(this);
        }
        outABoldInline(node);
    }

    public void inAItalicInline(AItalicInline node)
    {
        defaultIn(node);
    }

    public void outAItalicInline(AItalicInline node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAItalicInline(AItalicInline node)
    {
        inAItalicInline(node);
        if(node.getTerminal() != null)
        {
            node.getTerminal().apply(this);
        }
        if(node.getItalic() != null)
        {
            node.getItalic().apply(this);
        }
        if(node.getTilde() != null)
        {
            node.getTilde().apply(this);
        }
        outAItalicInline(node);
    }

    public void inAFontSizeInline(AFontSizeInline node)
    {
        defaultIn(node);
    }

    public void outAFontSizeInline(AFontSizeInline node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFontSizeInline(AFontSizeInline node)
    {
        inAFontSizeInline(node);
        if(node.getTerminal() != null)
        {
            node.getTerminal().apply(this);
        }
        if(node.getFontSize() != null)
        {
            node.getFontSize().apply(this);
        }
        if(node.getTilde() != null)
        {
            node.getTilde().apply(this);
        }
        outAFontSizeInline(node);
    }

    public void inAPlainTextTerminal(APlainTextTerminal node)
    {
        defaultIn(node);
    }

    public void outAPlainTextTerminal(APlainTextTerminal node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAPlainTextTerminal(APlainTextTerminal node)
    {
        inAPlainTextTerminal(node);
        if(node.getPlainText() != null)
        {
            node.getPlainText().apply(this);
        }
        outAPlainTextTerminal(node);
    }

    public void inAInlineTerminal(AInlineTerminal node)
    {
        defaultIn(node);
    }

    public void outAInlineTerminal(AInlineTerminal node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAInlineTerminal(AInlineTerminal node)
    {
        inAInlineTerminal(node);
        if(node.getRPar() != null)
        {
            node.getRPar().apply(this);
        }
        if(node.getInline() != null)
        {
            node.getInline().apply(this);
        }
        if(node.getLPar() != null)
        {
            node.getLPar().apply(this);
        }
        outAInlineTerminal(node);
    }
}
