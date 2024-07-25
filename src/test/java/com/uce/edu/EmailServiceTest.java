package com.uce.edu;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.uce.edu.service.emailService.EmailService;
import com.uce.edu.util.reportes.inventario.PdfGenerator;

/**
 * EmailServiceTest
 
 */
@SpringBootTest
public class EmailServiceTest {

	/**
	 * Dependencias
	 */
	@Autowired
	private EmailService emailService;

	/**
	 * Método que permite probar el envío de un email con un archivo PDF adjunto
	 * @throws Exception
	 */
	@Test
	public void testSendEmailWithPdf() throws Exception {
	
		// Datos de prueba
		String to = "edlithvinueza@gmail.com";
		String subject = "Test Subject";
		String text = "Test email body";
        PdfGenerator pdfGenerator = new PdfGenerator();
        ByteArrayOutputStream pdfOutputStream = pdfGenerator.generatePdf("example.pdf");

		String filename = "test.pdf";

		// Llamar al método bajo prueba
        emailService.sendEmailWithPdf(to, subject, text, pdfOutputStream, filename);

	}
}
