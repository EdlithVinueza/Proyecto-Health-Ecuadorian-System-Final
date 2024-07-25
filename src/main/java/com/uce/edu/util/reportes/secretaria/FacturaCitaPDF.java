package com.uce.edu.util.reportes.secretaria;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.uce.edu.repository.modelo.FacturaCita;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
/**
 * FacturaCitaPDF
 
 */
public class FacturaCitaPDF {

    /**
     * Método que permite generar un PDF con la información de una factura de cita
     * @param facturaCita
     * @return
     * @throws IOException
     */
    public ByteArrayOutputStream generarPdfFacturaCita(FacturaCita facturaCita) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            // Crear una tabla de 2 columnas para el logo y el nombre de la empresa
            PdfPTable tableLogo = new PdfPTable(2);
            tableLogo.setWidthPercentage(50); // La tabla ocupa el 100% del ancho de la página

            // Ajustar la proporción de la columna
            float[] columnWidths = new float[] { 1f, 4f }; // Proporciones ajustadas
            tableLogo.setWidths(columnWidths);
            tableLogo.setWidthPercentage(50);
            tableLogo.setHorizontalAlignment(15);
            // Paso 1: Obtener el InputStream del logo
            InputStream logoStream = getClass().getResourceAsStream("/static/img/logo.png");
            // Paso 2: Convertir el InputStream a un arreglo de bytes
            byte[] logoBytes = IOUtils.toByteArray(logoStream);
            // Crear un objeto Image usando el arreglo de bytes
            Image logo = Image.getInstance(logoBytes);
            // Escalar y alinear la imagen según sea necesario
            logo.scaleToFit(40, 40); // Ajusta el tamaño según sea necesario
            logo.setAlignment(Image.LEFT); // O cualquier otra alineación deseada

            // Celda para el logo
            // Agregar la imagen al documento
            PdfPCell cellLogo = new PdfPCell(logo, true);
            cellLogo.setBorder(Rectangle.NO_BORDER); // Sin bordes
            tableLogo.addCell(cellLogo);

            // Celda para el nombre de la empresa
            Font blueFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLDITALIC,
                    new CMYKColor(255, 0, 0, 0));
            Phrase companyNamePhrase = new Phrase("\nHealth Ecuadorian System", blueFont);
            PdfPCell cellCompanyName = new PdfPCell(companyNamePhrase);
            cellCompanyName.setBorder(Rectangle.NO_BORDER); // Sin bordes
            cellCompanyName.setHorizontalAlignment(Element.ALIGN_CENTER); // Alineación al centro
            tableLogo.addCell(cellCompanyName);
            tableLogo.setWidthPercentage(50);

            // Agregar la tabla al documento
            tableLogo.setWidthPercentage(50);
            document.setMargins(10, 50, 50, 50);
            document.add(tableLogo);

            // Crear el título del documento
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
            Paragraph title = new Paragraph("Factura de Cita", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Agregar un espacio en blanco para separación
            document.add(new Paragraph(" "));

            // Crear una tabla con dos columnas
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            // Definir las fuentes y colores
            Font cyanFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            Font whiteFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
            BaseColor cyanColor = new BaseColor(0, 255, 255);

            // Añadir las filas con los atributos y sus valores
            addRow(table, "Número de Autorización", facturaCita.getNumeroAutorizacion(), cyanFont, whiteFont, cyanColor);
            addRow(table, "Número de Factura", facturaCita.getNumeroFactura(), cyanFont, whiteFont, cyanColor);
            addRow(table, "Fecha de Emisión", facturaCita.getFechaEmision().toString(), cyanFont, whiteFont, cyanColor);
            addRow(table, "Hora de Emisión", facturaCita.getHoraEmision().toString(), cyanFont, whiteFont, cyanColor);
            addRow(table, "Cédula del Paciente", facturaCita.getCedulaPaciente(), cyanFont, whiteFont, cyanColor);
            addRow(table, "Nombre del Cliente", facturaCita.getNombreCliente(), cyanFont, whiteFont, cyanColor);
            addRow(table, "Dirección del Paciente", facturaCita.getDireccionPaciente(), cyanFont, whiteFont, cyanColor);
            addRow(table, "Teléfono del Paciente", facturaCita.getTelefonoPaciente(), cyanFont, whiteFont, cyanColor);
            addRow(table, "Correo del Paciente", facturaCita.getCorreoPaciente(), cyanFont, whiteFont, cyanColor);
            addRow(table, "Especialidad", facturaCita.getEspecialidad(), cyanFont, whiteFont, cyanColor);
            addRow(table, "Nombre del Auditor", facturaCita.getNombreAuditor(), cyanFont, whiteFont, cyanColor);
            addRow(table, "Cédula del Auditor", facturaCita.getCedulaAuditor(), cyanFont, whiteFont, cyanColor);
            addRow(table, "IVA", facturaCita.getIva().toString(), cyanFont, whiteFont, cyanColor);
            addRow(table, "Descuentos", facturaCita.getDescuentos().toString(), cyanFont, whiteFont, cyanColor);
            addRow(table, "Subtotal", facturaCita.getSubTotal().toString(), cyanFont, whiteFont, cyanColor);
            addRow(table, "Total a Pagar", facturaCita.getTotalPagar().toString(), cyanFont, whiteFont, cyanColor);
 
            // Agregar la tabla al documento
            document.add(table);

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return byteArrayOutputStream;
    }

    private void addRow(PdfPTable table, String attributeName, String attributeValue, Font cyanFont, Font whiteFont,
            BaseColor cyanColor) {
        PdfPCell cellAttributeName = new PdfPCell(new Phrase(attributeName, cyanFont));
        cellAttributeName.setBackgroundColor(cyanColor);
        table.addCell(cellAttributeName);

        PdfPCell cellAttributeValue = new PdfPCell(new Phrase(attributeValue, whiteFont));
        cellAttributeValue.setBackgroundColor(BaseColor.WHITE);
        table.addCell(cellAttributeValue);
    }
}