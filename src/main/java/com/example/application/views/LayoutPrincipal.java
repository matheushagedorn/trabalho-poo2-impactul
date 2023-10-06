package com.example.application.views;

import com.example.application.views.impactul.GanhoCrud;
import com.example.application.views.impactul.GastoCrud;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("home")
@PageTitle("Impactul - HOME")
public class LayoutPrincipal extends AppLayout {
    public LayoutPrincipal() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Impactul - Home");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        Tabs tabs = getTabs();

        addToDrawer(tabs);
        addToNavbar(toggle, title);

        setPrimarySection(Section.DRAWER);

        tabs.addSelectedChangeListener(e -> {
            Tab tabSelecionada = e.getSelectedTab();
            if (tabSelecionada.getLabel().equals("Ganhos")){
                setContent(new GanhoCrud());
            } else if (tabSelecionada.getLabel().equals("Gastos")) {
                setContent(new GastoCrud());
            } else {
                if (getContent() != null){
                    getContent().removeFromParent();
                }
            }
        });
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(createTab(VaadinIcon.DASHBOARD, "Ganhos"),
                createTab(VaadinIcon.CART, "Gastos"));
//                createTab(VaadinIcon.CHART, "Analytics"));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));

        link.setTabIndex(-1);
        Tab tab = new Tab(link);
        tab.setLabel(viewName);

        return tab;
    }
}
