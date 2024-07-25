package com.uce.edu.util.reportes.inventario;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;

/**
 * PdfGenerator (Pruebas)
 */
public class PdfGenerator {
	/**
	 * Método que permite generar un PDF
	 * 
	 * @param filename
	 * @return
	 */
	public ByteArrayOutputStream generatePdf(String filename) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, byteArrayOutputStream);
			document.open();
			document.add(new Paragraph("Hello World!"));
			document.add(new Paragraph("Welcome to OpenPDF."));
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			document.close();
		}
		return byteArrayOutputStream;
	}
}
