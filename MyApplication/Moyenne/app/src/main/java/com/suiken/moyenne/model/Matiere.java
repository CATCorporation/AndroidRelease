package com.suiken.moyenne.model;

import java.util.ArrayList;

/**
 * Created by Suiken on 22/02/15.
 */
public class Matiere {

    private long id;
    private String libelle;
    private int coefficient;
    private int semestre;
    private ArrayList<Note> notes;

    public Matiere(String libelle){
        this.libelle = libelle;
    }

    public Matiere(long id, String libelle, int coefficient, int semestre, ArrayList<Note> notes){
        this.id = id;
        this.libelle = libelle;
        this.coefficient = coefficient;
        this.semestre = semestre;
        this.notes = notes;
    }

    public Matiere(long id, String libelle, int coefficient, int semestre) {
        this.id = id;
        this.libelle = libelle;
        this.coefficient = coefficient;
        this.semestre = semestre;
    }

    public Matiere(String libelle, int coefficient, int semestre){
        this.libelle = libelle;
        this.coefficient = coefficient;
        this.semestre = semestre;
    }

    public Matiere(Matiere m){
        id = m.getId();
        libelle = m.getLibelle();
        coefficient = m.getCoefficient();
        semestre = m.getSemestre();
        notes = m.getNotes();
    }

    public Matiere(long id){
        this.id = id;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public String getLibelle() {
        return libelle;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String toString(){
        return "Matière : " + libelle + "     Coefficient " + coefficient;
    }
}
