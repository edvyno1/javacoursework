package courseSystem.ds;

import javax.persistence.*;
import java.io.Serializable;
@Entity
public class File implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToOne
    private Folder folder;

    public File(String name, Folder folder) {
        this.name = name;
        this.folder = folder;
    }

    public File() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }
}
