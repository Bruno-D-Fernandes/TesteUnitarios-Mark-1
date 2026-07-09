package edu.jUnitEMosquito.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "task_groups")
public class Group {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Usuario lider;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<Task> tasks;

    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioGrupo> usuarioGrupos;

    @OneToMany(mappedBy = "group", cascade = {CascadeType.ALL},orphanRemoval = true)
    private List<Tags> tags;

    public Group() {
    }

    public Group(String nome, Usuario lider) {
        this.nome = nome;
        this.lider = lider;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(nome, group.nome) && Objects.equals(lider, group.lider) && Objects.equals(tasks, group.tasks) && Objects.equals(usuarioGrupos, group.usuarioGrupos) && Objects.equals(tags, group.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Usuario getLider() {
        return lider;
    }

    public void setLider(Usuario lider) {
        this.lider = lider;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<UsuarioGrupo> getUsuarioGrupos() {
        return usuarioGrupos;
    }

    public void setUsuarioGrupos(List<UsuarioGrupo> usuarioGrupos) {
        this.usuarioGrupos = usuarioGrupos;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }
}
