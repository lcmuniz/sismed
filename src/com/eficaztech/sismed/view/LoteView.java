package com.eficaztech.sismed.view;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.joda.time.DateTime;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import com.eficaztech.sismed.model.GuiaConsulta;
import com.eficaztech.sismed.model.GuiaConsultaDao;
import com.eficaztech.sismed.model.GuiaSADT;
import com.eficaztech.sismed.model.GuiaSADTDao;
import com.eficaztech.sismed.model.Lote;
import com.eficaztech.sismed.model.LoteDao;
import com.eficaztech.sismed.model.Medico;
import com.eficaztech.sismed.model.MedicoDao;
import com.eficaztech.sismed.model.PlanoSaude;
import com.eficaztech.sismed.model.PlanoSaudeDao;
import com.eficaztech.sismed.util.Notification;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

@Init(superclass = true)
@AfterCompose(superclass = true)
public class LoteView extends View {

	private List<Lote> lotes;

	private List<Medico> medicos;
	private List<PlanoSaude> planos;

	private Lote loteSelecionado;

	// para a tela de procura de guias de consulta
	private List<GuiaConsulta> guiasConsulta;
	private List<GuiaConsulta> guiasConsultaSelecionadas;

	// para a tela de procura de guias sadt
	private List<GuiaSADT> guiasSADT;
	private List<GuiaSADT> guiasSADTSelecionadas;

	@Wire
	Window editWindow;

	@Wire
	Window guiasConsultaWindow;

	@Wire
	Window guiasSADTWindow;

	@Wire
	Listbox guiaListbox;

	@Wire
	Combobox filtroTipoConsultaCombobox;

	@Wire
	Combobox filtroMedicoCombobox;

	@Wire
	Combobox filtroPlanoSaudeCombobox;

	@Wire("#editWindow #tipoConsultaCombobox")
	Combobox tipoConsultaCombobox;

	@Wire("#editWindow #excluirButton")
	Button excluirButton;

	@Wire("#editWindow #dataEnvioDatebox")
	Datebox dataEnvioDatebox;

	@Wire("#editWindow #guiaConsultaRow")
	Row guiaConsultaRow;

	@Wire("#editWindow #guiaSADTRow")
	Row guiaSADTRow;

	@Wire("#guiasConsultaWindow #filtroInicioConsultaDatebox")
	Datebox filtroInicioConsultaDatebox;

	@Wire("#guiasConsultaWindow #filtroFimConsultaDatebox")
	Datebox filtroFimConsultaDatebox;

	@Wire("#guiasSADTWindow #filtroInicioSADTDatebox")
	Datebox filtroInicioSADTDatebox;

	@Wire("#guiasSADTWindow #filtroFimSADTDatebox")
	Datebox filtroFimSADTDatebox;

	@Override
	public void afterCompose() {

		iniciarComboboxes();

		pesquisar();
	}

	private void iniciarComboboxes() {

		MedicoDao dao2 = new MedicoDao();
		setMedicos(dao2.all());
		BindUtils.postNotifyChange(null, null, LoteView.this, "medicos");

		filtroMedicoCombobox.setValue(getMedicos().get(0).getNome());

		PlanoSaudeDao dao3 = new PlanoSaudeDao();
		setPlanos(dao3.all());
		BindUtils.postNotifyChange(null, null, LoteView.this, "planos");

		filtroPlanoSaudeCombobox.setValue(getPlanos().get(0).getNome());

	}

	@Command
	@NotifyChange("lotes")
	public void pesquisar() {
		loteSelecionado = null;
		filtroTipoConsultaCombobox.setFocus(true);
		LoteDao dao = new LoteDao();
		setLotes(dao.find(filtroTipoConsultaCombobox.getValue(), filtroMedicoCombobox.getValue(), filtroPlanoSaudeCombobox.getValue()));
	}

	@Command
	@NotifyChange("loteSelecionado")
	public void novo() {

		LoteDao dao = new LoteDao();
		int numeroLote = dao.nextNumeroLote();

		loteSelecionado = new Lote();

		getLoteSelecionado().setDataEnvio(new Date());
		getLoteSelecionado().setNumeroLote(numeroLote);
		
		if (medicos.size() == 1) {
			getLoteSelecionado().setMedico(medicos.get(0));
		}

		editWindow.setTitle("Nova Lote");
		editWindow.doModal();
		excluirButton.setDisabled(true);
		dataEnvioDatebox.setFocus(true);
	}

	@Command
	@NotifyChange("loteSelecionado")
	public void editar(@BindingParam("id") Long id) {
		LoteDao dao = new LoteDao();

		loteSelecionado = dao.find(id);

		alterarTipoConsulta();

		editWindow.setTitle("Editar Lote");
		editWindow.doModal();

		excluirButton.setDisabled(false);
		dataEnvioDatebox.setFocus(true);
	}

	@Command
	@NotifyChange("lotes")
	public void salvar() {
		LoteDao dao = new LoteDao();

		try {
			dao.save(loteSelecionado);

			salvarGuias();

			Notification.show("success", "Item salvo com sucesso.");
		} catch (ValidationException e) {
			Validations.show(editWindow, e.getErros());
		}

		editWindow.setVisible(false);
		loteSelecionado = null;
		pesquisar();
	}

	private void salvarGuias() {

		GuiaConsultaDao daoConsulta = new GuiaConsultaDao();
		GuiaSADTDao daoSADT = new GuiaSADTDao();

		// remover o lote_id de todas as guias de consultas deste lote
		daoConsulta.removerGuiasDoLote(loteSelecionado);

		// remover o lote_id de todas as guias de consultas deste lote
		daoSADT.removerGuiasDoLote(loteSelecionado);

		if (loteSelecionado.getTipoLote().equals("CONSULTA")) {

			// atualizar o lote_id das guias selecionadas para este lote
			daoConsulta.colocarGuiasNoLote(loteSelecionado, loteSelecionado.getGuiasConsulta());

		} else {

			// atualizar o lote_id das guias selecionadas para este lote
			daoSADT.colocarGuiasNoLote(loteSelecionado, loteSelecionado.getGuiasSADT());

		}

	}

	@Command
	public void alterarTipoConsulta() {
		if (loteSelecionado.getTipoLote().equals("CONSULTA")) {
			guiaConsultaRow.setVisible(true);
			guiaSADTRow.setVisible(false);
		} else {
			guiaConsultaRow.setVisible(false);
			guiaSADTRow.setVisible(true);
		}
	}

	@Command
	@NotifyChange("guiasConsulta")
	public void verGuiasConsulta() {
		
		if (loteSelecionado.getTipoLote() == null || loteSelecionado.getMedico() == null || loteSelecionado.getPlanoSaude() == null) {
			Messagebox.show("Preencha as informações do LOTE antes de adicionar guias.", "Sismed", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		
		guiasConsultaSelecionadas = loteSelecionado.getGuiasConsulta();
		DateTime inicioMes = new DateTime().dayOfMonth().withMinimumValue();
		DateTime fimMes = new DateTime().dayOfMonth().withMaximumValue();
		filtroInicioConsultaDatebox.setValue(inicioMes.toDate());
		filtroFimConsultaDatebox.setValue(fimMes.toDate());
		guiasConsultaWindow.doModal();
		pesquisarGuiasConsulta();
	}

	@Command
	@NotifyChange("guiasSADT")
	public void verGuiasSADT() {
		
		if (loteSelecionado.getTipoLote().isEmpty() || loteSelecionado.getMedico() == null || loteSelecionado.getPlanoSaude() == null) {
			Messagebox.show("Preencha as informações do LOTE antes de adicionar guias.", "Sismed", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		
		guiasSADTSelecionadas = loteSelecionado.getGuiasSADT();
		DateTime inicioMes = new DateTime().dayOfMonth().withMinimumValue();
		DateTime fimMes = new DateTime().dayOfMonth().withMaximumValue();
		filtroInicioSADTDatebox.setValue(inicioMes.toDate());
		filtroFimSADTDatebox.setValue(fimMes.toDate());
		guiasSADTWindow.doModal();
		pesquisarGuiasSADT();
	}

	@Command
	@NotifyChange("guiasConsulta")
	public void pesquisarGuiasConsulta() {
		GuiaConsultaDao dao = new GuiaConsultaDao();
		guiasConsulta = dao.find(loteSelecionado, loteSelecionado.getMedico(), loteSelecionado.getPlanoSaude(), filtroInicioConsultaDatebox.getValue(), filtroFimConsultaDatebox.getValue());
	}

	@Command
	@NotifyChange("guiasSADT")
	public void pesquisarGuiasSADT() {
		GuiaSADTDao dao = new GuiaSADTDao();
		setGuiasSADT(dao.find(loteSelecionado, loteSelecionado.getMedico(), loteSelecionado.getPlanoSaude(), filtroInicioSADTDatebox.getValue(), filtroFimSADTDatebox.getValue()));
	}

	@Command
	public void cancelarAdicionarGuiasConsulta() {
		guiasConsultaWindow.setVisible(false);
	}

	@Command
	public void cancelarAdicionarGuiasSADT() {
		guiasSADTWindow.setVisible(false);
	}

	@Command
	@NotifyChange("loteSelecionado")
	public void adicionarGuiasConsultaMarcadas() {
		loteSelecionado.setGuiasConsulta(guiasConsultaSelecionadas);
		guiasConsultaWindow.setVisible(false);
	}

	@Command
	@NotifyChange("loteSelecionado")
	public void adicionarGuiasSADTMarcadas() {
		loteSelecionado.setGuiasSADT(getGuiasSADTSelecionadas());
		guiasSADTWindow.setVisible(false);
	}

	@Command
	public void excluir() {

		Messagebox.show("Excluir este LOTE?", "Sismed", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener<Event>() {
			@Override
			public void onEvent(Event e) {
				if (Messagebox.ON_YES.equals(e.getName())) {
					LoteDao dao = new LoteDao();
					dao.delete(loteSelecionado);

					GuiaConsultaDao dao1 = new GuiaConsultaDao();
					dao1.removerGuiasDoLote(loteSelecionado);

					Notification.show("success", "Item excluído com sucesso.");
					editWindow.setVisible(false);
					BindUtils.postNotifyChange(null, null, LoteView.this, "lotes");
					pesquisar();
				}
			}
		});

	}

	@Command
	@NotifyChange("loteSelecionado")
	public void removerGuiasConsulta() {
		loteSelecionado.setGuiasConsulta(new ArrayList<GuiaConsulta>());
	}

	@Command
	@NotifyChange("loteSelecionado")
	public void removerGuiasSADT() {
		loteSelecionado.setGuiasSADT(new ArrayList<GuiaSADT>());
	}

	@Command
	@NotifyChange("lotes")
	public void cancelar() {
		loteSelecionado = null;
		editWindow.setVisible(false);
		pesquisar();
	}

	@Command
	public void download(@BindingParam("id") Long id) {
		try {
			LoteDao dao = new LoteDao();
			Lote lote = dao.find(id);

			String filename = "lote" + lote.getNumeroLote();
			String xml = lote.xml();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			ZipOutputStream zip = new ZipOutputStream(baos);
			zip.putNextEntry(new ZipEntry(filename + ".xml"));
			zip.write(xml.getBytes());
			zip.close();

			Filedownload.save(baos.toByteArray(), "application/zip", filename + ".zip");

		} catch (Exception e) {
		}

	}

	public List<Lote> getLotes() {
		return lotes;
	}

	public void setLotes(List<Lote> lotes) {
		this.lotes = lotes;
	}

	public Lote getLoteSelecionado() {
		return loteSelecionado;
	}

	public void setLoteSelecionado(Lote loteSelecionado) {
		this.loteSelecionado = loteSelecionado;
	}

	public List<Medico> getMedicos() {
		return medicos;
	}

	public void setMedicos(List<Medico> medicos) {
		this.medicos = medicos;
	}

	public List<PlanoSaude> getPlanos() {
		return planos;
	}

	public void setPlanos(List<PlanoSaude> planos) {
		this.planos = planos;
	}

	public List<GuiaConsulta> getGuiasConsulta() {
		return guiasConsulta;
	}

	public void setGuiasConsulta(List<GuiaConsulta> guiasConsulta) {
		this.guiasConsulta = guiasConsulta;
	}

	public List<GuiaConsulta> getGuiasConsultaSelecionadas() {
		return guiasConsultaSelecionadas;
	}

	public void setGuiasConsultaSelecionadas(List<GuiaConsulta> guiasConsultaSelecionadas) {
		this.guiasConsultaSelecionadas = guiasConsultaSelecionadas;
	}

	public List<GuiaSADT> getGuiasSADT() {
		return guiasSADT;
	}

	public void setGuiasSADT(List<GuiaSADT> guiasSADT) {
		this.guiasSADT = guiasSADT;
	}

	public List<GuiaSADT> getGuiasSADTSelecionadas() {
		return guiasSADTSelecionadas;
	}

	public void setGuiasSADTSelecionadas(List<GuiaSADT> guiasSADTSelecionadas) {
		this.guiasSADTSelecionadas = guiasSADTSelecionadas;
	}

}
