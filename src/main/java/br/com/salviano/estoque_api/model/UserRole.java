package br.com.salviano.estoque_api.model;

public enum UserRole {

    GERENTE("gerente"),
    FUNCIONARIO("funcionario");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public  String getRole(){
        return role;
    }
}
