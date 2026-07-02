package edu.jUnitEMosquito.model;

import jakarta.persistence.*;

import java.util.List;

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

    @OneToMany(mappedBy = "group")
    private List<Task> tasks;

}
