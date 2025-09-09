package app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "planet")
public class Planet {
    @Id
    @Pattern(regexp = "^[A-Z0-9]+$")
    @Column(length = 50, nullable = false)
    private String id;

    @Size(min = 1, max = 500)
    @Column(length = 500, nullable = false)
    private String name;

    @OneToMany(mappedBy = "from", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Ticket> ticketsFrom = new ArrayList<>();

    @OneToMany(mappedBy = "to", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Ticket> ticketsTo = new ArrayList<>();
}
