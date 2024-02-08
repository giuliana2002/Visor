package safa.model;

import java.time.LocalDate;

public class Cuenta {

    private Integer numCuenta;
    private String titular;
    private String nacionalidad;
    private LocalDate fechaApertura;
    private Double saldo;

    public void setNumCuenta(Integer numCuenta) {
        this.numCuenta = numCuenta;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Integer getNumCuenta() {
        return numCuenta;
    }

    public String getTitular() {
        return titular;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public Double getSaldo() {
        return saldo;
    }

    public Cuenta(Integer numCuenta, String titular, String nacionalidad, LocalDate fechaApertura, Double saldo) {
        this.numCuenta = numCuenta;
        this.titular = titular;
        this.nacionalidad = nacionalidad;
        this.fechaApertura = fechaApertura;
        this.saldo = saldo;
    }
}
