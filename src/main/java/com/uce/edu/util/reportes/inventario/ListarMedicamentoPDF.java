package com.uce.edu.util.reportes.inventario;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.uce.edu.repository.modelo.inventarioModelo.Medicamento;
/**
 * ListarMedicamentoPDF
 */
public class ListarMedicamentoPDF {
/**
 * Método que permite generar un PDF con la lista de medicamentos
 * @param listaMedicamentos
 * @return
 * @throws IOException
 */
public ByteArrayOutputStream generatePdfmedicamentos(List<Medicamento> listaMedicamentos) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
    
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
            // Agregar un salto de línea
            document.add(Chunk.NEWLINE);

            // Definir el color azul oscuro
            CMYKColor darkBlueColor = new CMYKColor(100, 90, 10, 0);

            // Crear la fuente con el color azul oscuro
            Font darkBlueFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD, darkBlueColor);

            // Crear el título con la fuente azul oscuro
            Paragraph title = new Paragraph("Lista de Médicamentos", darkBlueFont);

            // Centrar el título
            title.setAlignment(Element.ALIGN_CENTER);

            // Agregar el título al documento
            document.add(title);

            // Agregar un espacio en blanco para separación
            document.add(new Paragraph(" "));
            PdfPTable table = new PdfPTable(9);

            // Definir el color de fondo turquesa oscuro y el texto blanco para los
            // encabezados
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            PdfPCell cell;

            // Agregar los encabezados de la tabla con el color de fondo y texto
            // especificados
            String[] headers = { "Código", "Nombre Comun", "Nombre Marca", "Proveedor", "Ruc Proveedor", "Descripción","Precio Compra", "Precio Venta", "Stock" };
            for (String header : headers) {
                cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(new BaseColor(0, 206, 209)); // Color turquesa oscuro
                table.addCell(cell);
            }

            // Llenar la tabla con los datos de los proveedores
            for (Medicamento insumoMedico : listaMedicamentos) {
                table.addCell(insumoMedico.getCodigo());
                table.addCell(insumoMedico.getNombreComun());
                table.addCell(insumoMedico.getNombreMarca());
                table.addCell(insumoMedico.getProveedor().getNombreEmpresa());
                table.addCell(insumoMedico.getProveedor().getRuc());
                table.addCell(insumoMedico.getDescripcion());
                table.addCell(insumoMedico.getPrecioCompra().toString());
                table.addCell(insumoMedico.getPrecioVenta().toString());
                table.addCell(insumoMedico.getStock().toString());
             
            }
            // Agregar la tabla al documento
            document.add(table);

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return byteArrayOutputStream;
    }
}
