package com.example.application.views.impactul;

import com.example.application.dao.LoginDao;
import com.example.application.database.dataprovider.GanhoDataProvider;
import com.example.application.entity.dto.Ganho;
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


@Route("ganhos")
public class GanhoCrud extends Div {

    List<Ganho> database = GanhoDataProvider.DATABASE;

    private Crud<Ganho> crud;

    public GanhoCrud() {
        crud = new Crud<>(Ganho.class, createGrid(), createEditor());

        setupDataProvider();
        setupToolbar();

        add(crud);
    }

    private CrudEditor<Ganho> createEditor() {
        TextField nome = new TextField("Nome");
        NumberField valor = new NumberField("Valor");
        Div realPreffix = new Div();
        realPreffix.setText("R$");
        valor.setPrefixComponent(realPreffix);
        DatePicker data = new DatePicker("Data");
        ComboBox<Login> user = new ComboBox<>("Usuario");
        user.setItems(LoginDao.buscarLoginsLogados());
        user.setItemLabelGenerator(Login::getUsuario);
        FormLayout form = new FormLayout(nome, valor, data,
                user);

        Binder<Ganho> binder = new Binder<>(Ganho.class);
        binder.forField(nome).asRequired().bind(Ganho::getNome,
                Ganho::setNome);
        binder.forField(valor).asRequired().bind(Ganho::getValor,
                Ganho::setValor);
        binder.forField(data).asRequired().bind(Ganho::getData,
                Ganho::setData);
        binder.forField(user).asRequired().bind(Ganho::getUsuario,
                Ganho::setUsuario);

        return new BinderCrudEditor<>(binder, form);
    }

    private Grid<Ganho> createGrid() {
        Grid<Ganho> grid = new Grid<>(Ganho.class, false);
        Crud.addEditColumn(grid);
        grid.addColumn(Ganho::getNome).setHeader("Nome");
        grid.addColumn(Ganho::getValor).setHeader("Valor").setSortable(true);
        Renderer<Ganho> dataCriacao = new TextRenderer<>(ganho ->
                getDataCriacao(ganho)
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        grid.addColumn(dataCriacao).setHeader("Data de Criação").setSortable(true).setComparator(ganho -> getDataCriacao(ganho).toString());
        grid.addColumn(ganho -> ganho.getUsuario().getUsuario()).setHeader("Usuario").setSortable(true);
        return grid;
    }

    private void setupDataProvider() {
        GanhoDataProvider dataProvider = new GanhoDataProvider();
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

    private static LocalDate getDataCriacao(Ganho ganho) {
        return ganho.getData();
    }

    private void setupToolbar() {
        Button button = new Button("Adicionar ganho", VaadinIcon.PLUS.create());
        button.addClickListener(event -> {
            crud.edit(new Ganho(), Crud.EditMode.NEW_ITEM);
        });
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        crud.setNewButton(button);
    }

}
