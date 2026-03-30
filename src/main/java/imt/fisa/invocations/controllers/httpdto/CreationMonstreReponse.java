package imt.fisa.invocations.controllers.httpdto;

public class CreationMonstreReponse {
    private String id;

    public CreationMonstreReponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
