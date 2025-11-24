package guedes.gustavo.safenotes.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_scopes")
public class Scope {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scope_id", nullable = false)
    private Long scopeId;

    @Column(name = "name", nullable = false)
    private String name;

    public Scope() {
    }

    public Scope(String name) {
        this.name = name;
    }

    public Long getScopeId() {
        return scopeId;
    }

    public void setScopeId(Long scopeId) {
        this.scopeId = scopeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
