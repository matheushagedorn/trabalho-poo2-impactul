package com.example.application.views.impactul;

import com.example.application.dao.LoginDao;
import com.example.application.database.dataprovider.GanhoDataProvider;
import com.example.application.database.dataprovider.GastoDataProvider;
import com.example.application.entity.dto.Ganho;
import com.example.application.entity.dto.Gasto;
import com.example.application.entity.dto.Login;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.List;

@Route("gastos")
public class GastoCrud extends Div {
    List<Ganho> database = GanhoDataProvider.DATABASE;

    private Crud<Gasto> crud;

    public GastoCrud() {
        crud = new Crud<>(Gasto.class, createGrid(), createEditor());

        setupDataProvider();
        setupToolbar();

        add(crud);
    }

    private CrudEditor<Gasto> createEditor() {
        TextField nome = new TextField("Nome");
        NumberField valor = new NumberField("Valor");
        Div realPreffix = new Div();
        realPreffix.setText("R$");
        valor.setPrefixComponent(realPreffix);
        DatePicker data = new DatePicker("Data");
        ComboBox<Login> user = new ComboBox<>("Usuario");
        ComboBox<String> formasPagamento = new ComboBox<>("Forma de Pagamento");
        formasPagamento.setItems(formasPagamentos());
        user.setItems(LoginDao.buscarLoginsLogados());
        user.setItemLabelGenerator(Login::getUsuario);
        FormLayout form = new FormLayout(nome, valor, data,
                user,formasPagamento);

        Binder<Gasto> binder = new Binder<>(Gasto.class);
        binder.forField(nome).asRequired().bind(Gasto::getNome,
                Gasto::setNome);
        binder.forField(valor).asRequired().bind(Gasto::getValor,
                Gasto::setValor);
        binder.forField(data).asRequired().bind(Gasto::getData,
                Gasto::setData);
        binder.forField(user).asRequired().bind(Gasto::getUsuario,
                Gasto::setUsuario);
        binder.forField(formasPagamento).asRequired().bind(Gasto::getFormaPagamento,
                Gasto::setFormaPagamento);

        return new BinderCrudEditor<>(binder, form);
    }

    private Grid<Gasto> createGrid() {
        Grid<Gasto> grid = new Grid<>(Gasto.class, false);
        Crud.addEditColumn(grid);
        grid.addColumn(Gasto::getNome).setHeader("Nome");
        grid.addColumn(Gasto::getValor).setHeader("Valor").setSortable(true);
        Renderer<Gasto> dataCriacao = new TextRenderer<>(gasto ->
                getDataCriacao(gasto)
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        grid.addColumn(dataCriacao).setHeader("Data de Criação").setSortable(true).setComparator(gasto -> getDataCriacao(gasto).toString());
        grid.addColumn(Gasto::getFormaPagamento).setHeader("Forma de Pagamento").setSortable(true);
        grid.addColumn(ganho -> ganho.getUsuario().getUsuario()).setHeader("Usuario").setSortable(true);
        return grid;
    }

    private void setupDataProvider() {
        GastoDataProvider dataProvider = new GastoDataProvider();
        crud.setDataProvider(dataProvider);
        crud.addDeleteListener(
                deleteEvent -> {
                    dataProvider.delete(deleteEvent.getItem());

                });
        crud.addSaveListener(
                saveEvent -> {
                    dataProvider.persist(saveEvent.getItem());

                });
    }

    private static LocalDate getDataCriacao(Gasto gasto) {
        return gasto.getData();
    }

    private static List<String> formasPagamentos(){
        return List.of("Dinheiro", "Cartão de Crédito", "Cartão de Débito", "Pix", "Boleto", "Cheque");
    }

    private void setupToolbar() {
        Button button = new Button("Adicionar gasto", VaadinIcon.PLUS.create());
        button.addClickListener(event -> {
            crud.edit(new Gasto(), Crud.EditMode.NEW_ITEM);
        });
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        crud.setNewButton(button);
    }
}