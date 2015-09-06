package com.main;

import java.io.FileOutputStream;
import java.io.IOException;

import com.consts.PDFConsts;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.TextMarginFinder;

public class TillingHero {

	/**
	 * Manipulates a PDF file src with the file dest as result
	 * 
	 * @param src
	 *            the original PDF
	 * @param dest
	 *            the resulting PDF
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void manipulatePdf(String src, String dest) throws IOException,
			DocumentException {
		// Creating a reader
		float y;
		PdfReader reader = new PdfReader(src);
		Rectangle pagesize = reader.getPageSize(2);
		Rectangle rec1 = new Rectangle(pagesize.getHeight() * 9,
				pagesize.getWidth() * 9, 180);
		Document document = new Document(pagesize);
		// Document document2 = new Document(rec1);
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(dest));
		int j = 1;
		document.open();
		PdfContentByte content = writer.getDirectContent();
		writer.setPageSize(rec1);
		while (j <= 8) {
			PdfImportedPage page = writer.getImportedPage(reader, j);
			writer.setPageSize(rec1);
			y = pagesize.getHeight() / 2;
			// content.addTemplate(page, 2, 0, 0, 2, 0, 1);
			content.addTemplate(page, 1, 0, 0, 1, 0, y);
			document.newPage();
			content.addTemplate(page, 1, 0, 0, 1, 0, -y);
			document.newPage();
			j++;
		}
		document.close();
		reader.close();

	}

	public static void main(String[] args) throws IOException,
			DocumentException {
		new TillingHero().manipulatePdf(PDFConsts.RESOURCE,
				PDFConsts.SECONDSTEP);
		new TillingHero().stamp(); 
		// new TillingHero().secondStep(PDFConsts.SECONDSTEP, PDFConsts.RESULT);
	}

	public void secondStep(String src, String dest) throws IOException,
			DocumentException {
		float y;
		PdfReader reader = new PdfReader(src);
		Rectangle pagesize = reader.getPageSizeWithRotation(1);
		// pagesize.setRotation(90);
		Document document = new Document(pagesize);
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(dest));
		int j = 1;
		document.open();
		PdfContentByte content = writer.getDirectContent();
		while (j <= 8) {
			PdfImportedPage page = writer.getImportedPage(reader, j);
			y = pagesize.getHeight() / 2;
			content.addTemplate(page, 1, 0, 0, 1, 0, y);
			document.newPage();
			j++;
		}
		document.close();
		reader.close();

	}

	public void stamp() throws IOException, DocumentException {
		PdfReader reader = new PdfReader(PDFConsts.SECONDSTEP);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
				PDFConsts.RESULT));

		// Go through all pages
		int n = reader.getNumberOfPages();
		for (int i = 1; i < n; i++) {
			Rectangle pageSize = reader.getPageSize(n);
			Rectangle rect = getOutputPageSize(pageSize, reader, n);

			PdfDictionary page = reader.getPageN(n);
			page.put(PdfName.CROPBOX,
					new PdfArray(new float[] { rect.getLeft(),
							rect.getBottom(), rect.getRight(), rect.getTop() }));
			stamper.markUsed(page);
		}
		stamper.close();
	}

	private Rectangle getOutputPageSize(Rectangle pageSize, PdfReader reader,
			int page) throws IOException {
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		TextMarginFinder finder = parser.processContent(page,
				new TextMarginFinder());
		Rectangle result = new Rectangle(finder.getLlx(), finder.getLly()/2,
				finder.getUrx(), finder.getUry()/2);
		System.out.printf("Text/bitmap boundary: %f,%f to %f, %f\n",
				finder.getLlx(), finder.getLly(), finder.getUrx(),
				finder.getUry());
		return result;
	}
}