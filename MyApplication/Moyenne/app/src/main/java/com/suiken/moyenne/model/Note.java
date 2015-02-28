package com.suiken.moyenne.model;

/**
 * Created by Suiken on 26/02/15.
 */
public class Note {

    private long id;
    private float note;
    private Matiere matiere;

    public Note(long id, float note) {
        this.id = id;
        this.note = note;
    }

    public Note(Note note){
        this.id = note.getId();
        this.note = note.getNote();
        this.matiere = note.getMatiere();
    }

    public Note(float note, Matiere matiere){
        this.note = note;
        this.matiere = matiere;
    }

    public Note(float note){
        this.note = note;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public Note(long id, float note, Matiere matiere){
        this.id = id;
        this.note = note;
        this.matiere = matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public String toString(){
        return String.valueOf(note);
    }
}
