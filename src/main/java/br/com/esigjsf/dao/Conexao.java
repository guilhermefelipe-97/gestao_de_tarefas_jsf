package br.com.esigjsf.dao;

import java.sql.*;

public class Conexao {
    private static final String URL = "jdbc:postgresql://localhost:5432/tarefasJSF";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "admin";

    public static Connection conectar() throws SQLException, ClassNotFoundException {
    	Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
    
    public static void main(String[] args) {
    	try {
			System.out.println(Conexao.conectar());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
