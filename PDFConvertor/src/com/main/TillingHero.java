package com.main;

import java.io.FileOutputStream;
import java.io.IOException;

import com.consts.PDFConsts;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

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
		PdfReader reader = new PdfReader(src);
		Rectangle pagesize = reader.getPageSize(2);
		Document document = new Document(pagesize);
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(dest));
		int j = 1;
		document.open();
		PdfContentByte content = writer.getDirectContent();
		while (j <= 8) {
			PdfImportedPage page = writer.getImportedPage(reader, j);
			float x, y;
			// for (int i = 0; i < 2; i++) {
			// x = pagesize.getWidth()*(i/2-i) ;//* (i % 2);
			// y = pagesize.getHeight()*i;// * (i / 2 - 1);
			y = pagesize.getHeight()/2;
			// content.addTemplate(page, x, y);
			content.addTemplate(page, 1, 0, 0, 1, 0, y);
			document.newPage();
			// }
			j++;
		}
		// step 4
		document.close();
		reader.close();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            no arguments needed
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException,
			DocumentException {
		new TillingHero().manipulatePdf(PDFConsts.RESOURCE, PDFConsts.RESULT);
	}
}