package com.eficaztech.sismed.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import com.eficaztech.sismed.model.Consulta;
import com.eficaztech.sismed.model.Empresa;
import com.eficaztech.sismed.model.EmpresaDao;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Receita {

	public static void visualizar(Consulta consulta, String receita, Window window) {
		if (consulta.getMedico().getReceita().equalsIgnoreCase("receitasolange")) {
			visualizarReceitaSolange(consulta, receita, window);
		} else {
			visualizarReceitaNormal(consulta, receita, window);
		}
	}

	private static void visualizarReceitaNormal(Consulta consulta, String receita, Window window) {
		try {

			Font font = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

			EmpresaDao dao = new EmpresaDao();
			Empresa empresa = dao.findFirst();

			File temp = File.createTempFile("receita", ".pdf");

			Document document = new Document();

			PdfWriter.getInstance(document, new FileOutputStream(temp));
			document.setPageSize(PageSize.A5);
			document.open();

			// cabecalho
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			float[] widths = { 1, 6 };
			table.setWidths(widths);
			PdfPCell medico = new PdfPCell(new Paragraph(consulta.getMedico().getNome(), font));
			PdfPCell especialidade = new PdfPCell(new Paragraph(consulta.getMedico().getEspecialidade() + " - " + consulta.getMedico().getConselhoProfissional().name() + ": " + consulta.getMedico().getNumeroConselho(), font));
			PdfPCell nome_empresa = new PdfPCell(new Paragraph(empresa.getEndereco(), font));
			PdfPCell telefone = new PdfPCell(new Paragraph("Telefone: " + empresa.getTelefones(), font));
			PdfPCell titulo = new PdfPCell(new Paragraph("R E C E IT A", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));

			PdfPCell label_paciente = new PdfPCell(new Paragraph("Paciente:", font));
			PdfPCell paciente = new PdfPCell(new Paragraph(consulta.getCliente().getNome(), font));
			PdfPCell label_data = new PdfPCell(new Paragraph("Data:", font));
			PdfPCell data = new PdfPCell(new Paragraph(consulta.getDataBR(), font));
			medico.setColspan(2);
			medico.setBorder(PdfPCell.TOP);
			medico.setPaddingTop(10);
			medico.setHorizontalAlignment(Element.ALIGN_CENTER);
			especialidade.setBorder(PdfPCell.NO_BORDER);
			especialidade.setColspan(2);
			especialidade.setHorizontalAlignment(Element.ALIGN_CENTER);
			nome_empresa.setColspan(2);
			nome_empresa.setBorder(PdfPCell.NO_BORDER);
			nome_empresa.setHorizontalAlignment(Element.ALIGN_CENTER);
			telefone.setColspan(2);
			telefone.setBorder(PdfPCell.NO_BORDER);
			telefone.setHorizontalAlignment(Element.ALIGN_CENTER);
			titulo.setColspan(2);
			titulo.setBorder(PdfPCell.BOTTOM);
			titulo.setHorizontalAlignment(Element.ALIGN_CENTER);
			titulo.setPaddingTop(10);
			titulo.setPaddingBottom(10);
			label_paciente.setPaddingTop(10);
			label_paciente.setBorder(PdfPCell.NO_BORDER);
			label_data.setPaddingBottom(10);
			label_data.setBorder(PdfPCell.NO_BORDER);
			paciente.setPaddingTop(10);
			paciente.setBorder(PdfPCell.NO_BORDER);
			data.setPaddingBottom(10);
			data.setBorder(PdfPCell.NO_BORDER);
			label_data.setBorder(PdfPCell.BOTTOM);
			data.setBorder(PdfPCell.BOTTOM);
			table.addCell(medico);
			table.addCell(especialidade);
			table.addCell(nome_empresa);
			table.addCell(telefone);
			table.addCell(titulo);
			table.addCell(label_paciente);
			table.addCell(paciente);
			table.addCell(label_data);
			table.addCell(data);
			document.add(table);

			// receita
			Paragraph p = new Paragraph(receita, font);
			p.setSpacingBefore(20);
			document.add(p);
			document.close();

			Hashtable<String, String> h = new Hashtable<String, String>();
			h.put("File", temp.getAbsolutePath());

			Executions.getCurrent().createComponents("/pdf/pdf.zul", window, h);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void visualizarReceitaSolange(Consulta consulta, String receita, Window window) {
		try {

			Font font = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);

			File temp = File.createTempFile("receita", ".pdf");

			Document document = new Document();

			PdfWriter.getInstance(document, new FileOutputStream(temp));
			document.setMargins(160, 0, 90, 240);
			document.setPageSize(PageSize.A4);

			document.open();

			// cabecalho
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			float[] widths = { 1, 6 };
			table.setWidths(widths);

			PdfPCell label_paciente = new PdfPCell(new Paragraph("Paciente:", font));
			PdfPCell paciente = new PdfPCell(new Paragraph(consulta.getCliente().getNome(), font));
			PdfPCell label_data = new PdfPCell(new Paragraph("Data:", font));
			PdfPCell data = new PdfPCell(new Paragraph(consulta.getDataBR(), font));

			label_paciente.setPaddingTop(5);
			label_paciente.setBorder(PdfPCell.NO_BORDER);
			paciente.setPaddingTop(5);
			paciente.setBorder(PdfPCell.NO_BORDER);
			label_data.setPaddingBottom(5);
			label_data.setBorder(PdfPCell.NO_BORDER);
			data.setPaddingBottom(5);
			data.setBorder(PdfPCell.NO_BORDER);
			table.addCell(label_paciente);
			table.addCell(paciente);
			table.addCell(label_data);
			table.addCell(data);
			document.add(table);

			// receita
			Paragraph p = new Paragraph(receita, font);
			p.setSpacingBefore(10);
			document.add(p);
			document.close();

			Hashtable<String, String> h = new Hashtable<String, String>();
			h.put("File", temp.getAbsolutePath());

			Executions.getCurrent().createComponents("/pdf/pdf.zul", window, h);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
