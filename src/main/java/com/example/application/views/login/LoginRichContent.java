package com.example.application.views.login;

import com.example.application.dao.LoginDao;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Impactul - LOGIN")
@Route(value = "")
public class LoginRichContent extends Div {

    public LoginRichContent() {
        LoginDao.desativarTodosLogados();

        LoginI18n i18n = LoginI18n.createDefault();

        i18n.setAdditionalInformation(
                "Para acessar o sistema, entre em contato com o administrador do sistema.");
        i18n.getForm().setSubmit("Entrar");
        i18n.getForm().setTitle("Logar");
        i18n.getForm().setUsername("Usuario");
        i18n.getForm().setPassword("Senha");
        i18n.getForm().setForgotPassword("Esqueceu sua senha?");
        i18n.getErrorMessage().setTitle("Usuário ou senha inválidos.");
        i18n.getErrorMessage().setMessage("Usuário ou senha inválidos. Por favor, tente novamente.");

        LoginOverlay loginOverlay = new LoginOverlay();
        loginOverlay.setI18n(i18n);
        loginOverlay.setTitle("Impactul");
        loginOverlay.setDescription("Um nome que sugere impacto positivo e soluções inteligentes para controlar seus gastos.");

        loginOverlay.addLoginListener(e -> {
            boolean isAuthenticated = loginEvento(e.getUsername(), e.getPassword());
            if (isAuthenticated) {
                loginOverlay.close();
                direcionarTelaInicial();
            } else {
                loginOverlay.setError(true);
            }
        });

        loginOverlay.addForgotPasswordListener(e -> {
            esqueceuSenhaEvento();
        });

        add(loginOverlay);
        loginOverlay.setOpened(true);

        loginOverlay.getElement().setAttribute("no-autofocus", "");
    }

    private void direcionarTelaInicial() {
        UI.getCurrent().navigate("home");
    }

    public Boolean loginEvento(String username, String senha){
        Integer value = LoginDao.verificarUsuarioSenha(username, senha);
        if (value != 0){
            LoginDao.atualizarLoginParaLogado(value);
            return true;
        } else {
            LoginDao.desativarTodosLogados();
            return false;
        }
    }

    public void esqueceuSenhaEvento(){
        Notification notification = Notification.show("Para alterar sua senha converse com o administrador do sistema.",
                5000, Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }


}
