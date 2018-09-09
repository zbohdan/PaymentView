package com.zbohdan.payment.beans;

import com.zbohdan.payment.dao.AccountsFacade;
import com.zbohdan.payment.entities.Accounts;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class AccountBean implements Serializable {

    @Inject
    private AccountsFacade accountEJB;

    private List<Accounts> accounts;
    private Accounts accountSelection;
    private Accounts newAccount;

    public AccountBean() {
    }

    @PostConstruct
    public void init() {
        reinit();
    }

    private void reinit() {
        accounts = accountEJB.findAll();
        accountSelection = accounts.get(0);
        newAccount = new Accounts();
    }

    public void updateAccount() {
        accountEJB.edit(accountSelection);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Updated!",
                        "Account " + accountSelection.getId() + " has been successfully updated!"));
        reinit();
    }

    public void deleteAccount(Accounts account) {
        accountEJB.remove(account);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Deleted!",
                        "Account " + account.getId() + " has been deleted!"));
        reinit();
    }

    public void createAccount() {
        newAccount.setCreatedAt(new Date());
        newAccount.setUpdatedAt(new Date());
        
        accountEJB.create(newAccount);
        newAccount = new Accounts();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Created!",
                        "New Account created successfully"));
        reinit();

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Payments/faces/index.xhtml");
        } catch (IOException e) {
            System.out.println("Unable to work with external context and thus you cannot be redirected to the page!");
        }
    }

    public List<Accounts> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Accounts> accounts) {
        this.accounts = accounts;
    }

    public Accounts getAccountSelection() {
        return accountSelection;
    }

    public void setAccountSelection(Accounts accountSelection) {
        System.out.println("Selected! " + accountSelection.toString());
        this.accountSelection = accountSelection;
    }

    public Accounts getNewAccount() {
        return newAccount;
    }

    public void setNewAccount(Accounts newAccount) {
        this.newAccount = newAccount;
    }

}
