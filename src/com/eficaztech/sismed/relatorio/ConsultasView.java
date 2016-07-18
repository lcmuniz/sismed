package com.eficaztech.sismed.relatorio;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Window;

import com.eficaztech.sismed.model.Consulta;
import com.eficaztech.sismed.model.ConsultaDao;
import com.eficaztech.sismed.model.Empresa;
import com.eficaztech.sismed.model.EmpresaDao;
import com.eficaztech.sismed.model.Medico;
import com.eficaztech.sismed.model.MedicoDao;
import com.eficaztech.sismed.view.View;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Init(superclass = true)
@AfterCompose(superclass = true)
public class ConsultasView extends View {

	@Wire
	Window editWindow;

	@Wire("#editWindow #medicoCombobox")
	Combobox medicoCombobox;

	@Wire("#editWindow #inicioDatebox")
	Datebox inicioDatebox;

	@Wire("#editWindow #fimDatebox")
	Datebox fimDatebox;

	@Wire("#editWindow #mostrarRetornosCheckbox")
	Checkbox mostrarRetornosCheckbox;

	List<Medico> medicos;
	Medico medico;

	@Override
	@NotifyChange("medicos")
	public void afterCompose() {

		MedicoDao dao = new MedicoDao();
		medicos = dao.all();

		if (medicos.size() == 1) {
			medico = medicos.get(0);
			medicoCombobox.setReadonly(true);
			inicioDatebox.setFocus(true);
		} else {
			medicoCombobox.setReadonly(false);
			medicoCombobox.setFocus(true);
		}

		inicioDatebox.setValue(new Date());
		fimDatebox.setValue(new Date());
		mostrarRetornosCheckbox.setChecked(true);

	}

	@Command
	public void imprimir() {

		ConsultaDao dao = new ConsultaDao();
		List<Consulta> consultas = dao.find("", inicioDatebox.getValue(), fimDatebox.getValue(), mostrarRetornosCheckbox.isChecked());
		receita(consultas);
	}

	private void receita(List<Consulta> consultas) {

		try {

			Font font = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

			EmpresaDao dao = new EmpresaDao();
			Empresa empresa = dao.findFirst();

			File temp = File.createTempFile("consultas", ".pdf");

			Document document = new Document();

			PdfWriter.getInstance(document, new FileOutputStream(temp));
			document.setPageSize(PageSize.A4);
			document.open();

			// cabecalho
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			float[] widths = { 1, 6 };
			table.setWidths(widths);
			PdfPCell medicoCell = new PdfPCell(new Paragraph(medico.getNome(), font));
			PdfPCell especialidadeCell = new PdfPCell(new Paragraph(medico.getEspecialidade() + " - " + medico.getConselhoProfissional().name() + ": " + medico.getNumeroConselho(), font));
			PdfPCell nomeEmpresaCell = new PdfPCell(new Paragraph(empresa.getEndereco(), font));
			PdfPCell telefoneCell = new PdfPCell(new Paragraph("Telefone: " + empresa.getTelefones(), font));
			PdfPCell tituloCell = new PdfPCell(new Paragraph("C O N S U L T A S", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));

			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

			PdfPCell periodoCell = new PdfPCell(new Paragraph("Entre " + format.format(inicioDatebox.getValue()) + " e " + format.format(fimDatebox.getValue()), font));
			PdfPCell retornosCell;
			if (mostrarRetornosCheckbox.isChecked()) {
				retornosCell = new PdfPCell(new Paragraph("Mostrando os retornos", font));
			} else {
				retornosCell = new PdfPCell(new Paragraph("Sem os retornos", font));
			}

			medicoCell.setColspan(2);
			medicoCell.setBorder(PdfPCell.TOP);
			medicoCell.setPaddingTop(10);
			medicoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			especialidadeCell.setBorder(PdfPCell.NO_BORDER);
			especialidadeCell.setColspan(2);
			especialidadeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			nomeEmpresaCell.setColspan(2);
			nomeEmpresaCell.setBorder(PdfPCell.NO_BORDER);
			nomeEmpresaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			telefoneCell.setColspan(2);
			telefoneCell.setBorder(PdfPCell.NO_BORDER);
			telefoneCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			tituloCell.setColspan(2);
			tituloCell.setBorder(PdfPCell.BOTTOM);
			tituloCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			tituloCell.setPaddingTop(10);
			tituloCell.setPaddingBottom(10);
			periodoCell.setColspan(2);
			periodoCell.setBorder(PdfPCell.NO_BORDER);
			periodoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			periodoCell.setPaddingTop(10);
			periodoCell.setPaddingBottom(2);
			retornosCell.setColspan(2);
			retornosCell.setBorder(PdfPCell.NO_BORDER);
			retornosCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			retornosCell.setPaddingTop(2);
			retornosCell.setPaddingBottom(10);
			table.addCell(medicoCell);
			table.addCell(especialidadeCell);
			table.addCell(nomeEmpresaCell);
			table.addCell(telefoneCell);
			table.addCell(tituloCell);
			table.addCell(periodoCell);
			table.addCell(retornosCell);
			document.add(table);

			PdfPTable table2;

			if (mostrarRetornosCheckbox.isChecked()) {
				table2 = new PdfPTable(5);
				float[] widths1 = { 2, 7, 3, 1, 1 };
				table2.setWidths(widths1);
			} else {
				table2 = new PdfPTable(4);
				float[] widths1 = { 2, 7, 3, 1 };
				table2.setWidths(widths1);
			}

			table2.setWidthPercentage(100);
			table2.setPaddingTop(10);

			Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
			PdfPCell data1 = new PdfPCell(new PdfPCell(new Paragraph("Data", font1)));
			data1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			PdfPCell cliente1 = new PdfPCell(new PdfPCell(new Paragraph("Cliente", font1)));
			cliente1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			PdfPCell plano1 = new PdfPCell(new PdfPCell(new Paragraph("Plano de Saúde", font1)));
			plano1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			PdfPCell retorno1 = new PdfPCell(new PdfPCell(new Paragraph("Ret.", font1)));
			retorno1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			PdfPCell procedimento1 = new PdfPCell(new PdfPCell(new Paragraph("Proc.", font1)));
			procedimento1.setBackgroundColor(BaseColor.LIGHT_GRAY);

			table2.addCell(data1);
			table2.addCell(cliente1);
			table2.addCell(plano1);
			if (mostrarRetornosCheckbox.isChecked()) {
				table2.addCell(retorno1);
			}
			table2.addCell(procedimento1);

			for (Consulta consulta : consultas) {

				Paragraph data = new Paragraph(consulta.getDataBR(), font);
				Paragraph cliente = new Paragraph(consulta.getCliente().getNome(), font);
				Paragraph plano = new Paragraph(consulta.getCliente().getPlanoSaude().getNome(), font);
				Paragraph retorno = new Paragraph(consulta.getRetorno().getNome(), font);
				Paragraph procedimento = new Paragraph(consulta.getProcedimento().getNome(), font);

				table2.addCell(new PdfPCell(data));
				table2.addCell(new PdfPCell(cliente));
				table2.addCell(new PdfPCell(plano));
				if (mostrarRetornosCheckbox.isChecked()) {
					table2.addCell(new PdfPCell(retorno));
				}
				table2.addCell(new PdfPCell(procedimento));

			}
			document.add(table2);

			document.add(new Paragraph("Total de consultas: " + consultas.size(), font));

			document.close();

			Hashtable<String, String> h = new Hashtable<String, String>();
			h.put("File", temp.getAbsolutePath());

			Executions.getCurrent().createComponents("/pdf/pdf.zul", editWindow, h);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<Medico> getMedicos() {
		return medicos;
	}

	public void setMedicos(List<Medico> medicos) {
		this.medicos = medicos;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

}
