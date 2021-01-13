package com.jiuqi.dna.gams.jy03.printing.util;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.printing.Orientation;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;

public class PDFPageableExtend extends Book {
	private final PDDocument document;
    private final boolean showPageBorder;
    private final float dpi;
    private final Orientation orientation;

    /**
     * Creates a new PDFPageable.
     *
     * @param document the document to print
     */
    public PDFPageableExtend(PDDocument document)
    {
        this(document, Orientation.AUTO, false, 0);
    }
    
    /**
     * Creates a new PDFPageable with the given page orientation.
     *
     * @param document the document to print
     * @param orientation page orientation policy
     */
    public PDFPageableExtend(PDDocument document, Orientation orientation)
    {
        this(document, orientation, false, 0);
    }
    
    /**
     * Creates a new PDFPageable with the given page orientation and with optional page borders
     * shown. The image will be rasterized at the given DPI before being sent to the printer.
     *
     * @param document the document to print
     * @param orientation page orientation policy
     * @param showPageBorder true if page borders are to be printed
     */
    public PDFPageableExtend(PDDocument document, Orientation orientation, boolean showPageBorder)
    {
        this(document, orientation, showPageBorder, 0);
    }

    /**
     * Creates a new PDFPageable with the given page orientation and with optional page borders
     * shown. The image will be rasterized at the given DPI before being sent to the printer.
     *
     * @param document the document to print
     * @param orientation page orientation policy
     * @param showPageBorder true if page borders are to be printed
     * @param dpi if non-zero then the image will be rasterized at the given DPI
     */
    public PDFPageableExtend(PDDocument document, Orientation orientation, boolean showPageBorder,
                       float dpi)
    {
        this.document = document;
        this.orientation = orientation;
        this.showPageBorder = showPageBorder;
        this.dpi = dpi;
    }

    @Override
    public int getNumberOfPages()
    {
        return document.getNumberOfPages();
    }

    /**
     * {@inheritDoc}
     * 
     * Returns the actual physical size of the pages in the PDF file. May not fit the local printer.
     */
    @Override
    public PageFormat getPageFormat(int pageIndex)
    {
        PDPage page = document.getPage(pageIndex);
        PDRectangle mediaBox = page.getMediaBox();
        PDRectangle cropBox = page.getCropBox();
        
        // Java does not seem to understand landscape paper sizes, i.e. where width > height, it
        // always crops the imageable area as if the page were in portrait. I suspect that this is
        // a JDK bug but it might be by design, see PDFBOX-2922.
        //
        // As a workaround, we normalise all Page(s) to be portrait, then flag them as landscape in
        // the PageFormat.
        Paper paper;
        boolean isLandscape;
//        if (mediaBox.getWidth() > mediaBox.getHeight())
//        {
//            // rotate
//            paper = new Paper();
//            paper.setSize(mediaBox.getHeight(), mediaBox.getWidth());
//            paper.setImageableArea(cropBox.getLowerLeftY(), cropBox.getLowerLeftX(),
//                    cropBox.getHeight(), cropBox.getWidth());
//            isLandscape = true;
//        }
//        else
//        {
            paper = new Paper();
            paper.setSize(mediaBox.getWidth(), mediaBox.getHeight());
            paper.setImageableArea(cropBox.getLowerLeftX(), cropBox.getLowerLeftY(),
                    cropBox.getWidth(), cropBox.getHeight());
            isLandscape = false;
//        }

        PageFormat format = new PageFormat();
        format.setPaper(paper);
        
        // auto portrait/landscape
        switch (orientation)
        {
            case AUTO:
                format.setOrientation(isLandscape ? PageFormat.LANDSCAPE : PageFormat.PORTRAIT);
                break;
            case LANDSCAPE:
                format.setOrientation(PageFormat.LANDSCAPE);
                break;
            case PORTRAIT:
                format.setOrientation(PageFormat.PORTRAIT);
                break;
            default:
                break;
        }
        
        return format;
    }
    
    @Override
    public Printable getPrintable(int i)
    {
        if (i >= getNumberOfPages())
        {
            throw new IndexOutOfBoundsException(i + " >= " + getNumberOfPages());
        }
        return new PDFPrintable(document, Scaling.ACTUAL_SIZE, showPageBorder, dpi);
    }
    
}
